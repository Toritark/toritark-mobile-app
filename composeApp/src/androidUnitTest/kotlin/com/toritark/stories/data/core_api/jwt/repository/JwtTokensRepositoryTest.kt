package com.toritark.stories.data.core_api.jwt.repository

import app.cash.turbine.test
import com.russhwolf.settings.Settings
import com.toritark.stories.data.core_api.jwt.model.AccessTokenState
import com.toritark.stories.data.core_api.jwt.model.RefreshJwtTokenRequest
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class JwtTokensRepositoryTest {

    @MockK
    private lateinit var settings: Settings

    private lateinit var mockEngine: MockEngine
    private lateinit var httpClient: HttpClient
    private lateinit var repository: JwtTokensRepository
    private lateinit var json: Json

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        // Mock settings for initialization
        every { settings.getStringOrNull("access_token") } returns null
        every { settings.getStringOrNull("refresh_token") } returns null

        json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }

        mockEngine = MockEngine { request ->
            when {
                request.url.encodedPath.endsWith("/users/auth/jwt/token/refresh/") -> {
                    val requestBody = request.body.toByteArray().decodeToString()
                    val refreshRequest = json.decodeFromString<RefreshJwtTokenRequest>(requestBody)

                    if (refreshRequest.refreshToken == "valid_refresh_token") {
                        respond(
                            content = """{"access": "new_access_token"}""",
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        )
                    } else {
                        respond(
                            content = """{"detail": "Invalid refresh token"}""",
                            status = HttpStatusCode.Unauthorized,
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        )
                    }
                }

                else -> {
                    respond(
                        content = """{"error": "Unexpected request"}""",
                        status = HttpStatusCode.NotFound,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }
            }
        }

        httpClient = createMockClient(mockEngine)
        repository = JwtTokensRepositoryImpl(httpClient, settings, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun createMockClient(mockEngine: MockEngine): HttpClient {
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
            install(Resources)

            // Set default headers for all requests
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }

    @Test
    fun `init - when access token is present - should set PRESENT state`() = runTest {
        // Given
        every { settings.getStringOrNull("access_token") } returns "access_token"

        // When
        val repository = JwtTokensRepositoryImpl(httpClient, settings, testDispatcher)
        testScheduler.advanceUntilIdle()

        // Then
        repository.accessTokenState.test {
            assertEquals(AccessTokenState.PRESENT, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init - when access token is missing - should set MISSING state`() = runTest {
        // Given
        every { settings.getStringOrNull("access_token") } returns null

        // When
        val repository = JwtTokensRepositoryImpl(httpClient, settings, testDispatcher)
        testScheduler.advanceUntilIdle()

        // Then
        repository.accessTokenState.test {
            assertEquals(AccessTokenState.MISSING, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRefreshToken - when cached token exists - should return cached token`() = runTest {
        // Given
        val cachedToken = "cached_refresh_token"

        // Set the cached token by calling setRefreshToken
        every { settings.putString("refresh_token", cachedToken) } just Runs
        repository.setRefreshToken(cachedToken)

        // Clear the mock to verify it's not called again
        clearMocks(settings)

        // When
        val result = repository.getRefreshToken()

        // Then
        assertEquals(cachedToken, result)
        verify(exactly = 0) { settings.getStringOrNull("refresh_token") }
    }

    @Test
    fun `getRefreshToken - when no cached token - should get from settings`() = runTest {
        // Given
        val storedToken = "stored_refresh_token"
        every { settings.getStringOrNull("refresh_token") } returns storedToken

        // When
        val result = repository.getRefreshToken()

        // Then
        assertEquals(storedToken, result)
        verify { settings.getStringOrNull("refresh_token") }
    }

    @Test
    fun `getAccessToken - when cached token exists - should return cached token`() = runTest {
        // Given
        val cachedToken = "cached_access_token"

        // Set the cached token by calling setAccessToken
        every { settings.putString("access_token", cachedToken) } just Runs
        repository.setAccessToken(cachedToken)

        // Clear the mock to verify it's not called again
        clearMocks(settings)

        // When
        val result = repository.getAccessToken()

        // Then
        assertEquals(cachedToken, result)
        verify(exactly = 0) { settings.getStringOrNull("access_token") }
    }

    @Test
    fun `getAccessToken - when no cached token - should get from settings`() = runTest {
        // Given
        val storedToken = "stored_access_token"
        every { settings.getStringOrNull("access_token") } returns storedToken

        // When
        val result = repository.getAccessToken()

        // Then
        assertEquals(storedToken, result)
        verify { settings.getStringOrNull("access_token") }
    }

    @Test
    fun `setRefreshToken - should update cache and settings`() = runTest {
        // Given
        val newToken = "new_refresh_token"
        every { settings.putString("refresh_token", newToken) } just Runs

        // When
        repository.setRefreshToken(newToken)

        // Then
        verify { settings.putString("refresh_token", newToken) }

        // Verify cache is updated by getting the token without accessing settings
        clearMocks(settings)
        val cachedToken = repository.getRefreshToken()
        assertEquals(newToken, cachedToken)
        verify(exactly = 0) { settings.getStringOrNull("refresh_token") }
    }

    @Test
    fun `setAccessToken - should update cache, settings and state`() = runTest {
        // Given
        val newToken = "new_access_token"
        every { settings.putString("access_token", newToken) } just Runs

        // When
        repository.accessTokenState.test {
            // Initial state (MISSING because we mocked settings to return null for "access_token" in setUp)
            assertEquals(AccessTokenState.MISSING, awaitItem())

            // Set the token
            repository.setAccessToken(newToken)

            // State should change to PRESENT
            assertEquals(AccessTokenState.PRESENT, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        // Then
        verify { settings.putString("access_token", newToken) }

        // Verify cache is updated by getting the token without accessing settings
        clearMocks(settings)
        val cachedToken = repository.getAccessToken()
        assertEquals(newToken, cachedToken)
        verify(exactly = 0) { settings.getStringOrNull("access_token") }
    }

    @Test
    fun `clearTokens - should clear cache, settings and update state`() = runTest {
        // Given
        // Set up tokens first
        val accessToken = "access_token"
        val refreshToken = "refresh_token"

        every { settings.putString("access_token", accessToken) } just Runs
        every { settings.putString("refresh_token", refreshToken) } just Runs
        repository.setAccessToken(accessToken)
        repository.setRefreshToken(refreshToken)

        every { settings.remove("access_token") } just Runs
        every { settings.remove("refresh_token") } just Runs

        // When
        repository.accessTokenState.test {
            // Initial state should be PRESENT after setting the access token
            assertEquals(AccessTokenState.PRESENT, awaitItem())

            // Clear the tokens
            repository.clearTokens()

            // State should change to MISSING
            assertEquals(AccessTokenState.MISSING, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        // Then
        verify { settings.remove("access_token") }
        verify { settings.remove("refresh_token") }

        // Verify cache is cleared by getting the tokens
        clearMocks(settings)
        every { settings.getStringOrNull("access_token") } returns null
        every { settings.getStringOrNull("refresh_token") } returns null

        assertNull(repository.getAccessToken())
        assertNull(repository.getRefreshToken())
    }

    @Test
    fun `refreshAccessToken - with valid refresh token - should update access token and return true`() = runTest {
        // Given
        every { settings.getStringOrNull("refresh_token") } returns "valid_refresh_token"
        every { settings.putString("access_token", "new_access_token") } just Runs

        // When
        val result = repository.refreshAccessToken()

        // Then
        assertTrue(result)
        verify { settings.putString("access_token", "new_access_token") }

        // Verify state is updated
        repository.accessTokenState.test {
            assertEquals(AccessTokenState.PRESENT, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshAccessToken - with invalid refresh token - should clear tokens and return false`() = runTest {
        // Given
        every { settings.getStringOrNull("refresh_token") } returns "invalid_refresh_token"
        every { settings.remove("access_token") } just Runs
        every { settings.remove("refresh_token") } just Runs

        // When
        val result = repository.refreshAccessToken()

        // Then
        assertFalse(result)
        verify { settings.remove("access_token") }
        verify { settings.remove("refresh_token") }

        // Verify state is updated
        repository.accessTokenState.test {
            assertEquals(AccessTokenState.MISSING, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshAccessToken - with no refresh token - should return false`() = runTest {
        // Given
        every { settings.getStringOrNull("refresh_token") } returns null

        // When
        val result = repository.refreshAccessToken()

        // Then
        assertFalse(result)
        verify(exactly = 0) { settings.putString(any(), any()) }
        verify(exactly = 0) { settings.remove(any()) }
    }
}

package com.toritark.app.data.profile.repository

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.toritark.app.data.profile.api.model.ProfileApiModel
import com.toritark.app.data.profile.model.ProfileState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal interface ProfileRepository {
    val profileState: StateFlow<ProfileState>

    suspend fun setProfile(profile: ProfileApiModel)
    suspend fun deleteProfile()
}

internal class ProfileRepositoryImpl(
    private val settings: Settings,
    private val json: Json,
    private val ioDispatcher: CoroutineDispatcher,
) : ProfileRepository {

    private val logger = Logger.withTag(LOG_TAG)

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Unknown)
    override val profileState = _profileState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        CoroutineScope(ioDispatcher).launch {
            val profile = getProfile()
            _profileState.value = if (profile != null) {
                ProfileState.Present(profile)
            } else {
                ProfileState.Missing
            }
        }
    }

    private suspend fun getProfile(): ProfileApiModel? {
        return withContext(ioDispatcher) {
            val profileJson = settings.getStringOrNull(KEY_PROFILE) ?: return@withContext null

            return@withContext try {
                json.decodeFromString(profileJson)
            } catch (e: SerializationException) {
                logger.w(e) { "Failed to deserialize profile: $e" }
                deleteProfileInternal()

                null
            } catch (e: IllegalArgumentException) {
                logger.w(e) { "Failed to deserialize profile: $e" }
                deleteProfileInternal()

                null
            }
        }
    }

    override suspend fun deleteProfile() {
        deleteProfileInternal()

        _profileState.value = ProfileState.Missing
    }

    private suspend fun deleteProfileInternal() {
        withContext(ioDispatcher) {
            settings.remove(KEY_PROFILE)
        }
    }

    override suspend fun setProfile(profile: ProfileApiModel) {
        withContext(ioDispatcher) {
            _profileState.value = ProfileState.Present(profile)

            val profileJson = json.encodeToString(profile)
            settings.putString(KEY_PROFILE, profileJson)
        }
    }

    private companion object {
        private const val LOG_TAG = "ProfileRepository"

        private const val KEY_PROFILE = "profile"
    }
}
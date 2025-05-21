package com.toritark.app.presentation.profile.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import com.toritark.app.data.billing.api.model.PlanApiModel
import com.toritark.app.data.profile.api.model.ProfileApiModel
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.profile.main.model.ProfileMainScreenState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.toritark_owl

@Composable
internal fun ProfileHeaderSection(
    modifier: Modifier = Modifier,
    profileState: ProfileMainScreenState.ProfileUiState,
    onTripleClick: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        when (profileState) {
            is ProfileMainScreenState.ProfileUiState.Loading -> {}
            is ProfileMainScreenState.ProfileUiState.Present -> {
                val profile = profileState.profile

                // Track click count and last click time for triple-click detection
                var clickCount by remember { mutableIntStateOf(0) }
                var lastClickTime by remember { mutableLongStateOf(0L) }
                val clickTimeoutMs = 500L // Time window for triple-click detection

                // Reset click count after cooldown period
                LaunchedEffect(clickCount) {
                    if (clickCount > 0) {
                        delay(clickTimeoutMs)
                        clickCount = 0
                    }
                }

                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape,
                        )
                        .padding(16.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    val currentTime = Clock.System.now().toEpochMilliseconds()
                                    // Check if click is within the time window
                                    if ((currentTime - lastClickTime) <= clickTimeoutMs) {
                                        clickCount++

                                        // If triple-click detected, trigger callback and reset
                                        if (clickCount == 3) {
                                            onTripleClick()
                                            // Don't reset clickCount here, let the LaunchedEffect handle it
                                            // This creates the "cooldown" effect
                                        }
                                    } else {
                                        // If click is outside time window, reset count
                                        clickCount = 1
                                    }
                                    lastClickTime = currentTime
                                }
                            )
                        },
                    painter = painterResource(Res.drawable.toritark_owl),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${profile.firstName} ${profile.lastName}".trim(),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileHeaderSectionPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            ProfileHeaderSection(
                modifier = Modifier
                    .fillMaxWidth(),
                profileState = ProfileMainScreenState.ProfileUiState.Present(
                    profile = ProfileApiModel(
                        id = 1L,
                        isAnonymous = false,
                        isActive = true,
                        firstName = "John",
                        lastName = "Doe",
                        avatarUrl = null,
                        email = "test@example.com",
                        plan = PlanApiModel(
                            id = 1L,
                            name = "Free",
                            storiesPerDay = 1,
                            retellingsPerDay = 1,
                            audioStoriesPerDay = 1,
                            isFree = true,
                            isDefault = true,
                        ),
                    ),
                ),
            )
        }
    }
}

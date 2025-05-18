package com.toritark.app.presentation.profile.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.app.data.billing.api.model.PlanApiModel
import com.toritark.app.data.profile.api.model.ProfileApiModel
import com.toritark.app.presentation.main.app.AppTheme
import com.toritark.app.presentation.profile.main.model.ProfileMainScreenState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.toritark_owl

@Composable
internal fun ProfileHeaderSection(
    modifier: Modifier = Modifier,
    profileState: ProfileMainScreenState.ProfileUiState,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        when (profileState) {
            is ProfileMainScreenState.ProfileUiState.Loading -> {}
            is ProfileMainScreenState.ProfileUiState.Present -> {
                val profile = profileState.profile

                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape,
                        )
                        .padding(16.dp),
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
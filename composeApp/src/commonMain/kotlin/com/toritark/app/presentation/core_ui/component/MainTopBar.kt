@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.core_ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.toritark.app.data.billing.api.model.PlanApiModel
import com.toritark.app.data.profile.api.model.ProfileApiModel
import com.toritark.app.data.profile.model.ProfileState
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.main.app.theme.LocalExtendedColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_main_screen_topbar
import toritark.composeapp.generated.resources.title_main_screen_topbar_upgrade
import toritark.composeapp.generated.resources.toritark_owl

@Composable
fun MainTopBar(
    modifier: Modifier = Modifier,
    profileState: ProfileState,
    onUpgradeClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    TopAppBar(
        modifier = modifier
            .let {
                if (profileState is ProfileState.Present && profileState.profile.plan.isFree) {
                    it.clickable {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)

                        onUpgradeClick()
                    }
                } else {
                    it
                }
            },
        title = {
            MainTopBarTitle(
                profileState = profileState,
                onUpgradeClick = onUpgradeClick,
            )
        },
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    )
}

@Composable
private fun MainTopBarTitle(
    profileState: ProfileState,
    onUpgradeClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Image(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Bottom),
            painter = painterResource(Res.drawable.toritark_owl),
            contentDescription = stringResource(Res.string.title_main_screen_topbar),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier
                .align(Alignment.Bottom),
            text = stringResource(Res.string.title_main_screen_topbar),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (profileState is ProfileState.Present) {
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .align(Alignment.Top),
                text = profileState.profile.plan.name.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )

            if (profileState.profile.plan.isFree) {
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier
                        .background(
                            color = LocalExtendedColors.current.success.success,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .align(Alignment.Top),
                    text = stringResource(Res.string.title_main_screen_topbar_upgrade),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalExtendedColors.current.success.onSuccess,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MainTopBarUnknownPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 150.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            MainTopBar(
                modifier = Modifier
                    .fillMaxWidth(),
                profileState = ProfileState.Unknown,
                onUpgradeClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun MainTopBarMissingPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 150.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            MainTopBar(
                modifier = Modifier
                    .fillMaxWidth(),
                profileState = ProfileState.Missing,
                onUpgradeClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun MainTopBarPresentFreePreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 150.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            MainTopBar(
                modifier = Modifier
                    .fillMaxWidth(),
                profileState = ProfileState.Present(
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
                onUpgradeClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun MainTopBarPresentPaidPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 150.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            MainTopBar(
                modifier = Modifier
                    .fillMaxWidth(),
                profileState = ProfileState.Present(
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
                            name = "Pro",
                            storiesPerDay = 1,
                            retellingsPerDay = 1,
                            audioStoriesPerDay = 1,
                            isFree = false,
                            isDefault = false,
                        ),
                    ),
                ),
                onUpgradeClick = {},
            )
        }
    }
}
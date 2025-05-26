package com.toritark.app.presentation.profile.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.toritark.app.data.billing.api.model.PlanApiModel
import com.toritark.app.data.language.model.Language
import com.toritark.app.data.language.model.LanguageLevel
import com.toritark.app.data.profile.api.model.ProfileApiModel
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.language.setup.level.model.LanguageLevelUiModel
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.main.app.theme.LocalExtendedColors
import com.toritark.app.presentation.profile.main.component.ContactUsSection
import com.toritark.app.presentation.profile.main.component.ManageSubscriptionsSection
import com.toritark.app.presentation.profile.main.component.ProfileHeaderSection
import com.toritark.app.presentation.profile.main.component.ProfileSettingsSection
import com.toritark.app.presentation.profile.main.model.ProfileMainScreenState
import com.toritark.app.presentation.profile.main.model.ProfileMainScreenState.ProfileUiState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.desc_language_level_a2
import toritark.composeapp.generated.resources.title_language_level_a2
import toritark.composeapp.generated.resources.title_profile_upgrade_subscription_btn

@Composable
internal fun ProfileMainScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: ProfileMainViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    BaseScreen(
        viewModel = viewModel,
    ) { contentValue ->
        ProfileScreenContent(
            modifier = Modifier.fillMaxSize(),
            screenState = contentValue,
            onChooseLearningLanguageClick = viewModel::onChooseLearningLanguageClick,
            onChooseLanguageLevelClick = viewModel::onChooseLanguageLevelClick,
            onChooseNativeLanguageClick = viewModel::onChooseNativeLanguageClick,
            onProfileImageTripleClick = viewModel::onProfileImageTripleClick,
            onUpgradeSubscriptionClick = viewModel::onUpgradeSubscriptionClick,
        )
    }
}

@Composable
private fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    screenState: ProfileMainScreenState,
    onChooseLearningLanguageClick: () -> Unit,
    onChooseLanguageLevelClick: () -> Unit,
    onChooseNativeLanguageClick: () -> Unit,
    onProfileImageTripleClick: () -> Unit,
    onUpgradeSubscriptionClick: () -> Unit,
) {
    val hasActiveSubscription =
        screenState.profileState is ProfileUiState.Present && screenState.profileState.profile.plan.isFree.not()

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ProfileHeaderSection(
            modifier = Modifier.fillMaxWidth(),
            profileState = screenState.profileState,
            onTripleClick = onProfileImageTripleClick
        )

        if (!hasActiveSubscription) {
            Spacer(modifier = Modifier.height(16.dp))

            val upgradeSubscriptionShape = RoundedCornerShape(24.dp)

            Text(
                modifier = Modifier
                    .background(
                        color = LocalExtendedColors.current.success.success,
                        shape = upgradeSubscriptionShape
                    )
                    .clip(upgradeSubscriptionShape)
                    .clickable(onClick = onUpgradeSubscriptionClick)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.title_profile_upgrade_subscription_btn),
                style = MaterialTheme.typography.labelMedium,
                color = LocalExtendedColors.current.success.onSuccess,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileSettingsSection(
            modifier = Modifier.fillMaxWidth(),
            languageSettingsState = screenState.languageSettingsState,
            onChooseLearningLanguageClick = onChooseLearningLanguageClick,
            onChooseLanguageLevelClick = onChooseLanguageLevelClick,
            onChooseNativeLanguageClick = onChooseNativeLanguageClick,
        )

        Spacer(modifier = Modifier.height(24.dp))

        ContactUsSection(
            modifier = Modifier.fillMaxWidth(),
        )

        if (hasActiveSubscription) {
            Spacer(modifier = Modifier.height(16.dp))

            ManageSubscriptionsSection(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Preview
@Composable
private fun ProfileScreenContentPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 800.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            ProfileScreenContent(
                modifier = Modifier
                    .fillMaxWidth(),
                screenState = ProfileMainScreenState(
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
                    languageSettingsState = ProfileMainScreenState.LanguageSettingsState.Present(
                        learningLanguage = Language(
                            isoCode = "en",
                            nameEn = "English",
                            name = "English",
                            flagUnicode = "\uD83C\uDDEC\uD83C\uDDE7",
                        ),
                        languageLevel = LanguageLevelUiModel(
                            languageLevel = LanguageLevel.A2,
                            titleStringResource = Res.string.title_language_level_a2,
                            descriptionStringResource = Res.string.desc_language_level_a2,
                        ),
                        nativeLanguage = Language(
                            isoCode = "et",
                            nameEn = "Estonian",
                            name = "Eesti keel",
                            flagUnicode = "\uD83C\uDDEA\uD83C\uDDEA",
                        ),
                    ),
                ),
                onChooseLearningLanguageClick = {},
                onChooseLanguageLevelClick = {},
                onChooseNativeLanguageClick = {},
                onProfileImageTripleClick = {},
                onUpgradeSubscriptionClick = {},
            )
        }
    }
}

package com.toritark.stories.presentation.language.setup.learning

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.core_ui.screen.BaseScreen
import com.toritark.stories.presentation.language.base.LanguageChooser
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_choose_learning_language_next_btn
import toritark.composeapp.generated.resources.title_choose_learning_language_screen

@Composable
internal fun LearningLanguageChooserScreen(
    onNavigate: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewmodel: LearningLanguageChooserViewModel = koinViewModel(),
) {
    viewmodel.onNavigate = onNavigate
    viewmodel.onPopBackStack = onPopBackStack

    BaseScreen(viewmodel) {
        val languages by viewmodel.languages.collectAsState()
        val isNextButtonEnabled by viewmodel.isNextButtonEnabled.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Text(
                text = stringResource(Res.string.title_choose_learning_language_screen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LanguageChooser(
                    modifier = Modifier.fillMaxSize(),
                    languages = languages,
                    selectedLanguage = null,
                    onSelectLanguage = viewmodel::onLanguageSelected,
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = viewmodel::onSaveLanguageClick,
                enabled = isNextButtonEnabled,
            ) {
                Text(
                    text = stringResource(Res.string.title_choose_learning_language_next_btn),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.size(width = 0.dp, height = 16.dp))
        }
    }
}

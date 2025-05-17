package com.toritark.app.presentation.language.setup.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.language.setup.base.model.BaseLanguageSetupScreenContent
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun <C : BaseLanguageSetupScreenContent> BaseLanguageSetupScreen(
    titleStringResource: StringResource,
    nextButtonStringResource: StringResource,
    viewModel: BaseLanguageSetupViewModel<C>,
    content: @Composable (contentValue: C) -> Unit,
) {
    BaseScreen(viewModel) { contentValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Text(
                text = stringResource(titleStringResource),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                content(contentValue)
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = viewModel::onNextButtonClick,
                enabled = contentValue.isNextButtonEnabled,
            ) {
                Text(
                    text = stringResource(nextButtonStringResource),
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
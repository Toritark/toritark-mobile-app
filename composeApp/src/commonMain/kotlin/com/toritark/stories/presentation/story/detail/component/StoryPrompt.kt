package com.toritark.stories.presentation.story.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.stories.presentation.core_ui.animation.FadeAndExpandVerticallyAnimation
import org.jetbrains.compose.resources.stringResource
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.hint_story_customize_prompt

@Composable
internal fun StoryPrompt(
    modifier: Modifier = Modifier,
    prompt: String,
    isExpanded: Boolean = false,
    onPromptChange: (String) -> Unit = {},
) {
    FadeAndExpandVerticallyAnimation(
        visible = isExpanded,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            OutlinedTextField(
                value = prompt,
                onValueChange = onPromptChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.hint_story_customize_prompt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                minLines = 3,
                shape = MaterialTheme.shapes.small
            )
        }
    }
}

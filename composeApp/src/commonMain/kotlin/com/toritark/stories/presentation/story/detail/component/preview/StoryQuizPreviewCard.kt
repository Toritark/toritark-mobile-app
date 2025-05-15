package com.toritark.stories.presentation.story.detail.component.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.story.model.story.story.StoryApiModel
import com.toritark.stories.data.story.model.story.story.StoryQuestionApiModel
import com.toritark.stories.presentation.core_ui.animation.FadeAndExpandVerticallyAnimation
import com.toritark.stories.presentation.core_ui.component.FlatCard
import com.toritark.stories.presentation.core_ui.icon.AppIcons
import com.toritark.stories.presentation.core_ui.icon.Quiz
import com.toritark.stories.presentation.main.app.AppTheme
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.desc_story_quiz
import toritark.composeapp.generated.resources.title_story_quiz

@Composable
internal fun StoryQuizPreviewCard(
    modifier: Modifier = Modifier,
    story: StoryApiModel,
    onClick: () -> Unit,
) {
    FadeAndExpandVerticallyAnimation {
        FlatCard(
            modifier = modifier,
            backgroundColor = MaterialTheme.colorScheme.background,
            cornerRadius = 24.dp,
            borderWidth = 1.dp,
            borderColor = MaterialTheme.colorScheme.secondary,
            rightIcon = Icons.Default.ChevronRight,
            rightIconTint = MaterialTheme.colorScheme.onBackground,
            onClick = onClick,
        ) {
            CardBody(
                story = story,
            )
        }
    }
}

@Composable
private fun RowScope.CardBody(
    story: StoryApiModel,
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .weight(1f)
    ) {
        CardHeader()

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = pluralStringResource(
                Res.plurals.desc_story_quiz,
                story.questions.size,
                story.questions.size,
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun CardHeader() {
    Row {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(24.dp),
            imageVector = AppIcons.Quiz,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = stringResource(Res.string.title_story_quiz),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview
@Composable
private fun StoryQuizPreviewCardPreview() {
    val story = StoryApiModel(
        learningLanguageText = emptyList(),
        nativeLanguageText = emptyList(),
        questions = listOf(
            StoryQuestionApiModel(
                question = "",
                answers = emptyList(),
            ),
            StoryQuestionApiModel(
                question = "",
                answers = emptyList(),
            ),
            StoryQuestionApiModel(
                question = "",
                answers = emptyList(),
            ),
            StoryQuestionApiModel(
                question = "",
                answers = emptyList(),
            ),
        ),
    )

    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 600.dp, height = 400.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            StoryQuizPreviewCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                story = story,
                onClick = {},
            )
        }
    }
}
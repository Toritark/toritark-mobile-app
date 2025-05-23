package com.toritark.app.presentation.story.detail.component.preview

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.presentation.core_ui.animation.FadeAndExpandVerticallyAnimation
import com.toritark.app.presentation.core_ui.component.FlatCard
import com.toritark.app.presentation.core_ui.icon.AppIcons
import com.toritark.app.presentation.core_ui.icon.Story
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_preview_read_your_story

@Composable
internal fun StoryPreviewCard(
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
            borderColor = MaterialTheme.colorScheme.onBackground,
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
            text = story.learningLanguageText.joinToString("\n"),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
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
            imageVector = AppIcons.Story,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = stringResource(Res.string.title_story_preview_read_your_story),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview
@Composable
private fun StoryPreviewCardPreview() {
    val story = StoryApiModel(
        learningLanguageText = listOf(
            "Hello, my name is Toritark.",
            "I am a developer, writing code in Kotlin and Python.",
            "I like to code.",
            "I am coding in Kotlin.",
            "I am learning Jetpack Compose. This is a long, multi-line sentence",
            "One more sentence",
        ),
        nativeLanguageText = emptyList(),
        questions = emptyList(),
    )

    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 600.dp, height = 400.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            StoryPreviewCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                story = story,
                onClick = {},
            )
        }
    }
}
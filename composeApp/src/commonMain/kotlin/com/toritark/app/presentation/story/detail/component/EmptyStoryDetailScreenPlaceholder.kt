package com.toritark.app.presentation.story.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_detail_empty_hint

@Composable
internal fun EmptyStoryDetailScreenPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        GenerateStoryArrowHint(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            strokeWidth = 1.5.dp,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(Res.string.title_story_detail_empty_hint),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun EmptyStoryDetailScreenPlaceholderPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            EmptyStoryDetailScreenPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp),
            )
        }
    }
}
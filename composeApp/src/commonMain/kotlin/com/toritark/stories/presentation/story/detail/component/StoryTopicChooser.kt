package com.toritark.stories.presentation.story.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.toritark.stories.presentation.story.model.StoryTopicUiModel
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun StoryTopicChooser(
    modifier: Modifier = Modifier,
    topics: List<StoryTopicUiModel>,
    selectedTopic: StoryTopicUiModel?,
    onTopicSelected: (StoryTopicUiModel) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val topic = selectedTopic ?: topics.firstOrNull()
    val topicText = topic?.let { stringResource(it.nameResource) } ?: ""

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                )
                .clip(MaterialTheme.shapes.small)
                .clickable { expanded = true }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topicText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            topics.forEach { topic ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(topic.nameResource),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onTopicSelected(topic)
                        expanded = false
                    }
                )
            }
        }
    }
}

package com.toritark.stories.presentation.story.detail.component.generate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.story.model.topic.StoryTopic
import com.toritark.stories.presentation.core_ui.icon.AppIcons
import com.toritark.stories.presentation.core_ui.icon.Magic
import com.toritark.stories.presentation.main.app.AppTheme
import com.toritark.stories.presentation.story.model.StoryTopicUiModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.*

@Composable
internal fun GenerateStoryHeader(
    modifier: Modifier = Modifier,
    topics: List<StoryTopicUiModel>,
    selectedTopic: StoryTopicUiModel?,
    isPromptVisible: Boolean,
    isGenerateButtonEnabled: Boolean,
    onTopicSelected: (StoryTopicUiModel) -> Unit,
    onCustomizeClick: () -> Unit,
    onGenerateClick: () -> Unit,
) {

    val hapticFeedback = LocalHapticFeedback.current

    Row(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        // Dropdown
        StoryTopicDropdown(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            topics = topics,
            selectedTopic = selectedTopic,
            onTopicSelected = onTopicSelected,
        )

        // Customize topic prompt
        IconButton(
            modifier = Modifier.fillMaxHeight(),
            onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                onCustomizeClick()
            }
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        color = if (isPromptVisible) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                        shape = CircleShape,
                    )
                    .padding(6.dp),
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = if (isPromptVisible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }

        // Generate button
        Button(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxHeight(),
            enabled = isGenerateButtonEnabled,
            onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                onGenerateClick()
            }
        ) {
            Text(
                text = stringResource(Res.string.title_story_generate_btn),
            )

            Icon(
                imageVector = AppIcons.Magic,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(16.dp)
            )
        }
    }
}

@Composable
private fun StoryTopicDropdown(
    modifier: Modifier = Modifier,
    topics: List<StoryTopicUiModel>,
    selectedTopic: StoryTopicUiModel?,
    onTopicSelected: (StoryTopicUiModel) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val topic = selectedTopic ?: topics.firstOrNull()
    val topicText = topic?.let { stringResource(it.nameResource) } ?: ""

    val hapticFeedback = LocalHapticFeedback.current

    Box(modifier = modifier) {
        AssistChip(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            onClick = { expanded = true },
            colors = AssistChipDefaults.assistChipColors(
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            label = {
                Text(
                    text = topicText,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            containerColor = MaterialTheme.colorScheme.surface,
            shadowElevation = 0.dp,
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.secondary),
            onDismissRequest = { expanded = false },
            modifier = Modifier
        ) {
            topics.forEach { topic ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(topic.nameResource),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                        onTopicSelected(topic)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun StoryTopicDropdownPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 200.dp, height = 200.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            StoryTopicDropdown(
                modifier = Modifier.height(100.dp),
                topics = previewTopics,
                selectedTopic = previewTopics.firstOrNull(),
                onTopicSelected = {},
            )
        }
    }
}

@Preview
@Composable
private fun GenerateStoryHeaderPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 200.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            GenerateStoryHeader(
                topics = previewTopics,
                selectedTopic = previewTopics.firstOrNull(),
                isPromptVisible = false,
                isGenerateButtonEnabled = true,
                onTopicSelected = {},
                onCustomizeClick = {},
                onGenerateClick = {},
            )
        }
    }
}

private val previewTopics by lazy {
    listOf(
        StoryTopicUiModel(
            storyTopic = StoryTopic.Family,
            nameResource = Res.string.title_story_topic_daily_routine,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopic.Family,
            nameResource = Res.string.title_story_topic_store_dialog,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopic.Family,
            nameResource = Res.string.title_story_topic_meeting_new_friend,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopic.Family,
            nameResource = Res.string.title_story_topic_family,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopic.Family,
            nameResource = Res.string.title_story_topic_my_room,
        ),
    )
}

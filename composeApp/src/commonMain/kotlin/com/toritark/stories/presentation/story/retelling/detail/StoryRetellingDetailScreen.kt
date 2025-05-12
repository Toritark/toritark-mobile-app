@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.stories.presentation.story.retelling.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toritark.stories.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import com.toritark.stories.data.story.model.retelling.retelling.StoryRetellingScoresApiModel
import com.toritark.stories.data.story.model.retelling.retelling.StoryRetellingSentenceReviewApiModel
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.core_ui.screen.BaseScreen
import com.toritark.stories.presentation.story.retelling.component.StoryRetellingReviewSummary
import com.toritark.stories.presentation.story.retelling.detail.model.StoryRetellingDetailScreenContent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_text_screen

@Composable
internal fun StoryRetellingDetailScreen(
    review: StoryRetellingReviewApiModel,
    viewModel: StoryRetellingDetailViewModel = koinViewModel(),
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
) {
    viewModel.setReview(review)

    viewModel.onPopBackStack = onPopBackStack
    viewModel.onNavigateTo = onNavigateTo

    BaseScreen(viewModel) { contentValue ->
        StoryRetellingDetailScreenContent(
            content = contentValue,
            onCloseClick = viewModel::onCloseClick,
        )
    }
}

@Composable
private fun StoryRetellingDetailScreenContent(
    content: StoryRetellingDetailScreenContent,
    onCloseClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                title = {
                    Text(stringResource(Res.string.title_story_text_screen))
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            )
        },
    ) { innerPadding ->
        val review = content.review ?: return@Scaffold

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            StoryRetellingReviewSummary(
                modifier = Modifier.fillMaxWidth(),
                review = review,
                showDetailsButton = false,
                onDetailsClick = {},
            )

            Spacer(modifier = Modifier.height(16.dp))

            review.sentences.forEach { sentence ->
                RetellingSentenceReview(
                    modifier = Modifier.fillMaxWidth(),
                    review = sentence,
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun RetellingSentenceReview(
    modifier: Modifier = Modifier,
    review: StoryRetellingSentenceReviewApiModel,
) {
    val backgroundColor = when (review.status) {
        StoryRetellingSentenceReviewApiModel.Status.CORRECT -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)

        StoryRetellingSentenceReviewApiModel.Status.INSIGNIFICANT_MISTAKES -> MaterialTheme.colorScheme.errorContainer.copy(
            alpha = 0.5f
        )

        StoryRetellingSentenceReviewApiModel.Status.SIGNIFICANT_MISTAKES -> MaterialTheme.colorScheme.errorContainer
    }

    val textColor = when (review.status) {
        StoryRetellingSentenceReviewApiModel.Status.CORRECT -> MaterialTheme.colorScheme.onSecondaryContainer
        StoryRetellingSentenceReviewApiModel.Status.INSIGNIFICANT_MISTAKES -> MaterialTheme.colorScheme.onErrorContainer
        StoryRetellingSentenceReviewApiModel.Status.SIGNIFICANT_MISTAKES -> MaterialTheme.colorScheme.onErrorContainer
    }

    val icon = when (review.status) {
        StoryRetellingSentenceReviewApiModel.Status.CORRECT -> Icons.Default.CheckCircle
        StoryRetellingSentenceReviewApiModel.Status.INSIGNIFICANT_MISTAKES -> Icons.Default.Error
        StoryRetellingSentenceReviewApiModel.Status.SIGNIFICANT_MISTAKES -> Icons.Default.Error
    }

    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .clip(shape)
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = review.sentence,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier
                    .size(16.dp),
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
            )
        }


        if (review.review != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                text = review.review,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                ),
                fontWeight = FontWeight.Medium,
                color = textColor,
            )
        }
    }

}

@Preview
@Composable
private fun StoryRetellingDetailScreenContentPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 800.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            StoryRetellingDetailScreenContent(
                content = StoryRetellingDetailScreenContent(
                    review = StoryRetellingReviewApiModel(
                        overallReview = "This is a very good retelling. I like it very much.\nSome mistakes were made, but they are not significant.",
                        scores = StoryRetellingScoresApiModel(
                            overall = 90,
                            completeness = 80,
                            grammar = 50,
                            vocabulary = 30,
                            spelling = 20,
                            punctuation = 0,
                        ),
                        sentences = listOf(
                            StoryRetellingSentenceReviewApiModel(
                                sentence = "This is a very good retelling. I like it very much.",
                                status = StoryRetellingSentenceReviewApiModel.Status.CORRECT,
                                review = null,
                            ),
                            StoryRetellingSentenceReviewApiModel(
                                sentence = "Hello, world! This is a very good retelling. I like it very much.",
                                status = StoryRetellingSentenceReviewApiModel.Status.INSIGNIFICANT_MISTAKES,
                                review = "Factual errors. The sentence is not correct.",
                            ),
                            StoryRetellingSentenceReviewApiModel(
                                sentence = "This is a very good retelling. I like it very much.",
                                status = StoryRetellingSentenceReviewApiModel.Status.SIGNIFICANT_MISTAKES,
                                review = "This is totally incorrect! Full sentence is wrong.\nMultiline text here",
                            )
                        ),
                    ),
                ),
                onCloseClick = {},
            )
        }
    }
}

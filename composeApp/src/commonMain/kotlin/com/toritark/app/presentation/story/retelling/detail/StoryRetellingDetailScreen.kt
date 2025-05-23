@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.story.retelling.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.toritark.app.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import com.toritark.app.data.story.model.retelling.retelling.StoryRetellingScoresApiModel
import com.toritark.app.data.story.model.retelling.retelling.StoryRetellingSentenceReviewApiModel
import com.toritark.app.presentation.core_ui.component.FlatCard
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.core_ui.text.htmlToAnnotatedString
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.main.app.theme.LocalExtendedColors
import com.toritark.app.presentation.story.retelling.component.StoryRetellingReviewSummary
import com.toritark.app.presentation.story.retelling.detail.model.StoryRetellingDetailScreenState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_retelling_review_detail_screen

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
    content: StoryRetellingDetailScreenState,
    onCloseClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(stringResource(Res.string.title_retelling_review_detail_screen))
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
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            FlatCard(
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.background,
                cornerRadius = 24.dp,
                borderWidth = 1.dp,
                borderColor = MaterialTheme.colorScheme.secondary,
            ) {
                StoryRetellingReviewSummary(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    review = review,
                    showDetailsButton = false,
                    onDetailsClick = {},
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            review.sentences.forEachIndexed { index, sentence ->
                RetellingSentenceReview(
                    modifier = Modifier.fillMaxWidth(),
                    review = sentence,
                    isFirst = index == 0,
                    isLast = index == review.sentences.lastIndex,
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
    isFirst: Boolean,
    isLast: Boolean,
) {
    val textColor = when (review.status) {
        StoryRetellingSentenceReviewApiModel.Status.CORRECT -> LocalExtendedColors.current.success.success
        StoryRetellingSentenceReviewApiModel.Status.WRONG -> MaterialTheme.colorScheme.error
    }

    val icon = when (review.status) {
        StoryRetellingSentenceReviewApiModel.Status.CORRECT -> Icons.Default.Check
        StoryRetellingSentenceReviewApiModel.Status.WRONG -> Icons.Default.Close
    }

    val shape = when {
        isFirst && isLast -> RoundedCornerShape(
            size = 24.dp,
        )

        isFirst -> RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
        )

        isLast -> RoundedCornerShape(
            bottomStart = 24.dp,
            bottomEnd = 24.dp,
        )

        else -> RoundedCornerShape(size = 4.dp)
    }

    Column(
        modifier = modifier
            .clip(shape)
            .background(color = MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = shape
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        StoryRetellingSentenceReviewText(
            text = review.sentence,
            icon = icon,
            color = textColor,
        )

        if (review.correctedSentence != null) {
            Spacer(modifier = Modifier.height(8.dp))

            StoryRetellingSentenceReviewText(
                text = review.correctedSentence,
                icon = Icons.Default.Check,
                color = LocalExtendedColors.current.success.success,
            )
        }

        if (review.explanation != null) {
            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            StoryRetellingSentenceReviewText(
                text = review.explanation,
                icon = Icons.Default.Info,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun StoryRetellingSentenceReviewText(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    icon: ImageVector,
    color: Color,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .size(16.dp),
            imageVector = icon,
            contentDescription = null,
            tint = color,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier
                .weight(1f),
            text = htmlToAnnotatedString(text),
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )
    }
}

@Preview
@Composable
private fun StoryRetellingDetailScreenContentPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 800.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            StoryRetellingDetailScreenContent(
                content = StoryRetellingDetailScreenState(
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
                                status = StoryRetellingSentenceReviewApiModel.Status.CORRECT,
                                sentence = "This is a very good retelling. I like it very much.",
                                correctedSentence = null,
                                explanation = null,
                            ),
                            StoryRetellingSentenceReviewApiModel(
                                status = StoryRetellingSentenceReviewApiModel.Status.WRONG,
                                sentence = "Hello, world! This is a <s>very good</s> retelling. I <s>likes</s> it very much.",
                                correctedSentence = "Hello, world! This is a <u>quite good</u> retelling. I <u>like</u> it very much.",
                                explanation = "Factual errors. The sentence is not correct.",
                            ),
                            StoryRetellingSentenceReviewApiModel(
                                status = StoryRetellingSentenceReviewApiModel.Status.WRONG,
                                sentence = "This is a very good retelling. I like <s>this</s> very much.",
                                correctedSentence = "This is a very good retelling. I like <u>it</u> very much.",
                                explanation = "This is totally incorrect! Full sentence is wrong.\nMultiline text here",
                            )
                        ),
                    ),
                ),
                onCloseClick = {},
            )
        }
    }
}

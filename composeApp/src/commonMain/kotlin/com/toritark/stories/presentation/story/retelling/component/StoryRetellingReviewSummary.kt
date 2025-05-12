package com.toritark.stories.presentation.story.retelling.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import com.toritark.stories.data.story.model.retelling.retelling.StoryRetellingScoresApiModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.*

@Composable
internal fun StoryRetellingReviewSummary(
    modifier: Modifier = Modifier,
    review: StoryRetellingReviewApiModel,
    showDetailsButton: Boolean = true,
    onDetailsClick: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        RetellingReviewSummaryHeader(
            modifier = Modifier
                .fillMaxWidth(),
            review = review,
        )

        Spacer(modifier = Modifier.height(8.dp))

        RetellingReviewScores(
            review = review,
        )

        Spacer(modifier = Modifier.height(16.dp))

        val hapticFeedback = LocalHapticFeedback.current

        if (showDetailsButton) {
            TextButton(
                modifier = Modifier
                    .align(Alignment.End),
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                    onDetailsClick()
                },
            ) {
                Text(
                    text = stringResource(Res.string.title_retelling_review_summary_more_details_btn),
                )
            }
        }
    }
}

@Composable
private fun RetellingReviewSummaryHeader(
    modifier: Modifier = Modifier,
    review: StoryRetellingReviewApiModel,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(
                resource = Res.string.title_retelling_review_summary_header,
                review.scores.overall,
            ),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = review.overallReview,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun RetellingReviewScores(
    modifier: Modifier = Modifier.fillMaxWidth(),
    review: StoryRetellingReviewApiModel,
) {
    Column(
        modifier = modifier,
    ) {
        RetellingReviewScore(
            score = review.scores.completeness,
            scoreNameResource = Res.string.title_retelling_review_score_completeness,
        )
        Spacer(modifier = Modifier.height(8.dp))
        RetellingReviewScore(
            score = review.scores.grammar,
            scoreNameResource = Res.string.title_retelling_review_score_grammar,
        )
        Spacer(modifier = Modifier.height(8.dp))
        RetellingReviewScore(
            score = review.scores.vocabulary,
            scoreNameResource = Res.string.title_retelling_review_score_vocabulary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        RetellingReviewScore(
            score = review.scores.spelling,
            scoreNameResource = Res.string.title_retelling_review_score_spelling,
        )
        Spacer(modifier = Modifier.height(8.dp))
        RetellingReviewScore(
            score = review.scores.punctuation,
            scoreNameResource = Res.string.title_retelling_review_score_punctuation,
        )
    }
}

@Composable
private fun RetellingReviewScore(
    modifier: Modifier = Modifier.fillMaxWidth(),
    score: Int,
    scoreNameResource: StringResource,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(scoreNameResource),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = score.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { score.toFloat() / 100f },
            modifier = Modifier.fillMaxWidth(),
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
    }
}

@Preview
@Composable
private fun StoryRetellingReviewSummaryPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            StoryRetellingReviewSummary(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
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
                    sentences = emptyList(),
                ),
                onDetailsClick = {}
            )
        }
    }
}
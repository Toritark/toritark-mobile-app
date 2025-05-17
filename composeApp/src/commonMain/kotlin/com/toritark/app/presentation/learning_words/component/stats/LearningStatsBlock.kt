package com.toritark.app.presentation.learning_words.component.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.toritark.app.data.learning_words.data.model.LearningStats
import com.toritark.app.presentation.main.app.AppTheme
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_learning_words_stats_learned
import toritark.composeapp.generated.resources.title_learning_words_stats_to_learn

@Composable
internal fun LearningStatsBlock(
    modifier: Modifier,
    learningStats: LearningStats,
) {

    Column(
        modifier = modifier,
    ) {

        LearningStatsItem(
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Filled.CheckCircle,
            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
            textResource = Res.plurals.title_learning_words_stats_learned,
            value = learningStats.words.learned,
        )

        Spacer(modifier = Modifier.height(8.dp))

        LearningStatsItem(
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Filled.MoreTime,
            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
            textResource = Res.plurals.title_learning_words_stats_to_learn,
            value = learningStats.words.toLearn,
        )
    }
}

@Composable
private fun LearningStatsItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: Color,
    textResource: PluralStringResource,
    value: Long,
) {
    Row(
        modifier = modifier,
    ) {
        val text = pluralStringResource(textResource, value.toInt(), value)

        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            contentDescription = text,
            tint = iconTint,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}


@Preview
@Composable
private fun LearningStatsBlock() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 600.dp, height = 300.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            LearningStatsBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                learningStats = LearningStats(
                    words = LearningStats.Words(
                        total = 110,
                        learned = 50,
                        toLearn = 60,
                    )
                )
            )
        }
    }
}
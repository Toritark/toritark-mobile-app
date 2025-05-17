package com.toritark.stories.presentation.profile.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.stories.presentation.main.app.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ProfileHeaderSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        // TODO: Add image

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "John TODO Doe",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Preview
@Composable
private fun ProfileHeaderSectionPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            ProfileHeaderSection(
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}
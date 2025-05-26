package com.toritark.app.presentation.profile.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.toritark.app.presentation.core_ui.component.FlatCard
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.desc_profile_contact_us
import toritark.composeapp.generated.resources.title_profile_contact_us

@Composable
internal fun ContactUsSection(
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val uriHandler = LocalUriHandler.current

    FlatCard(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.background,
        borderColor = MaterialTheme.colorScheme.secondary,
        borderWidth = 1.dp,
        cornerRadius = 24.dp,
        rightIcon = Icons.Default.ChevronRight,
        rightIconTint = MaterialTheme.colorScheme.onBackground,
        padding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
            uriHandler.openUri("mailto:hi@toritark.com?subject=App Feedback")
        },
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp),
                    imageVector = Icons.Default.Mail,
                    contentDescription = stringResource(Res.string.title_profile_contact_us),
                    tint = MaterialTheme.colorScheme.onBackground,
                )

                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.title_profile_contact_us),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier,
                text = stringResource(Res.string.desc_profile_contact_us),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview
@Composable
private fun ContactUsSectionPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            ContactUsSection(
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}
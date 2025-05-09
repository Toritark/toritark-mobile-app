package com.toritark.stories.presentation.core_ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val ERROR_MESSAGE_DELAY_MS = 1000L * 5

@Composable
fun <T : BaseViewModel> BaseScreen(
    viewModel: T,
    content: @Composable () -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.errorMessage.collect { message ->
            snackbarMessage = message
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = screenState) {
            ScreenState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            ScreenState.Content -> {
                content()
            }

            is ScreenState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetryClick = viewModel::onRetryClick
                )
            }
        }

        snackbarMessage?.let { message ->
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(text = message)
            }
            LaunchedEffect(message) {
                delay(ERROR_MESSAGE_DELAY_MS)
                snackbarMessage = null
            }
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetryClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = message,
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = onRetryClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("Retry")
        }
    }
}
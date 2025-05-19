package com.toritark.app.presentation.core_ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val ERROR_MESSAGE_DELAY_MS = 1000L * 5
private const val MESSAGE_DELAY_MS = 1000L * 3

@Composable
fun <C, T : BaseViewModel<C>> BaseScreen(
    viewModel: T,
    content: @Composable (screenState: C) -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()
    var snackbarErrorMessage by remember { mutableStateOf<String?>(null) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.errorMessage.collect { message ->
            snackbarErrorMessage = message
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.snackBarMessage.collect { message ->
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

            is ScreenState.Content<*> -> {
                @Suppress("UNCHECKED_CAST")
                content(state.value as C)
            }

            is ScreenState.Error -> {
                ErrorScreen(
                    message = state.getMessage(),
                    onRetryClick = viewModel::onRetryClick
                )
            }
        }

        snackbarErrorMessage?.let { message ->
            MessageSnackbar(
                message = message,
                dismissDelay = ERROR_MESSAGE_DELAY_MS,
                onDismissWithDelay = { snackbarErrorMessage = null },
            )
        }

        snackbarMessage?.let { message ->
            MessageSnackbar(
                message = message,
                dismissDelay = MESSAGE_DELAY_MS,
                onDismissWithDelay = { snackbarMessage = null },
            )
        }
    }
}

@Composable
private fun BoxScope.MessageSnackbar(
    message: String,
    dismissDelay: Long = 0L,
    onDismissWithDelay: () -> Unit = {},
) {
    Snackbar(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .systemBarsPadding(),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    if (dismissDelay > 0) {
        LaunchedEffect(message) {
            delay(dismissDelay)
            onDismissWithDelay()
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
package com.toritark.stories.presentation.core_ui.screen

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.StringResource
import org.koin.core.component.KoinComponent

abstract class BaseViewModel<C>(
    protected val defaultDispatcher: CoroutineDispatcher,
    protected val ioDispatcher: CoroutineDispatcher,
    protected val mainDispatcher: CoroutineDispatcher,
    defaultContentValue: C,
) : ViewModel(), KoinComponent {

    protected abstract val logger: Logger

    protected var contentValue: C = defaultContentValue

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Content(defaultContentValue))
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    var onNavigateTo: OnNavigateTo = { _, _ -> }
    var onPopBackStack: OnPopBackStack = {}

    protected fun setScreenState(state: ScreenState) {
        _screenState.value = state
    }

    protected fun setLoadingScreenState() {
        setScreenState(ScreenState.Loading)
    }

    protected fun setContentScreenState() {
        setScreenState(ScreenState.Content(contentValue))
    }

    protected fun updateAndShowContent(block: C.() -> C) {
        contentValue = contentValue.block()
        setContentScreenState()
    }

    protected fun setErrorScreenState(message: String) {
        setScreenState(ScreenState.Error.Text(message = message))
    }

    protected fun setErrorScreenState(resource: StringResource) {
        setScreenState(ScreenState.Error.Resource(resource = resource))
    }

    protected fun setErrorScreenState(throwable: Throwable) {
        setErrorScreenState(throwable.message ?: throwable.toString())
    }

    protected fun <T> Flow<T>.onErrorShowMessage(
        getMessage: (throwable: Throwable) -> String = { throwable ->
            throwable.message ?: throwable.toString()
        },
    ): Flow<T> {
        return this.catch { throwable ->
            val message = getMessage(throwable)

            Logger.e(throwable = throwable) { message }

            withContext(mainDispatcher) {
                showErrorMessage(message = message)
                setContentScreenState()
            }
        }
    }

    suspend fun showErrorMessage(message: String) {
        _errorMessage.emit(message)
    }

    open fun onRetryClick() {
    }
}
package com.hung.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<ViewState : PresentationState>(
    initialState: ViewState
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _events = Channel<PresentationEvent>(BUFFERED)
    val events = _events.receiveAsFlow()

    protected fun updateState(updatedState: (lastState: ViewState) -> ViewState) {
        _state.update { viewState ->
            updatedState(viewState)
        }
    }

    protected fun sendEvent(event: PresentationEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    protected fun launchUseCase(
        onError: (Throwable?) -> Unit = { sendEvent(DefaultErrorEvent(it?.message)) },
        useCaseBlock: suspend () -> Unit,
    ) = viewModelScope.launch(
        CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                onError(throwable)
            }
        }
    ) {
        useCaseBlock()
    }

    open fun onEnter() {}
    open fun onExit() {}
}

package com.hung.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hung.core.presentation.BaseViewModel
import com.hung.core.presentation.PresentationState

@Composable
inline fun <State : PresentationState, reified ViewModel : BaseViewModel<State>> BaseScreen(
    eventHandler: EventHandler = EventHandler { },
    content: @Composable ScreenScope<State, ViewModel>.() -> Unit,
) {
    val viewModel: ViewModel = hiltViewModel<ViewModel>()
    DisposableEffect(Unit) {
        viewModel.onEnter()
        onDispose {
            viewModel.onExit()
        }
    }
    val scope = remember(viewModel) { ScreenScope(viewModel) }
    EventObserver(viewModel.events, eventHandler::onEventSent)
    with(scope) {
        content()
    }
}
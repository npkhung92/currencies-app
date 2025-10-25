package com.hung.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hung.core.presentation.BaseViewModel
import com.hung.core.presentation.PresentationState

class ScreenScope<State : PresentationState, ViewModel : BaseViewModel<State>>(val viewModel: ViewModel) {
    @Composable
    fun ScreenContent(content: @Composable (State) -> Unit) {
        val state by viewModel.state.collectAsStateWithLifecycle()
        content(state)
    }
}
package com.hung.core.ui

import com.hung.core.presentation.PresentationEvent

fun interface EventHandler {
    fun onEventSent(event: PresentationEvent)
}
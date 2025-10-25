package com.hung.core.presentation

abstract class ErrorEvent(open val message: String?) : PresentationEvent

data class DefaultErrorEvent(override val message: String?) : ErrorEvent(message)

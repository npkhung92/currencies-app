package com.hung.currencies.presentation

import com.hung.core.presentation.PresentationEvent

sealed interface CurrencyListEvent : PresentationEvent {
    object SucceedInsertCurrencySample : CurrencyListEvent
    object SucceedClearCurrencySample : CurrencyListEvent
}

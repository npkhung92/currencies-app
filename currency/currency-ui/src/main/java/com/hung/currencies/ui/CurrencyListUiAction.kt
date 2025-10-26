package com.hung.currencies.ui

import com.hung.currencies.presentation.model.CurrencyFilterPresentationModel

sealed interface CurrencyListUiAction {
    object Clear : CurrencyListUiAction
    object Insert : CurrencyListUiAction
    data class Filter(val filter: CurrencyFilterPresentationModel) : CurrencyListUiAction
    sealed interface Search : CurrencyListUiAction {
        object Dismiss : Search
        data class Query(val query: String) : Search
    }
}

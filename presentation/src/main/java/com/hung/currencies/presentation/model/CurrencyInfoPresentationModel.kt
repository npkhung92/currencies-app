package com.hung.currencies.presentation.model

sealed class CurrencyInfoPresentationModel(
    open val id: String,
    open val name: String,
    open val symbol: String
) {
    data class Crypto(
        override val id: String,
        override val name: String,
        override val symbol: String,
    ) : CurrencyInfoPresentationModel(id, name, symbol)

    data class Fiat(
        override val id: String,
        override val name: String,
        override val symbol: String,
        val code: String
    ) : CurrencyInfoPresentationModel(id, name, symbol)
}
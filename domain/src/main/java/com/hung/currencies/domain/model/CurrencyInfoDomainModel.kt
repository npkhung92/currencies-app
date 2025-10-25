package com.hung.currencies.domain.model

sealed interface CurrencyInfoDomainModel {
    data class Crypto(
        val id: String,
        val name: String,
        val symbol: String,
    ) : CurrencyInfoDomainModel

    data class Fiat(
        val id: String,
        val name: String,
        val symbol: String,
        val code: String
    ) : CurrencyInfoDomainModel
}
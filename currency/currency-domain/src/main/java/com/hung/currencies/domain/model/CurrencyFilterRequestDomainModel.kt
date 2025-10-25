package com.hung.currencies.domain.model

data class CurrencyFilterRequestDomainModel(
    val searchText: String,
    val currencyType: CurrencyTypeDomainModel?
)

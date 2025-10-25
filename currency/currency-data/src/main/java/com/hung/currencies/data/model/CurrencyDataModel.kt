package com.hung.currencies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyDataModel(
    val id: String,
    val name: String,
    val symbol: String,
    val code: String? = null
)

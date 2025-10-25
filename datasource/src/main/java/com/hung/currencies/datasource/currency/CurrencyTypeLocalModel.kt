package com.hung.currencies.datasource.currency

import androidx.room.TypeConverter

enum class CurrencyTypeLocalModel {
    CRYPTO,
    FIAT
}

class CurrencyTypeConverter {
    @TypeConverter
    fun toCurrencyType(value: String): CurrencyTypeLocalModel {
        return when (value) {
            "CRYPTO" -> CurrencyTypeLocalModel.CRYPTO
            "FIAT" -> CurrencyTypeLocalModel.FIAT
            else -> throw IllegalArgumentException("Invalid currency type: $value")
        }
    }

    @TypeConverter
    fun toValue(currencyType: CurrencyTypeLocalModel): String {
        return currencyType.name
    }
}

package com.hung.currencies.data.mapper

import com.hung.currencies.data.model.CurrencyDataModel
import com.hung.currencies.datasource.currency.CurrencyEntity
import com.hung.currencies.datasource.currency.CurrencyTypeLocalModel

class CurrencyDataEntityMapper {
    fun map(data: CurrencyDataModel): CurrencyEntity {
        return CurrencyEntity(
            id = data.id,
            name = data.name,
            symbol = data.symbol,
            code = data.code,
            currencyType = if (data.code != null) {
                CurrencyTypeLocalModel.FIAT
            } else {
                CurrencyTypeLocalModel.CRYPTO
            }
        )
    }
}
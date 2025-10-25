package com.hung.currencies.data.mapper

import com.hung.currencies.datasource.currency.CurrencyEntity
import com.hung.currencies.datasource.currency.CurrencyTypeLocalModel
import com.hung.currencies.domain.model.CurrencyInfoDomainModel

class CurrencyEntityDomainMapper {
    fun map(entity: CurrencyEntity): CurrencyInfoDomainModel {
        return when (entity.currencyType) {
            CurrencyTypeLocalModel.CRYPTO -> CurrencyInfoDomainModel.Crypto(
                id = entity.id,
                name = entity.name,
                symbol = entity.symbol,
            )

            CurrencyTypeLocalModel.FIAT -> CurrencyInfoDomainModel.Fiat(
                id = entity.id,
                name = entity.name,
                symbol = entity.symbol,
                code = entity.code ?: throw IllegalArgumentException("Code is required for fiat currency")
            )
        }
    }
}
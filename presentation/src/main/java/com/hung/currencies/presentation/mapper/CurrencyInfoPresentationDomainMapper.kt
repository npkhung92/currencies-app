package com.hung.currencies.presentation.mapper

import com.hung.currencies.domain.model.CurrencyInfoDomainModel
import com.hung.currencies.presentation.model.CurrencyInfoPresentationModel

class CurrencyInfoPresentationDomainMapper {
    fun map(currency: CurrencyInfoDomainModel): CurrencyInfoPresentationModel {
        return when (currency) {
            is CurrencyInfoDomainModel.Crypto -> CurrencyInfoPresentationModel.Crypto(
                id = currency.id,
                name = currency.name,
                symbol = currency.symbol
            )

            is CurrencyInfoDomainModel.Fiat -> CurrencyInfoPresentationModel.Fiat(
                id = currency.id,
                name = currency.name,
                symbol = currency.symbol,
                code = currency.code
            )
        }
    }
}
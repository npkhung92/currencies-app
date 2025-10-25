package com.hung.currencies.presentation.mapper

import com.hung.currencies.domain.model.CurrencyTypeDomainModel
import com.hung.currencies.presentation.model.CurrencyFilterPresentationModel

class CurrencyFilterPresentationDomainMapper {
    fun map(filter: CurrencyFilterPresentationModel): CurrencyTypeDomainModel? {
        return when (filter) {
            CurrencyFilterPresentationModel.FIAT -> CurrencyTypeDomainModel.FIAT
            CurrencyFilterPresentationModel.CRYPTO -> CurrencyTypeDomainModel.CRYPTO
            CurrencyFilterPresentationModel.ALL -> null
        }
    }
}
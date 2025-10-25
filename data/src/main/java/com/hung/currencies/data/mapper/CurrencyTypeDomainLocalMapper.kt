package com.hung.currencies.data.mapper

import com.hung.currencies.datasource.CurrencyTypeLocalModel
import com.hung.currencies.domain.model.CurrencyTypeDomainModel

class CurrencyTypeDomainLocalMapper {
    fun map(type: CurrencyTypeDomainModel): CurrencyTypeLocalModel {
        return when (type) {
            CurrencyTypeDomainModel.FIAT -> CurrencyTypeLocalModel.FIAT
            CurrencyTypeDomainModel.CRYPTO -> CurrencyTypeLocalModel.CRYPTO
        }
    }
}
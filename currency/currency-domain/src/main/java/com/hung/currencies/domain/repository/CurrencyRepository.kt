package com.hung.currencies.domain.repository

import com.hung.currencies.domain.model.CurrencyInfoDomainModel
import com.hung.currencies.domain.model.CurrencyFilterRequestDomainModel
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getCurrencies(request: CurrencyFilterRequestDomainModel): Flow<List<CurrencyInfoDomainModel>>
    suspend fun insertCurrencySample()
    suspend fun deleteCurrencySample()
}
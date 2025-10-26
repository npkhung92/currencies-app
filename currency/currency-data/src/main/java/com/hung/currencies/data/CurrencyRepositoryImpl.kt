package com.hung.currencies.data

import com.hung.core.data.BaseRepository
import com.hung.currencies.data.mapper.CurrencyEntityDomainMapper
import com.hung.currencies.data.mapper.CurrencyTypeDomainLocalMapper
import com.hung.currencies.data.provider.SampleCurrencyListProvider
import com.hung.currencies.datasource.currency.CurrencyDao
import com.hung.currencies.domain.model.CurrencyFilterRequestDomainModel
import com.hung.currencies.domain.model.CurrencyInfoDomainModel
import com.hung.currencies.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyRepositoryImpl(
    private val dao: CurrencyDao,
    private val currencyMapper: CurrencyEntityDomainMapper,
    private val currencyListProvider: SampleCurrencyListProvider,
    private val currencyTypeMapper: CurrencyTypeDomainLocalMapper,
) : CurrencyRepository, BaseRepository() {
    override suspend fun getCurrencies(request: CurrencyFilterRequestDomainModel): Flow<List<CurrencyInfoDomainModel>> =
        launchSafely {
            dao.getCurrencies(
                searchText = request.searchText,
                currencyType = request.currencyType?.let { currencyTypeMapper.map(it) }
            ).map { it.map { currency -> currencyMapper.map(currency) } }
        }

    override suspend fun insertCurrencySample() = launchSafely {
        dao.insertAll(*currencyListProvider.provide().toTypedArray())
    }

    override suspend fun deleteCurrencySample() = launchSafely {
        dao.deleteAll()
    }
}
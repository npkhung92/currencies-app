package com.hung.currency.di

import com.hung.currencies.data.CurrencyRepositoryImpl
import com.hung.currencies.data.local.SampleCurrencyListProvider
import com.hung.currencies.data.mapper.CurrencyEntityDomainMapper
import com.hung.currencies.data.mapper.CurrencyTypeDomainLocalMapper
import com.hung.currencies.datasource.currency.CurrencyDao
import com.hung.currencies.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesCurrencyRepository(
        currencyDao: CurrencyDao,
        currencyMapper: CurrencyEntityDomainMapper,
        currencyListProvider: SampleCurrencyListProvider,
        currencyTypeMapper: CurrencyTypeDomainLocalMapper
    ): CurrencyRepository = CurrencyRepositoryImpl(
        dao = currencyDao,
        currencyMapper = currencyMapper,
        currencyListProvider = currencyListProvider,
        currencyTypeMapper = currencyTypeMapper
    )

}
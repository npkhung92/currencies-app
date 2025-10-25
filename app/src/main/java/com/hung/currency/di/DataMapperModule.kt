package com.hung.currency.di

import com.hung.currencies.data.local.SampleCurrencyListProvider
import com.hung.currencies.data.mapper.CurrencyEntityDomainMapper
import com.hung.currencies.data.mapper.CurrencyTypeDomainLocalMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataMapperModule {
    @Provides
    fun providesCurrencyEntityDomainMapper() = CurrencyEntityDomainMapper()

    @Provides
    fun providesSampleCurrencyListProvider() = SampleCurrencyListProvider()

    @Provides
    fun providesCurrencyTypeDomainLocalMapper() = CurrencyTypeDomainLocalMapper()
}
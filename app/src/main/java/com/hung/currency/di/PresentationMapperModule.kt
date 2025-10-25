package com.hung.currency.di

import com.hung.currencies.presentation.mapper.CurrencyFilterPresentationDomainMapper
import com.hung.currencies.presentation.mapper.CurrencyInfoPresentationDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PresentationMapperModule {
    @Provides
    fun providesCurrencyFilterPresentationDomainMapper() = CurrencyFilterPresentationDomainMapper()

    @Provides
    fun providesCurrencyInfoPresentationDomainMapper() = CurrencyInfoPresentationDomainMapper()
}
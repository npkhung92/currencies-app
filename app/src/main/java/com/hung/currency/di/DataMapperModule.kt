package com.hung.currency.di

import android.content.Context
import com.hung.currencies.data.mapper.CurrencyDataEntityMapper
import com.hung.currencies.data.mapper.CurrencyEntityDomainMapper
import com.hung.currencies.data.mapper.CurrencyTypeDomainLocalMapper
import com.hung.currencies.data.provider.SampleCurrencyListProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object DataMapperModule {
    @Provides
    fun providesCurrencyEntityDomainMapper() = CurrencyEntityDomainMapper()

    @Provides
    fun providesCurrencyDataEntityMapper() = CurrencyDataEntityMapper()

    @Provides
    fun providesSampleCurrencyListProvider(
        @ApplicationContext context: Context,
        json: Json,
        mapper: CurrencyDataEntityMapper
    ) = SampleCurrencyListProvider(
        context = context,
        json = json,
        mapper = mapper
    )

    @Provides
    fun providesCurrencyTypeDomainLocalMapper() = CurrencyTypeDomainLocalMapper()
}
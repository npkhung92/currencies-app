package com.hung.currency.di

import com.hung.currencies.domain.repository.CurrencyRepository
import com.hung.currencies.domain.usecase.DeleteCurrencySampleUseCase
import com.hung.currencies.domain.usecase.GetCurrencyListUseCase
import com.hung.currencies.domain.usecase.InsertCurrencySampleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providesGetCurrencyListUseCase(currencyRepository: CurrencyRepository): GetCurrencyListUseCase =
        GetCurrencyListUseCase(currencyRepository)

    @Provides
    fun providesInsertCurrencySampleUseCase(currencyRepository: CurrencyRepository): InsertCurrencySampleUseCase =
        InsertCurrencySampleUseCase(currencyRepository)

    @Provides
    fun providesDeleteCurrencySampleUseCase(currencyRepository: CurrencyRepository): DeleteCurrencySampleUseCase =
        DeleteCurrencySampleUseCase(currencyRepository)

}
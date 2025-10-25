package com.hung.currencies.domain.usecase

import com.hung.currencies.domain.repository.CurrencyRepository

class InsertCurrencySampleUseCase(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke() = currencyRepository.insertCurrencySample()
}
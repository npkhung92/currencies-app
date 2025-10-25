package com.hung.currencies.domain.usecase

import com.hung.currencies.domain.repository.CurrencyRepository

class DeleteCurrencySampleUseCase(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke() = currencyRepository.deleteCurrencySample()
}
package com.hung.currencies.domain.usecase

import com.hung.core.domain.OneTimeUseCase
import com.hung.currencies.domain.repository.CurrencyRepository

class InsertCurrencySampleUseCase(
    private val currencyRepository: CurrencyRepository
) : OneTimeUseCase<Unit, Unit> {
    override suspend fun invoke(request: Unit) = currencyRepository.insertCurrencySample()
}
package com.hung.currencies.domain.usecase

import com.hung.currencies.domain.model.CurrencyFilterRequestDomainModel
import com.hung.currencies.domain.repository.CurrencyRepository

class GetCurrencyListUseCase(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(request: CurrencyFilterRequestDomainModel) = currencyRepository.getCurrencies(request)
}
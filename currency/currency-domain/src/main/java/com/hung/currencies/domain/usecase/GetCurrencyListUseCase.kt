package com.hung.currencies.domain.usecase

import com.hung.core.domain.ContinuousUseCase
import com.hung.currencies.domain.model.CurrencyFilterRequestDomainModel
import com.hung.currencies.domain.model.CurrencyInfoDomainModel
import com.hung.currencies.domain.repository.CurrencyRepository

class GetCurrencyListUseCase(
    private val currencyRepository: CurrencyRepository
) : ContinuousUseCase<CurrencyFilterRequestDomainModel, List<CurrencyInfoDomainModel>> {
    override suspend fun invoke(
        request: CurrencyFilterRequestDomainModel,
        onResult: (List<CurrencyInfoDomainModel>) -> Unit
    ) {
        currencyRepository.getCurrencies(request).collect { currencyList ->
            onResult(currencyList)
        }
    }
}
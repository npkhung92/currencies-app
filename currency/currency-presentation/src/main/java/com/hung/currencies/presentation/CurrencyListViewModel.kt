package com.hung.currencies.presentation

import com.hung.core.presentation.BaseViewModel
import com.hung.core.presentation.PresentationState
import com.hung.currencies.domain.model.CurrencyFilterRequestDomainModel
import com.hung.currencies.domain.usecase.DeleteCurrencySampleUseCase
import com.hung.currencies.domain.usecase.GetCurrencyListUseCase
import com.hung.currencies.domain.usecase.InsertCurrencySampleUseCase
import com.hung.currencies.presentation.mapper.CurrencyFilterPresentationDomainMapper
import com.hung.currencies.presentation.mapper.CurrencyInfoPresentationDomainMapper
import com.hung.currencies.presentation.model.CurrencyFilterPresentationModel
import com.hung.currencies.presentation.model.CurrencyInfoPresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val getCurrencyListUseCase: GetCurrencyListUseCase,
    private val deleteCurrencySampleUseCase: DeleteCurrencySampleUseCase,
    private val insertCurrencySampleUseCase: InsertCurrencySampleUseCase,
    private val currencyFilterMapper: CurrencyFilterPresentationDomainMapper,
    private val currencyInfoMapper: CurrencyInfoPresentationDomainMapper
) : BaseViewModel<CurrencyListScreenPresentationState>(CurrencyListScreenPresentationState()) {

    var searchJob: Job? = null

    override fun onEnter() {
        with(state.value) {
            getCurrencyList(searchQuery, filter)
        }
    }

    fun onFilterAction(filter: CurrencyFilterPresentationModel) {
        updateState { lastState ->
            lastState.copy(
                filter = filter
            )
        }
        getCurrencyList(state.value.searchQuery, filter)
    }

    fun onInsertAction() {
        launchUseCase {
            insertCurrencySampleUseCase(Unit)
            sendEvent(CurrencyListEvent.SucceedInsertCurrencySample)
        }
    }

    fun onClearAction() {
        launchUseCase {
            deleteCurrencySampleUseCase(Unit)
            sendEvent(CurrencyListEvent.SucceedClearCurrencySample)
        }
    }

    fun onSearchQueryAction(searchText: String) {
        updateState { lastState ->
            lastState.copy(
                searchQuery = searchText
            )
        }
        getCurrencyList(searchText, state.value.filter)
    }

    override fun onExit() {
        searchJob?.cancel()
    }

    private fun getCurrencyList(searchText: String, filter: CurrencyFilterPresentationModel) {
        searchJob?.cancel()
        searchJob = launchUseCase {
            getCurrencyListUseCase(
                request = CurrencyFilterRequestDomainModel(
                    searchText = searchText,
                    currencyType = currencyFilterMapper.map(filter)
                ),
                onResult = { currencyList ->
                    updateState { lastState ->
                        lastState.copy(
                            currencyList = currencyList.map { currencyInfoMapper.map(it) }
                        )
                    }
                }
            )
        }
    }
}

data class CurrencyListScreenPresentationState(
    val currencyList: List<CurrencyInfoPresentationModel> = emptyList(),
    val filter: CurrencyFilterPresentationModel = CurrencyFilterPresentationModel.ALL,
    val searchQuery: String = ""
) : PresentationState
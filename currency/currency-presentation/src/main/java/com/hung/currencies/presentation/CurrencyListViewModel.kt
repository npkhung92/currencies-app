package com.hung.currencies.presentation

import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            insertCurrencySampleUseCase()
        }
    }

    fun onClearAction() {
        viewModelScope.launch {
            deleteCurrencySampleUseCase()
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
        searchJob = viewModelScope.launch {
            val result = runCatching {
                getCurrencyListUseCase(
                    request = CurrencyFilterRequestDomainModel(
                        searchText = searchText,
                        currencyType = currencyFilterMapper.map(filter)
                    )
                ).onStart {
                    updateState { lastState ->
                        lastState.copy(
                            isLoading = true
                        )
                    }
                }.onCompletion { throwable ->
                    throwable?.let {
                        updateState { lastState ->
                            lastState.copy(
                                isLoading = false,
                                isError = true
                            )
                        }
                    }
                }.collect { currencyList ->
                    updateState { lastState ->
                        lastState.copy(
                            isLoading = false,
                            isError = false,
                            currencyList = currencyList.map { currencyInfoMapper.map(it) }
                        )
                    }
                }
            }
            if (!result.isSuccess) {
                updateState { lastState ->
                    lastState.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }
    }
}

data class CurrencyListScreenPresentationState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val currencyList: List<CurrencyInfoPresentationModel> = emptyList(),
    val filter: CurrencyFilterPresentationModel = CurrencyFilterPresentationModel.ALL,
    val searchQuery: String = ""
) : PresentationState
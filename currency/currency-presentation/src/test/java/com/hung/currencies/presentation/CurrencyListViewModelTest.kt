package com.hung.currencies.presentation

import com.hung.core.presentation.DefaultErrorEvent
import com.hung.currencies.domain.model.CurrencyFilterRequestDomainModel
import com.hung.currencies.domain.model.CurrencyInfoDomainModel
import com.hung.currencies.domain.model.CurrencyTypeDomainModel
import com.hung.currencies.domain.usecase.DeleteCurrencySampleUseCase
import com.hung.currencies.domain.usecase.GetCurrencyListUseCase
import com.hung.currencies.domain.usecase.InsertCurrencySampleUseCase
import com.hung.currencies.presentation.mapper.CurrencyFilterPresentationDomainMapper
import com.hung.currencies.presentation.mapper.CurrencyInfoPresentationDomainMapper
import com.hung.currencies.presentation.model.CurrencyFilterPresentationModel
import com.hung.currencies.presentation.model.CurrencyInfoPresentationModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyListViewModelTest {

    @MockK
    lateinit var getCurrencyListUseCase: GetCurrencyListUseCase

    @MockK
    lateinit var deleteCurrencySampleUseCase: DeleteCurrencySampleUseCase

    @MockK
    lateinit var insertCurrencySampleUseCase: InsertCurrencySampleUseCase

    @MockK
    lateinit var currencyFilterMapper: CurrencyFilterPresentationDomainMapper

    @MockK
    lateinit var currencyInfoMapper: CurrencyInfoPresentationDomainMapper
    private lateinit var viewModel: CurrencyListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = CurrencyListViewModel(
            getCurrencyListUseCase = getCurrencyListUseCase,
            deleteCurrencySampleUseCase = deleteCurrencySampleUseCase,
            insertCurrencySampleUseCase = insertCurrencySampleUseCase,
            currencyFilterMapper = currencyFilterMapper,
            currencyInfoMapper = currencyInfoMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `initial state should have default values`() = runTest {
        val initialState = viewModel.state.value

        assertTrue(initialState.currencyList.isEmpty())
        assertEquals(CurrencyFilterPresentationModel.ALL, initialState.filter)
        assertEquals("", initialState.searchQuery)
    }

    @Test
    fun `onEnter should call getCurrencyList with current search query and filter`() = runTest {
        // Given
        val mockCurrencies = listOf(
            CurrencyInfoDomainModel.Crypto("bitcoin", "Bitcoin", "BTC"),
            CurrencyInfoDomainModel.Fiat("usd", "US Dollar", "$", "USD")
        )
        val mockPresentationCurrencies = listOf(
            CurrencyInfoPresentationModel.Crypto("bitcoin", "Bitcoin", "BTC"),
            CurrencyInfoPresentationModel.Fiat("usd", "US Dollar", "$", "USD")
        )
        val request = CurrencyFilterRequestDomainModel(
            searchText = "",
            currencyType = null
        )

        coEvery { getCurrencyListUseCase(request) } returns flowOf(mockCurrencies)
        every { currencyFilterMapper.map(CurrencyFilterPresentationModel.ALL) } returns null
        mockCurrencies.forEachIndexed { index, model ->
            every { currencyInfoMapper.map(model) } returns mockPresentationCurrencies[index]
        }

        // When
        viewModel.onEnter()
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(2, state.currencyList.size)
    }

    @Test
    fun `onFilterAction should update filter and call getCurrencyList`() = runTest {
        // Given
        val newFilter = CurrencyFilterPresentationModel.CRYPTO
        val mockCurrencies = listOf(CurrencyInfoDomainModel.Crypto("bitcoin", "Bitcoin", "BTC"))
        val mockPresentationCurrencies = listOf(CurrencyInfoPresentationModel.Crypto("bitcoin", "Bitcoin", "BTC"))
        val request = CurrencyFilterRequestDomainModel(
            searchText = "",
            currencyType = CurrencyTypeDomainModel.CRYPTO
        )

        coEvery { getCurrencyListUseCase(request) } returns flowOf(mockCurrencies)
        every { currencyFilterMapper.map(CurrencyFilterPresentationModel.CRYPTO) } returns CurrencyTypeDomainModel.CRYPTO
        mockCurrencies.forEachIndexed { index, model ->
            every { currencyInfoMapper.map(model) } returns mockPresentationCurrencies[index]
        }

        // When
        viewModel.onFilterAction(newFilter)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(CurrencyFilterPresentationModel.CRYPTO, state.filter)
    }

    @Test
    fun `onSearchQueryAction should update search query and call getCurrencyList`() = runTest {
        // Given
        val searchText = "bitcoin"
        val mockCurrencies = listOf(CurrencyInfoDomainModel.Crypto("bitcoin", "Bitcoin", "BTC"))
        val mockPresentationCurrencies = listOf(CurrencyInfoPresentationModel.Crypto("bitcoin", "Bitcoin", "BTC"))
        val request = CurrencyFilterRequestDomainModel(
            searchText = searchText,
            currencyType = null
        )

        coEvery { getCurrencyListUseCase(request) } returns flowOf(mockCurrencies)
        every { currencyFilterMapper.map(CurrencyFilterPresentationModel.ALL) } returns null
        mockCurrencies.forEachIndexed { index, model ->
            every { currencyInfoMapper.map(model) } returns mockPresentationCurrencies[index]
        }

        // When
        viewModel.onSearchQueryAction(searchText)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(searchText, state.searchQuery)
    }

    @Test
    fun `onInsertAction should call insertCurrencySampleUseCase`() = runTest {
        // Given
        coEvery { insertCurrencySampleUseCase() } returns Unit

        // When
        viewModel.onInsertAction()
        advanceUntilIdle()

        // Then
        coVerify { insertCurrencySampleUseCase() }
    }

    @Test
    fun `onClearAction should call deleteCurrencySampleUseCase`() = runTest {
        // Given
        coEvery { deleteCurrencySampleUseCase() } returns Unit

        // When
        viewModel.onClearAction()
        advanceUntilIdle()

        // Then
        coVerify { deleteCurrencySampleUseCase() }
    }

    @Test
    fun `getCurrencyList should handle empty result`() = runTest {
        // Given
        coEvery { getCurrencyListUseCase(any()) } returns flowOf(emptyList())
        every { currencyFilterMapper.map(any()) } returns null

        // When
        viewModel.onEnter()
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state.currencyList.isEmpty())
    }

    @Test
    fun `search query should be preserved across filter changes`() = runTest {
        // Given
        val searchText = "bitcoin"
        val mockCurrencies = listOf(CurrencyInfoDomainModel.Crypto("bitcoin", "Bitcoin", "BTC"))
        val mockPresentationCurrencies = listOf(CurrencyInfoPresentationModel.Crypto("bitcoin", "Bitcoin", "BTC"))
        val request = CurrencyFilterRequestDomainModel(
            searchText,
            CurrencyTypeDomainModel.CRYPTO
        )

        coEvery { getCurrencyListUseCase(request) } returns flowOf(mockCurrencies)
        every { currencyFilterMapper.map(CurrencyFilterPresentationModel.CRYPTO) } returns CurrencyTypeDomainModel.CRYPTO
        mockCurrencies.forEachIndexed { index, model ->
            every { currencyInfoMapper.map(model) } returns mockPresentationCurrencies[index]
        }

        // When
        viewModel.onSearchQueryAction(searchText)
        viewModel.onFilterAction(CurrencyFilterPresentationModel.CRYPTO)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(searchText, state.searchQuery)
    }

    @Test
    fun `filter should be preserved across search query changes`() = runTest {
        // Given
        val filter = CurrencyFilterPresentationModel.FIAT
        val mockCurrencies = listOf(CurrencyInfoDomainModel.Fiat("usd", "US Dollar", "$", "USD"))
        val mockPresentationCurrencies = listOf(CurrencyInfoPresentationModel.Fiat("usd", "US Dollar", "$", "USD"))
        val request = CurrencyFilterRequestDomainModel("dollar", CurrencyTypeDomainModel.FIAT)

        coEvery { getCurrencyListUseCase(request) } returns flowOf(mockCurrencies)
        every { currencyFilterMapper.map(CurrencyFilterPresentationModel.FIAT) } returns CurrencyTypeDomainModel.FIAT
        mockCurrencies.forEachIndexed { index, model ->
            every { currencyInfoMapper.map(model) } returns mockPresentationCurrencies[index]
        }

        // When
        viewModel.onFilterAction(filter)
        viewModel.onSearchQueryAction("dollar")
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(filter, state.filter)
    }

    @Test
    fun `onInsertAction should send DefaultErrorEvent when insertCurrencySampleUseCase throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Insert failed")
        coEvery { insertCurrencySampleUseCase() } throws exception

        // When
        viewModel.onInsertAction()
        advanceUntilIdle()

        // Then
        val event = viewModel.events.first()
        assertTrue(event is DefaultErrorEvent && event.message == "Insert failed")
    }

    @Test
    fun `onClearAction should send DefaultErrorEvent when deleteCurrencySampleUseCase throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Delete failed")
        coEvery { deleteCurrencySampleUseCase() } throws exception

        // When
        viewModel.onClearAction()
        advanceUntilIdle()

        // Then
        val event = viewModel.events.first()
        assertTrue(event is DefaultErrorEvent && event.message == "Delete failed")
    }

    @Test
    fun `getCurrencyList should send DefaultErrorEvent when getCurrencyListUseCase throws exception in launchUseCase`() =
        runTest {
            // Given
            val exception = RuntimeException("Get currency list failed")
            coEvery { getCurrencyListUseCase(any()) } throws exception
            every { currencyFilterMapper.map(any()) } returns null

            // When
            viewModel.onEnter()
            advanceUntilIdle()

            // Then
            val event = viewModel.events.first()
            assertTrue(event is DefaultErrorEvent && event.message == "Get currency list failed")
        }

    @Test
    fun `getCurrencyList should send DefaultErrorEvent when flow onCompletion throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Flow completion error")
        val flowWithCompletionException = flow<List<CurrencyInfoDomainModel>> {
            emit(emptyList())
        }.onCompletion { throw exception }

        coEvery { getCurrencyListUseCase(any()) } returns flowWithCompletionException
        every { currencyFilterMapper.map(any()) } returns null

        // When
        viewModel.onEnter()
        advanceUntilIdle()

        // Then
        val event = viewModel.events.first()
        assertTrue(event is DefaultErrorEvent && event.message == "Flow completion error")
    }
}
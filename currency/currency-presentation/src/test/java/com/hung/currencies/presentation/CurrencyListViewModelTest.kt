package com.hung.currencies.presentation

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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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

        assertTrue(initialState.isLoading)
        assertFalse(initialState.isError)
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
        assertFalse(state.isLoading)
        assertFalse(state.isError)
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
    fun `getCurrencyList should handle error and update state correctly`() = runTest {
        // Given
        coEvery { getCurrencyListUseCase(any()) } throws RuntimeException("Database error")
        every { currencyFilterMapper.map(any()) } returns null

        // When
        viewModel.onEnter()
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
        assertTrue(state.currencyList.isEmpty())
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
        assertFalse(state.isLoading)
        assertFalse(state.isError)
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
    fun `getCurrencyList should handle onCompletion with exception and update state correctly`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        val flowWithException = flow<List<CurrencyInfoDomainModel>> {
            throw exception
        }
        
        coEvery { getCurrencyListUseCase(any()) } returns flowWithException
        every { currencyFilterMapper.map(any()) } returns null

        // When
        viewModel.onEnter()
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
        assertTrue(state.currencyList.isEmpty())
    }
}
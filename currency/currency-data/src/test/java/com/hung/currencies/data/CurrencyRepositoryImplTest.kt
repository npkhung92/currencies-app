package com.hung.currencies.data

import com.hung.currencies.data.local.SampleCurrencyListProvider
import com.hung.currencies.data.mapper.CurrencyEntityDomainMapper
import com.hung.currencies.data.mapper.CurrencyTypeDomainLocalMapper
import com.hung.currencies.datasource.currency.CurrencyDao
import com.hung.currencies.datasource.currency.CurrencyEntity
import com.hung.currencies.datasource.currency.CurrencyTypeLocalModel
import com.hung.currencies.domain.model.CurrencyFilterRequestDomainModel
import com.hung.currencies.domain.model.CurrencyInfoDomainModel
import com.hung.currencies.domain.model.CurrencyTypeDomainModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CurrencyRepositoryImplTest {

    private lateinit var currencyRepositoryImpl: CurrencyRepositoryImpl

    @MockK
    lateinit var mockDao: CurrencyDao

    @MockK
    lateinit var mockCurrencyMapper: CurrencyEntityDomainMapper

    @MockK
    lateinit var mockCurrencyListProvider: SampleCurrencyListProvider

    @MockK
    lateinit var mockCurrencyTypeMapper: CurrencyTypeDomainLocalMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        currencyRepositoryImpl = CurrencyRepositoryImpl(
            dao = mockDao,
            currencyMapper = mockCurrencyMapper,
            currencyListProvider = mockCurrencyListProvider,
            currencyTypeMapper = mockCurrencyTypeMapper
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCurrencies should return mapped crypto currencies when dao returns entities`() = runTest {
        // Given
        val request = CurrencyFilterRequestDomainModel(
            searchText = "Bitcoin",
            currencyType = CurrencyTypeDomainModel.CRYPTO
        )

        val mockEntities = listOf(
            CurrencyEntity(
                id = "BTC",
                name = "Bitcoin",
                symbol = "BTC",
                currencyType = CurrencyTypeLocalModel.CRYPTO
            ),
            CurrencyEntity(
                id = "BCH",
                name = "Bitcoin Cash",
                symbol = "BCH",
                currencyType = CurrencyTypeLocalModel.CRYPTO
            )
        )

        val mockDomainModels = listOf(
            CurrencyInfoDomainModel.Crypto(
                id = "BTC",
                name = "Bitcoin",
                symbol = "BTC"
            ),
            CurrencyInfoDomainModel.Crypto(
                id = "BCH",
                name = "Bitcoin Cash",
                symbol = "BCH"
            )
        )

        coEvery { mockCurrencyTypeMapper.map(CurrencyTypeDomainModel.CRYPTO) } returns CurrencyTypeLocalModel.CRYPTO
        every { mockDao.getCurrencies("Bitcoin", CurrencyTypeLocalModel.CRYPTO) } returns flowOf(mockEntities)
        every { mockCurrencyMapper.map(mockEntities[0]) } returns mockDomainModels[0]
        every { mockCurrencyMapper.map(mockEntities[1]) } returns mockDomainModels[1]

        // When
        val result = currencyRepositoryImpl.getCurrencies(request).first()

        // Then
        assertEquals(2, result.size)
        assertEquals(mockDomainModels[0], result[0])
        assertEquals(mockDomainModels[1], result[1])
        coVerify { mockCurrencyTypeMapper.map(CurrencyTypeDomainModel.CRYPTO) }
    }

    @Test
    fun `getCurrencies should return mapped fiat currencies when dao returns entities`() = runTest {
        // Given
        val request = CurrencyFilterRequestDomainModel(
            searchText = "Dollar",
            currencyType = CurrencyTypeDomainModel.FIAT
        )

        val mockEntity = CurrencyEntity(
            id = "USD",
            name = "US Dollar",
            symbol = "$",
            code = "USD",
            currencyType = CurrencyTypeLocalModel.FIAT
        )

        val mockDomainModel = CurrencyInfoDomainModel.Fiat(
            id = "USD",
            name = "US Dollar",
            symbol = "$",
            code = "USD"
        )

        coEvery { mockCurrencyTypeMapper.map(CurrencyTypeDomainModel.FIAT) } returns CurrencyTypeLocalModel.FIAT
        every { mockDao.getCurrencies("Dollar", CurrencyTypeLocalModel.FIAT) } returns flowOf(listOf(mockEntity))
        every { mockCurrencyMapper.map(mockEntity) } returns mockDomainModel

        // When
        val result = currencyRepositoryImpl.getCurrencies(request).first()

        // Then
        assertEquals(1, result.size)
        assertEquals(mockDomainModel, result[0])
        coVerify { mockCurrencyTypeMapper.map(CurrencyTypeDomainModel.FIAT) }
    }

    @Test
    fun `getCurrencies should return empty list when dao returns empty list`() = runTest {
        // Given
        val request = CurrencyFilterRequestDomainModel(
            searchText = "RandomText",
            currencyType = null
        )

        every { mockDao.getCurrencies("RandomText", null) } returns flowOf(emptyList())

        // When
        val result = currencyRepositoryImpl.getCurrencies(request).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getCurrencies should handle null currency type correctly`() = runTest {
        // Given
        val request = CurrencyFilterRequestDomainModel(
            searchText = "USD",
            currencyType = null
        )

        val mockEntities = listOf(
            CurrencyEntity(
                id = "USDC",
                name = "USD Coin",
                symbol = "USDC",
                currencyType = CurrencyTypeLocalModel.CRYPTO
            ),
            CurrencyEntity(
                id = "USD",
                name = "US Dollar",
                symbol = "$",
                code = "USD",
                currencyType = CurrencyTypeLocalModel.FIAT
            )
        )

        val mockDomainModels = listOf(
            CurrencyInfoDomainModel.Crypto(
                id = "USDC",
                name = "USD Coin",
                symbol = "USDC"
            ),
            CurrencyInfoDomainModel.Fiat(
                id = "USD",
                name = "US Dollar",
                symbol = "$",
                code = "USD"
            )
        )
        every { mockDao.getCurrencies("USD", null) } returns flowOf(mockEntities)
        mockEntities.forEachIndexed { index, entity ->
            every { mockCurrencyMapper.map(entity) } returns mockDomainModels[index]
        }

        // When
        val result = currencyRepositoryImpl.getCurrencies(request).first()

        // Then
        assertEquals(2, result.size)
        assertEquals(mockDomainModels[0], result[0])
        assertEquals(mockDomainModels[1], result[1])
    }

    @Test
    fun `insertCurrencySample should call dao insertAll with sample data`() = runTest {
        // Given
        val sampleCurrencies = listOf(
            CurrencyEntity(
                id = "BTC",
                name = "Bitcoin",
                symbol = "BTC",
                currencyType = CurrencyTypeLocalModel.CRYPTO
            ),
            CurrencyEntity(
                id = "USD",
                name = "US Dollar",
                symbol = "$",
                code = "USD",
                currencyType = CurrencyTypeLocalModel.FIAT
            )
        )

        every { mockCurrencyListProvider.provide() } returns sampleCurrencies
        coEvery { mockDao.insertAll(*sampleCurrencies.toTypedArray()) } returns Unit

        // When
        currencyRepositoryImpl.insertCurrencySample()

        // Then
        coVerify { mockDao.insertAll(*sampleCurrencies.toTypedArray()) }
        coVerify { mockCurrencyListProvider.provide() }
    }

    @Test
    fun `insertCurrencySample should handle empty sample data`() = runTest {
        // Given
        every { mockCurrencyListProvider.provide() } returns emptyList()
        coEvery { mockDao.insertAll() } returns Unit

        // When
        currencyRepositoryImpl.insertCurrencySample()

        // Then
        coVerify { mockDao.insertAll() }
        coVerify { mockCurrencyListProvider.provide() }
    }

    @Test
    fun `deleteCurrencySample should call dao deleteAll`() = runTest {
        // Given
        coEvery { mockDao.deleteAll() } returns Unit

        // When
        currencyRepositoryImpl.deleteCurrencySample()

        // Then
        coVerify { mockDao.deleteAll() }
    }
}
package com.hung.currencies.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.hung.currencies.datasource.currency.CurrencyDao
import com.hung.currencies.datasource.currency.CurrencyEntity
import com.hung.currencies.datasource.currency.CurrencyTypeLocalModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CurrencyDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: CurrencyDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.currencyDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private val testBTCEntity = CurrencyEntity(
        id = "bitcoin",
        name = "Bitcoin",
        symbol = "BTC",
        code = "BTC",
        currencyType = CurrencyTypeLocalModel.CRYPTO
    )

    private val testETHEntity = CurrencyEntity(
        id = "ethereum",
        name = "Ethereum Classic",
        symbol = "ETH",
        code = "ETH",
        currencyType = CurrencyTypeLocalModel.CRYPTO
    )

    private val testUSDEntity = CurrencyEntity(
        id = "usd",
        name = "US Dollar",
        symbol = "$",
        code = "USD",
        currencyType = CurrencyTypeLocalModel.FIAT
    )

    private val testEUREntity = CurrencyEntity(
        id = "euro",
        name = "Euro",
        symbol = "€",
        code = "EUR",
        currencyType = CurrencyTypeLocalModel.FIAT
    )

    @Test
    fun getCurrencies_shouldReturnEmptyFlow_whenDatabaseIsEmpty() = runTest {
        // When
        val result = dao.getCurrencies("", null).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun getCurrencies_shouldReturnAllCurrencies_whenSearchTextIsEmpty() = runTest {
        // Given
        dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity)

        // When
        val result = dao.getCurrencies("", null).first()

        // Then
        assertEquals(3, result.size)
        assertTrue(result.contains(testBTCEntity))
        assertTrue(result.contains(testETHEntity))
        assertTrue(result.contains(testUSDEntity))
    }

    @Test
    fun getCurrencies_shouldFilterByNamePrefix() = runTest {
        // Given
        dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity)

        // When
        val result = dao.getCurrencies("bit", null).first()

        // Then
        assertEquals(1, result.size)
        assertEquals(testBTCEntity, result.first())
    }

    @Test
    fun getCurrencies_shouldReturnEmpty_ifFilterByNameButTextIsNotPrefix() = runTest {
        // Given
        dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity)

        // When
        val result = dao.getCurrencies("coin", null).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun getCurrencies_shouldReturnMatchedCurrencies_ifFilterByNameContainingPartialMatchWithSpacePrefixedToSearchText() =
        runTest {
            // Given
            dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity)

            // When
            val result = dao.getCurrencies("class", null).first()

            // Then
            assertEquals(1, result.size)
            assertEquals(testETHEntity, result.first())
        }

    @Test
    fun getCurrencies_shouldFilterBySymbolPrefix() = runTest {
        // Given
        dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity)

        // When
        val result = dao.getCurrencies("BT", null).first()

        // Then
        assertEquals(1, result.size)
        assertEquals(testBTCEntity, result.first())
    }

    @Test
    fun getCurrencies_shouldFilterByCryptoCurrencyType() = runTest {
        // Given
        dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity, testEUREntity)

        // When
        val cryptoResult = dao.getCurrencies("", CurrencyTypeLocalModel.CRYPTO).first()

        // Then
        assertEquals(2, cryptoResult.size)
        assertTrue(cryptoResult.contains(testBTCEntity))
        assertTrue(cryptoResult.contains(testETHEntity))
    }

    @Test
    fun getCurrencies_shouldFilterByFiatCurrencyType() = runTest {
        // Given
        dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity, testEUREntity)

        // When
        val fiatResult = dao.getCurrencies("", CurrencyTypeLocalModel.FIAT).first()

        // Then
        assertEquals(2, fiatResult.size)
        assertTrue(fiatResult.contains(testUSDEntity))
        assertTrue(fiatResult.contains(testEUREntity))
    }

    @Test
    fun getCurrencies_shouldFilterByBothSearchTextAndCurrencyType() = runTest {
        // Given
        dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity, testEUREntity)

        // When
        val result = dao.getCurrencies("Bit", CurrencyTypeLocalModel.CRYPTO).first()

        // Then
        assertEquals(1, result.size)
        assertEquals(testBTCEntity, result.first())
    }

    @Test
    fun getCurrencies_shouldReturnEmptyResult_whenNoSearchTextMatches() = runTest {
        // Given
        dao.insertAll(testBTCEntity, testETHEntity)

        // When
        val result = dao.getCurrencies("RandomText", null).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun insertAll_shouldInsertCurrenciesSuccessfully() = runTest {
        // When
        dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity)

        // Then
        val result = dao.getCurrencies("", null).first()
        assertEquals(3, result.size)
        assertTrue(result.contains(testBTCEntity))
        assertTrue(result.contains(testETHEntity))
        assertTrue(result.contains(testUSDEntity))
    }

    @Test
    fun deleteAll_shouldRemoveAllCurrencies() = runTest {
        // Given
        dao.insertAll(testBTCEntity, testETHEntity, testUSDEntity)
        assertEquals(3, dao.getCurrencies("", null).first().size)

        // When
        dao.deleteAll()

        // Then
        val result = dao.getCurrencies("", null).first()
        assertTrue(result.isEmpty())
    }
}
package com.hung.currencies.data.mapper

import com.hung.currencies.datasource.currency.CurrencyEntity
import com.hung.currencies.datasource.currency.CurrencyTypeLocalModel
import com.hung.currencies.domain.model.CurrencyInfoDomainModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CurrencyEntityDomainMapperTest {

    private lateinit var mapper: CurrencyEntityDomainMapper

    @Before
    fun setUp() {
        mapper = CurrencyEntityDomainMapper()
    }

    @Test
    fun `map should return Crypto domain model when entity is crypto type`() {
        // Given
        val entity = CurrencyEntity(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC",
            code = null,
            currencyType = CurrencyTypeLocalModel.CRYPTO
        )

        // When
        val result = mapper.map(entity)

        // Then
        assertTrue(result is CurrencyInfoDomainModel.Crypto)
        val cryptoResult = result as CurrencyInfoDomainModel.Crypto
        assertEquals("BTC", cryptoResult.id)
        assertEquals("Bitcoin", cryptoResult.name)
        assertEquals("BTC", cryptoResult.symbol)
    }

    @Test
    fun `map should return Fiat domain model when entity is fiat type with code`() {
        // Given
        val entity = CurrencyEntity(
            id = "USD",
            name = "US Dollar",
            symbol = "$",
            code = "USD",
            currencyType = CurrencyTypeLocalModel.FIAT
        )

        // When
        val result = mapper.map(entity)

        // Then
        assertTrue(result is CurrencyInfoDomainModel.Fiat)
        val fiatResult = result as CurrencyInfoDomainModel.Fiat
        assertEquals("USD", fiatResult.id)
        assertEquals("US Dollar", fiatResult.name)
        assertEquals("$", fiatResult.symbol)
        assertEquals("USD", fiatResult.code)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `map should throw IllegalArgumentException when fiat entity has null code`() {
        // Given
        val entity = CurrencyEntity(
            id = "GBP",
            name = "British Pound",
            symbol = "£",
            code = null,
            currencyType = CurrencyTypeLocalModel.FIAT
        )

        // When
        mapper.map(entity)
    }
}
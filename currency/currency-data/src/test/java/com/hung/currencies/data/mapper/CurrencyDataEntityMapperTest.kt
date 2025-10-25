package com.hung.currencies.data.mapper

import com.hung.currencies.data.model.CurrencyDataModel
import com.hung.currencies.datasource.currency.CurrencyEntity
import com.hung.currencies.datasource.currency.CurrencyTypeLocalModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyDataEntityMapperTest {

    private lateinit var mapper: CurrencyDataEntityMapper

    @Before
    fun setUp() {
        mapper = CurrencyDataEntityMapper()
    }

    @Test
    fun `map should return FIAT currency entity when code is not null`() {
        // Given
        val dataModel = CurrencyDataModel(
            id = "USD",
            name = "US Dollar",
            symbol = "$",
            code = "USD"
        )

        // When
        val result = mapper.map(dataModel)

        // Then
        val expectedEntity = CurrencyEntity(
            id = "USD",
            name = "US Dollar",
            symbol = "$",
            code = "USD",
            currencyType = CurrencyTypeLocalModel.FIAT
        )
        assertEquals(expectedEntity, result)
    }

    @Test
    fun `map should return CRYPTO currency entity when code is null`() {
        // Given
        val dataModel = CurrencyDataModel(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC",
            code = null
        )

        // When
        val result = mapper.map(dataModel)

        // Then
        val expectedEntity = CurrencyEntity(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC",
            code = null,
            currencyType = CurrencyTypeLocalModel.CRYPTO
        )
        assertEquals(expectedEntity, result)
    }

    @Test
    fun `map should return FIAT currency entity when code is empty string`() {
        // Given
        val dataModel = CurrencyDataModel(
            id = "ETH",
            name = "Ethereum",
            symbol = "ETH",
            code = ""
        )

        // When
        val result = mapper.map(dataModel)

        // Then
        val expectedEntity = CurrencyEntity(
            id = "ETH",
            name = "Ethereum",
            symbol = "ETH",
            code = "",
            currencyType = CurrencyTypeLocalModel.FIAT
        )
        assertEquals(expectedEntity, result)
    }

    @Test
    fun `map should return FIAT currency entity when code is blank string`() {
        // Given
        val dataModel = CurrencyDataModel(
            id = "ADA",
            name = "Cardano",
            symbol = "ADA",
            code = "   "
        )

        // When
        val result = mapper.map(dataModel)

        // Then
        val expectedEntity = CurrencyEntity(
            id = "ADA",
            name = "Cardano",
            symbol = "ADA",
            code = "   ",
            currencyType = CurrencyTypeLocalModel.FIAT
        )
        assertEquals(expectedEntity, result)
    }
}

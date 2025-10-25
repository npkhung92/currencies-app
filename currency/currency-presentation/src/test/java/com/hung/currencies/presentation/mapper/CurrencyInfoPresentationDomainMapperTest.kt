package com.hung.currencies.presentation.mapper

import com.hung.currencies.domain.model.CurrencyInfoDomainModel
import com.hung.currencies.presentation.model.CurrencyInfoPresentationModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyInfoPresentationDomainMapperTest {

    private lateinit var mapper: CurrencyInfoPresentationDomainMapper

    @Before
    fun setup() {
        mapper = CurrencyInfoPresentationDomainMapper()
    }

    @Test
    fun `map should correctly map Crypto domain model to Crypto presentation model`() {
        // Given
        val cryptoDomainModel = CurrencyInfoDomainModel.Crypto(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC"
        )

        // When
        val result = mapper.map(cryptoDomainModel)

        // Then
        assert(result is CurrencyInfoPresentationModel.Crypto)
        val cryptoPresentationModel = result as CurrencyInfoPresentationModel.Crypto
        assertEquals("bitcoin", cryptoPresentationModel.id)
        assertEquals("Bitcoin", cryptoPresentationModel.name)
        assertEquals("BTC", cryptoPresentationModel.symbol)
    }

    @Test
    fun `map should correctly map Fiat domain model to Fiat presentation model`() {
        // Given
        val fiatDomainModel = CurrencyInfoDomainModel.Fiat(
            id = "usd",
            name = "US Dollar",
            symbol = "$",
            code = "USD"
        )

        // When
        val result = mapper.map(fiatDomainModel)

        // Then
        assert(result is CurrencyInfoPresentationModel.Fiat)
        val fiatPresentationModel = result as CurrencyInfoPresentationModel.Fiat
        assertEquals("usd", fiatPresentationModel.id)
        assertEquals("US Dollar", fiatPresentationModel.name)
        assertEquals("$", fiatPresentationModel.symbol)
        assertEquals("USD", fiatPresentationModel.code)
    }
}
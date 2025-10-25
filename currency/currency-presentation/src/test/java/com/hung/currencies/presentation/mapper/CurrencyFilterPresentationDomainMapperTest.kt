package com.hung.currencies.presentation.mapper

import com.hung.currencies.domain.model.CurrencyTypeDomainModel
import com.hung.currencies.presentation.model.CurrencyFilterPresentationModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CurrencyFilterPresentationDomainMapperTest {

    private lateinit var mapper: CurrencyFilterPresentationDomainMapper

    @Before
    fun setUp() {
        mapper = CurrencyFilterPresentationDomainMapper()
    }

    @Test
    fun `map FIAT presentation model should return FIAT domain model`() {
        // Given
        val presentationModel = CurrencyFilterPresentationModel.FIAT

        // When
        val result = mapper.map(presentationModel)

        // Then
        assertEquals(CurrencyTypeDomainModel.FIAT, result)
    }

    @Test
    fun `map CRYPTO presentation model should return CRYPTO domain model`() {
        // Given
        val presentationModel = CurrencyFilterPresentationModel.CRYPTO

        // When
        val result = mapper.map(presentationModel)

        // Then
        assertEquals(CurrencyTypeDomainModel.CRYPTO, result)
    }

    @Test
    fun `map ALL presentation model should return null`() {
        // Given
        val presentationModel = CurrencyFilterPresentationModel.ALL

        // When
        val result = mapper.map(presentationModel)

        // Then
        assertNull(result)
    }
}
package com.hung.currencies.data.mapper

import com.hung.currencies.datasource.CurrencyTypeLocalModel
import com.hung.currencies.domain.model.CurrencyTypeDomainModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyTypeDomainLocalMapperTest {

    private lateinit var mapper: CurrencyTypeDomainLocalMapper

    @Before
    fun setUp() {
        mapper = CurrencyTypeDomainLocalMapper()
    }

    @Test
    fun `map should return FIAT local model when domain model is FIAT`() {
        // Given
        val domainModel = CurrencyTypeDomainModel.FIAT

        // When
        val result = mapper.map(domainModel)

        // Then
        assertEquals(CurrencyTypeLocalModel.FIAT, result)
    }

    @Test
    fun `map should return CRYPTO local model when domain model is CRYPTO`() {
        // Given
        val domainModel = CurrencyTypeDomainModel.CRYPTO

        // When
        val result = mapper.map(domainModel)

        // Then
        assertEquals(CurrencyTypeLocalModel.CRYPTO, result)
    }
}
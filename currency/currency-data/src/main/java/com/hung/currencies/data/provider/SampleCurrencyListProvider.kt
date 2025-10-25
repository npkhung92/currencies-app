package com.hung.currencies.data.provider

import android.content.Context
import com.hung.currencies.data.mapper.CurrencyDataEntityMapper
import com.hung.currencies.data.model.CurrencyDataModel
import com.hung.currencies.datasource.currency.CurrencyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class SampleCurrencyListProvider(
    val json: Json,
    val context: Context,
    val mapper: CurrencyDataEntityMapper
) {
    suspend fun provide(): List<CurrencyEntity> {
        val currencies = withContext(Dispatchers.Default) {
            val cryptoRead = async(Dispatchers.IO) {
                context.assets.open("crypto-currency.json").bufferedReader().use { it.readText() }
            }
            val fiatRead = async(Dispatchers.IO) {
                context.assets.open("fiat-currency.json").bufferedReader().use { it.readText() }
            }
            json.decodeFromString<List<CurrencyDataModel>>(
                cryptoRead.await()
            ) + json.decodeFromString<List<CurrencyDataModel>>(
                fiatRead.await()
            )
        }
        return currencies.map { mapper.map(it) }
    }
}
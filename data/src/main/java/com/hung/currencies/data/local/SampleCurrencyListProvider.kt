package com.hung.currencies.data.local

import com.hung.currencies.datasource.CurrencyEntity
import com.hung.currencies.datasource.CurrencyTypeLocalModel

class SampleCurrencyListProvider {
    fun provide(): List<CurrencyEntity> = listOf(
        // Crypto currencies
        CurrencyEntity(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "ETH",
            name = "Ethereum",
            symbol = "ETH",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "XRP",
            name = "XRP",
            symbol = "XRP",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "BCH",
            name = "Bitcoin Cash",
            symbol = "BCH",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "LTC",
            name = "Litecoin",
            symbol = "LTC",
            code = "LTC",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "EOS",
            name = "EOS",
            symbol = "EOS",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "BNB",
            name = "Binance Coin",
            symbol = "BNB",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "LINK",
            name = "Chainlink",
            symbol = "LINK",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "NEO",
            name = "NEO",
            symbol = "NEO",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "ETC",
            name = "Ethereum Classic",
            symbol = "ETC",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "ONT",
            name = "Ontology",
            symbol = "ONT",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "CRO",
            name = "Crypto.com Chain",
            symbol = "CRO",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "CUC",
            name = "Cucumber",
            symbol = "CUC",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),
        CurrencyEntity(
            id = "USDC",
            name = "USD Coin",
            symbol = "USDC",
            currencyType = CurrencyTypeLocalModel.CRYPTO
        ),

        // Fiat currencies
        CurrencyEntity(
            id = "SGD",
            name = "Singapore Dollar",
            symbol = "$",
            code = "SGD",
            currencyType = CurrencyTypeLocalModel.FIAT
        ),
        CurrencyEntity(
            id = "EUR",
            name = "Euro",
            symbol = "€",
            code = "EUR",
            currencyType = CurrencyTypeLocalModel.FIAT
        ),
        CurrencyEntity(
            id = "GBP",
            name = "British Pound",
            symbol = "£",
            code = "GBP",
            currencyType = CurrencyTypeLocalModel.FIAT
        ),
        CurrencyEntity(
            id = "HKD",
            name = "Hong Kong Dollar",
            symbol = "$",
            code = "HKD",
            currencyType = CurrencyTypeLocalModel.FIAT
        ),
        CurrencyEntity(
            id = "JPY",
            name = "Japanese Yen",
            symbol = "¥",
            code = "JPY",
            currencyType = CurrencyTypeLocalModel.FIAT
        ),
        CurrencyEntity(
            id = "AUD",
            name = "Australian Dollar",
            symbol = "$",
            code = "AUD",
            currencyType = CurrencyTypeLocalModel.FIAT
        ),
        CurrencyEntity(
            id = "USD",
            name = "United States Dollar",
            symbol = "$",
            code = "USD",
            currencyType = CurrencyTypeLocalModel.FIAT
        )
    )
}
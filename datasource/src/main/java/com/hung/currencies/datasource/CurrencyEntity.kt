package com.hung.currencies.datasource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "code") val code: String? = null,
    @ColumnInfo(name = "currency_type") val currencyType: CurrencyTypeLocalModel
)

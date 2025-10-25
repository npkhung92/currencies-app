package com.hung.currencies.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hung.currencies.datasource.currency.CurrencyDao
import com.hung.currencies.datasource.currency.CurrencyEntity
import com.hung.currencies.datasource.currency.CurrencyTypeConverter

@Database(
    entities = [CurrencyEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CurrencyTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
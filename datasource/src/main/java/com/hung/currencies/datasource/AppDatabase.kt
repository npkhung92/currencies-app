package com.hung.currencies.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CurrencyEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CurrencyTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
package com.hung.currencies.datasource.di

import android.content.Context
import androidx.room.Room
import com.hung.currencies.datasource.AppDatabase
import com.hung.currencies.datasource.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    @Singleton
    @Provides
    fun providesCurrencyDao(db: AppDatabase): CurrencyDao = db.currencyDao()
}
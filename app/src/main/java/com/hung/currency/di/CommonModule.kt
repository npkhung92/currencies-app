package com.hung.currency.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    fun providesJsonSerializer(): Json =
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            explicitNulls = false
            coerceInputValues = true
        }

}
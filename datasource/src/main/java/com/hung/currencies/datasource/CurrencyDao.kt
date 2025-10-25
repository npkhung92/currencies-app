package com.hung.currencies.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency")
    suspend fun getAllCurrencies(): List<CurrencyEntity>

    @Query("""
        SELECT * FROM currency 
        WHERE (name LIKE :searchText || '%' 
        OR name LIKE '% ' || :searchText || '%'
        OR symbol LIKE :searchText || '%')
        AND (:currencyType IS NULL OR currency_type = :currencyType)
    """)
    fun getCurrencies(searchText: String, currencyType: CurrencyTypeLocalModel? = null): Flow<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg currency: CurrencyEntity)

    @Query("DELETE FROM currency")
    suspend fun deleteAll()
}
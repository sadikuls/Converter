package com.sadikul.currencyexchange.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sadikul.currencyexchange.data.local.db.entity.CurrencyEntity

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CurrencyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll( entity: List<CurrencyEntity>)

    @Query("SELECT * FROM currencies order by currency asc")
    suspend fun getAllCurrency(): List<CurrencyEntity>

    @Query("SELECT value FROM currencies where currency = :currencyName")
    fun getCurrencyvalue(currencyName:String): Double
}
package com.sadikul.currencyexchange.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity
import com.sadikul.currencyexchange.data.local.db.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountBalanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CurrencyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll( entities: List<AccountBalanceEntity>)

    @Query("UPDATE account_balance SET balance = :balance WHERE currency = :currency")
    suspend fun updateBalance(currency: String, balance: Double)

    @Query("SELECT * FROM account_balance order by balance desc")
    fun getBalanceList(): Flow<List<AccountBalanceEntity>>

    @Query("SELECT * FROM account_balance order by balance desc")
    suspend fun getBalanceListWithoutFlow(): List<AccountBalanceEntity>

    @Query("SELECT count(currency) FROM account_balance")
    suspend fun getItemCount(): Int

    @Query("SELECT balance FROM account_balance where currency = :currencyName")
    suspend fun getBalance(currencyName:String): Double

}
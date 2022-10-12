package com.sadikul.currencyexchange.domain.repository
import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel
import kotlinx.coroutines.flow.Flow

interface BalanceCalculatorRepo {
    suspend fun getAccountBalances(): Flow<List<AccountBalanceEntity>>
    suspend fun getBalanceList(): List<AccountBalanceEntity>
    suspend fun updateBalance(currency: CurrencyBalanceModel)
    suspend fun getNumberOfConversion(): Int
    suspend fun saveConversionCount(count: Int)
    suspend fun getTotalConvertedAmount(): Double
    suspend fun saveConvertedAmount(amount: Double)
    suspend fun getBalance(currencyName: String): Double
    suspend fun initializeBalance(currencies: List<Currency>)
    suspend fun getCount():Int
}
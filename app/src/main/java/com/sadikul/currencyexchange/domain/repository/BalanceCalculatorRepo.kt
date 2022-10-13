package com.sadikul.currencyexchange.domain.repository
import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel
import kotlinx.coroutines.flow.Flow

interface BalanceCalculatorRepo {
    suspend fun getAccountBalances(): Flow<List<AccountBalanceEntity>>
    suspend fun getBalanceList(): List<AccountBalanceEntity>
    suspend fun updateBalance(currency: CurrencyBalanceModel)
    suspend fun getCurrencyDetails(currencyName: String): CurrencyBalanceModel
    suspend fun initializeBalance(currencies: List<Currency>, currenciesToSetDefaultValue: List<String>, initialBalance: Double)
    suspend fun getCount():Int
}
package com.sadikul.currencyexchange.data.repository
import com.sadikul.currencyexchange.data.local.Preference.PreferenceManager
import com.sadikul.currencyexchange.data.local.db.AppDatabase
import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity
import com.sadikul.currencyexchange.data.local.db.entity.toBalanceModel
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel
import com.sadikul.currencyexchange.domain.model.toEntity
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * params
 * appDatabase : Instance of local database injected by hilt
 *
 */
class BalanceCalculatorRepoImpl @Inject constructor(private val appDatabase: AppDatabase, private val appPreference: PreferenceManager) :
    BalanceCalculatorRepo {
    private val mTag = "BalanceCalculatorRepoImpl"

    override suspend fun getAccountBalances(): Flow<List<AccountBalanceEntity>> = appDatabase.balanceDao().getBalanceList()
    override suspend fun getBalanceList(): List<AccountBalanceEntity> = appDatabase.balanceDao().getBalanceListWithoutFlow()
    override suspend fun insertBalance(currency: CurrencyBalanceModel) {
        appDatabase.balanceDao().insert(currency.toEntity())
    }

    override suspend fun updateBalance(currency: CurrencyBalanceModel) = appDatabase.balanceDao()
        .updateBalance(
            currency.currency,
            currency.balance,
            currency.soldAmount,
            currency.conversionCount
        )

    override suspend fun getCurrencyDetails(currencyName: String): AccountBalanceEntity? {
        return appDatabase.balanceDao().getBalance(currencyName)
    }

    override suspend fun initializeBalance() {
        appDatabase.balanceDao().insert(AccountBalanceEntity("EUR",1000.0,0.0,0))
    }

    override suspend fun getCount() = appDatabase.balanceDao().getItemCount()
}
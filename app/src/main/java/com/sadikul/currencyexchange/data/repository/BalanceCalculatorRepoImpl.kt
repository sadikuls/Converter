package com.sadikul.currencyexchange.data.repository
import com.sadikul.currencyexchange.data.local.Preference.PreferenceManager
import com.sadikul.currencyexchange.data.local.db.AppDatabase
import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel
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

    override suspend fun updateBalance(currency: CurrencyBalanceModel) = appDatabase.balanceDao().updateBalance(currency.currency,currency.balance)

    override suspend fun getNumberOfConversion() = appPreference.numberOfConversion

    override suspend fun saveConversionCount(count: Int) {
        appPreference.numberOfConversion += count
    }

    override suspend fun getTotalConvertedAmount(): Double {
        appPreference.totalConvertedAmount?.let {
                return it
        }
        return 0.0
    }

    override suspend fun saveConvertedAmount(amount: Double) {
        appPreference.totalConvertedAmount?.let {
            appPreference.totalConvertedAmount = it+amount
        }
    }

    override suspend fun getBalance(currencyName: String): Double = appDatabase.balanceDao().getBalance(currencyName)

    override suspend fun initializeBalance(currencies: List<Currency>) {
        var balanceList = currencies.map { currency ->
            AccountBalanceEntity(
                currency = currency.currencyName,
                balance = 0.0
            )
        }
        appDatabase.balanceDao().apply {
            val listToInsert = balanceList.filterNot { it.currency == "EUR" }.toMutableList()
            listToInsert.add(AccountBalanceEntity("EUR",1000.0))
            insertAll(listToInsert)
        }
    }

    override suspend fun getCount() = appDatabase.balanceDao().getItemCount()
}
package com.sadikul.currencyexchange.data.repository
import com.sadikul.currencyexchange.data.local.Preference.PreferenceManager
import com.sadikul.currencyexchange.data.local.db.AppDatabase
import com.sadikul.currencyexchange.data.local.db.dao.AccountBalanceDao
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
class BalanceCalculatorRepoImpl @Inject constructor(private val balanceDao: AccountBalanceDao) :
    BalanceCalculatorRepo {
    private val mTag = "BalanceCalculatorRepoImpl"

    override suspend fun getAccountBalances(): Flow<List<AccountBalanceEntity>> = balanceDao.getBalanceList()
    override suspend fun getBalanceList(): List<AccountBalanceEntity> = balanceDao.getBalanceListWithoutFlow()
    override suspend fun insertBalance(currency: CurrencyBalanceModel) {
        balanceDao.insert(currency.toEntity())
    }

    override suspend fun updateBalance(currency: CurrencyBalanceModel) = balanceDao
        .updateBalance(
            currency.currency,
            currency.balance,
            currency.soldAmount,
            currency.conversionCount
        )

    override suspend fun getCurrencyDetails(currencyName: String): AccountBalanceEntity? {
        return balanceDao.getBalance(currencyName)
    }

    override suspend fun initializeBalance() {
        balanceDao.insert(AccountBalanceEntity("EUR",1000.0,0.0,0))
    }

    override suspend fun getCount() = balanceDao.getItemCount()
}
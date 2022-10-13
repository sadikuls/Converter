package com.sadikul.currencyexchange.data.repository

import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity
import com.sadikul.currencyexchange.data.local.db.entity.toBalanceModel
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel
import com.sadikul.currencyexchange.domain.model.toEntity
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBalanceCalculatorRepoImpl : BalanceCalculatorRepo {

    val balancelist = mutableListOf<AccountBalanceEntity>()
    var numberOfonversion = 0
    var totalConvertedAmount: Double = 0.0

    override suspend fun getAccountBalances(): Flow<List<AccountBalanceEntity>> = flow{
        emit(balancelist)
    }

    override suspend fun getBalanceList(): List<AccountBalanceEntity> {
        return balancelist
    }

    override suspend fun updateBalance(item: CurrencyBalanceModel) {
        balancelist.removeIf { it.currency == item.currency }
        balancelist.add(item.toEntity())
    }

    override suspend fun getCurrencyDetails(currencyName: String): CurrencyBalanceModel {
        return balancelist.find { it.currency == currencyName }?.toBalanceModel() ?: CurrencyBalanceModel(currency = currencyName)
    }

    override suspend fun initializeBalance(currencies: List<Currency>, currenciesToSetDefaultValue: List<String>, initialBalance: Double) {
        var items = currencies.map { currency ->
            AccountBalanceEntity(
                currency = currency.currencyName,
                balance = if(currenciesToSetDefaultValue.contains(currency.currencyName))  initialBalance else 0.0,
                conversionCount = 0,
                soldAmount = 0.0
            )
        }
/*        val listToInsert = items.filterNot { it.currency == "EUR" }.toMutableList()
        listToInsert.add(AccountBalanceEntity("EUR",1000.0))*/
        balancelist.addAll(items)
    }

    override suspend fun getCount(): Int {
        return balancelist.size
    }
}

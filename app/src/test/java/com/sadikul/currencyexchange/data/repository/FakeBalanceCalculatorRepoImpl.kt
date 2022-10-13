package com.sadikul.currencyexchange.data.repository

import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel
import com.sadikul.currencyexchange.domain.model.toEntity
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

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

    override suspend fun getNumberOfConversion(): Int {
        return numberOfonversion
    }

    override suspend fun saveConversionCount(count: Int) {
        numberOfonversion+= count
    }

    override suspend fun getTotalConvertedAmount(): Double {
        return totalConvertedAmount
    }

    override suspend fun saveConvertedAmount(amount: Double) {
        totalConvertedAmount+= amount
    }

    override suspend fun getBalance(currencyName: String): Double {
        val balance = balancelist.find { it.currency == currencyName }?.balance
        if(balance != null) return balance
        return 0.0
    }

    override suspend fun initializeBalance(currencies: List<Currency>) {
        var items = currencies.map { currency ->
            AccountBalanceEntity(
                currency = currency.currencyName,
                balance = 0.0
            )
        }

        val listToInsert = items.filterNot { it.currency == "EUR" }.toMutableList()
        listToInsert.add(AccountBalanceEntity("EUR",1000.0))
        balancelist.addAll(listToInsert)
    }

    override suspend fun getCount(): Int {
        return balancelist.size
    }
}

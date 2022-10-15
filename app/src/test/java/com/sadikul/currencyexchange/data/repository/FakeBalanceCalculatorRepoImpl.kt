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

    override suspend fun getAccountBalances(): Flow<List<AccountBalanceEntity>> = flow{
        emit(balancelist)
    }

    override suspend fun getBalanceList(): List<AccountBalanceEntity> {
        return balancelist
    }

    override suspend fun insertBalance(currency: CurrencyBalanceModel) {
        balancelist.add(currency.toEntity())
    }

    override suspend fun updateBalance(item: CurrencyBalanceModel) {
        balancelist.removeIf { it.currency == item.currency }
        balancelist.add(item.toEntity())
    }

    override suspend fun getCurrencyDetails(currencyName: String): AccountBalanceEntity? {
        return balancelist.find { it.currency == currencyName }
    }

    override suspend fun initializeBalance() {
        balancelist.add(AccountBalanceEntity("EUR",1000.0,0.0,0))
    }

    override suspend fun getCount(): Int {
        return balancelist.size
    }
}

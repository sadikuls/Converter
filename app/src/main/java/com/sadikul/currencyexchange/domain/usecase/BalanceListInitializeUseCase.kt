package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.core.utils.CURENCIES_FOR_DEFAULT_DATA
import com.sadikul.currencyexchange.core.utils.INITIAL_BALANCE
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import javax.inject.Inject

class BalanceListInitializeUseCase @Inject constructor(private val repo: BalanceCalculatorRepo)
{
    operator  suspend fun invoke(currencyList: List<Currency>){
        if(repo.getCount() == 0) repo.initializeBalance(currencyList, CURENCIES_FOR_DEFAULT_DATA,INITIAL_BALANCE)
    }

}
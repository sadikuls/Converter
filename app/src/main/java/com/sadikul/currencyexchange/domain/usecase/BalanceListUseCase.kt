package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.data.local.db.entity.toBalanceModel
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BalanceListUseCase @Inject constructor(private val repo: BalanceCalculatorRepo)
{
    operator  suspend fun invoke() : Flow<List<CurrencyBalanceModel>> = flow {
        repo.getAccountBalances().collect {
            val list = it.map {
                it.toBalanceModel()
            }
            emit(list)
        }
    }.flowOn(Dispatchers.IO)
}
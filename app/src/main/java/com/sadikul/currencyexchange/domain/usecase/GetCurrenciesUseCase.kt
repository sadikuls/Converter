package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.core.utils.NETWORK_CALL_DELAY
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val repo: CurrencyconversionRepo
) {
    operator suspend fun invoke(): Flow<Resource<List<Currency>>> = flow {
        val localData = repo.getCurrenciesFromLocal()
        if (localData.size > 0) {
            println("local data size ${localData}")
            emit(Resource.Loading(data = localData))
        }
        repo.getCurrencies().collect {
            when (it) {
                is Resource.Error -> {
                    emit(Resource.Error(data = it.data, message = it.message))
                }
                is Resource.Success -> {
                    emit(Resource.Success(it.data))
                }
                else -> {}
            }
        }

    }.flowOn(Dispatchers.IO)
}
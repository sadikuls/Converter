package com.sadikul.currencyexchange.domain.usecase
import android.util.Log
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(private val repo: CurrencyconversionRepo) {

    operator suspend fun invoke(forceToGetRemoteData: Boolean): Flow<Resource<List<Currency>>> = flow {
        val localdata = repo.getCurrenciesFromLocal()
       // Log.i("CurrencyRepo", "size of local data ${localdata.size}")
        if (localdata.size == 0 || forceToGetRemoteData) {
            //Log.w("CurrencyRepo", "Returning data from remote")
            repo.getCurrencies(true).collect {
                when (it) {
                    is Resource.Loading -> {
                        emit(Resource.Loading())
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(message = it.message))
                    }
                    is Resource.Success -> {
                        repo.insertCurrencies(it.data)
                        emit(Resource.Success(it.data))
                    }
                }
            }
        } else {
           // Log.w("CurrencyRepo", "Returning data from local")
            emit(Resource.Success(localdata))
        }

    }
}
package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.core.utils.NETWORK_CALL_DELAY
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(private val repo: CurrencyconversionRepo) {

    operator suspend fun invoke(): Flow<Resource<List<Currency>>> = flow {
        emit(Resource.Loading())
        try {
            while (currentCoroutineContext().isActive){
                repo.getCurrencies(true).collect {
                    when (it) {
                        is Resource.Loading -> {
                            //emit(Resource.Loading())
                        }
                        is Resource.Error -> {
                            emit(Resource.Error(message = it.message))
                        }
                        is Resource.Success -> {
                            repo.insertCurrencies(it.data)
                            emit(Resource.Success(it.data))
                        }
                    }
                    delay(NETWORK_CALL_DELAY)
                }
            }
        }catch (e: HttpException){
            emit(Resource.Error(message = e.localizedMessage ?: "Something went wrong."))
        }catch(e: IOException){
            emit(Resource.Error(data = repo.getCurrenciesFromLocal(), message = "Couldn't reach to server. Check your internet connection."))
        }
    }
/*
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
*/
}
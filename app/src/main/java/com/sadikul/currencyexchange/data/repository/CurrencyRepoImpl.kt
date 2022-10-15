package com.sadikul.currencyexchange.data.repository
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.local.Preference.PreferenceManager
import com.sadikul.currencyexchange.data.local.db.AppDatabase
import com.sadikul.currencyexchange.data.local.db.dao.CurrencyDao
import com.sadikul.currencyexchange.data.local.db.entity.CurrencyEntity
import com.sadikul.currencyexchange.data.local.db.entity.toCurrency
import com.sadikul.currencyexchange.data.remote.ApiService.CurrencyApiService
import com.sadikul.currencyexchange.data.remote.NetworkHelper
import com.sadikul.currencyexchange.data.remote.NetworkHelper.Companion.MESSAGE_SERVER_NOT_REACABLE
import com.sadikul.currencyexchange.data.remote.NetworkHelper.Companion.NETWORK_CHECK_ERROR
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.data.remote.dto.toCurrencyEntity
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

/**
 * params
 * appDatabase : Instance of local database injected by hilt
 *
 */
class CurrencyRepoImpl @Inject constructor(
    private val apiService: CurrencyApiService,
    private val currencyDao: CurrencyDao,
    private val appPreference: PreferenceManager,
    private val networkHelper: NetworkHelper
) : CurrencyconversionRepo {
    //private val mTag = "CurrencyRepoImpl"

    override suspend fun getCurrencies(): Flow<Resource<List<Currency>>> = flow {
        val localData = currencyDao.getAllCurrency().map { it.toCurrency() }
        if(!networkHelper.hasNetwork()){
            emit(Resource.Error(data = localData, message = NETWORK_CHECK_ERROR))
            return@flow
        }
        try {
            val remoteData = apiService.getData()
            val data = remoteData.rates.map {
                CurrencyEntity(it.key, it.value)
            }
            currencyDao.insertAll(data)
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = MESSAGE_SERVER_NOT_REACABLE
                )
            )
        }
        val currencies = currencyDao.getAllCurrency().map { it.toCurrency() }
        emit(Resource.Success(currencies))
    }

    override suspend fun getCurrenciesFromLocal(): List<Currency> {
        return currencyDao.getAllCurrency().map { it.toCurrency() }
    }

    override suspend fun insertCurrencies(currencies: List<Currency>) {
        currencyDao.insertAll(currencies.map { it.toCurrencyEntity() })
    }



    override suspend fun getRate(currencyName: String) =
        currencyDao.getCurrencyvalue(currencyName)

    override suspend fun getFromCurrency(): Currency {
        if(appPreference.fromCurrency == null) {
            appPreference.fromCurrency = Currency("EUR")
        }
        return appPreference.fromCurrency!!
    }

    override suspend fun saveFromCurrency(currency: Currency) {
        appPreference.fromCurrency = currency
    }

    override suspend fun getToCurrency(): Currency {
        if (appPreference.toCurrency == null) {
            appPreference.toCurrency = Currency("USD")
        }
        return appPreference.toCurrency!!
    }

    override suspend fun saveToCurrency(currency: Currency) {
        appPreference.toCurrency = currency
    }
}
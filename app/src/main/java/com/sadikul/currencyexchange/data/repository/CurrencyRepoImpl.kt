package com.sadikul.currencyexchange.data.repository

import android.util.Log
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.core.utils.safeApiCall
import com.sadikul.currencyexchange.data.local.Preference.PreferenceManager
import com.sadikul.currencyexchange.data.local.db.AppDatabase
import com.sadikul.currencyexchange.data.local.db.entity.toCurrency
import com.sadikul.currencyexchange.data.remote.ApiService.CurrencyApiService
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.data.remote.dto.toCurrencyEntity
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

/**
 * params
 * appDatabase : Instance of local database injected by hilt
 *
 */
class CurrencyRepoImpl @Inject constructor(
    private val apiService: CurrencyApiService,
    private val appDatabase: AppDatabase,
    private val appPreference: PreferenceManager
) : CurrencyconversionRepo {
    private val mTag = "CurrencyRepoImpl"


    override suspend fun getCurrencies(latest: Boolean): Flow<Resource<List<Currency>>> =
        safeApiCall {
            apiService.getData()
        }.transform {
            if (it is Resource.Success) {
                val data = it.data.rates.map {
                    Currency(it.key, it.value)
                }
                emit(Resource.Success(data))
            } else emit(it as Resource<List<Currency>>)
        }

    override suspend fun getCurrenciesFromLocal(): List<Currency> {
        return appDatabase.currencyDao().getAllCurrency().map { it.toCurrency() }
    }

    override suspend fun insertCurrencies(currencies: List<Currency>) {
        appDatabase.currencyDao().insertAll(currencies.map { it.toCurrencyEntity() })
    }



    override suspend fun getRate(currencyName: String) =
        appDatabase.currencyDao().getCurrencyvalue(currencyName)

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
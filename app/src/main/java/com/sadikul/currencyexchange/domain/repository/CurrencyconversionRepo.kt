package com.sadikul.currencyexchange.domain.repository
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.remote.dto.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyconversionRepo {
    suspend fun getCurrencies(): Flow<Resource<List<Currency>>>
    suspend fun getCurrenciesFromLocal(): List<Currency>
    suspend fun insertCurrencies(currencies: List<Currency>)
    suspend fun getRate(currencyName:String):Double
    suspend fun getFromCurrency():Currency
    suspend fun saveFromCurrency(currency: Currency)
    suspend fun getToCurrency():Currency
    suspend fun saveToCurrency(currency: Currency)
}
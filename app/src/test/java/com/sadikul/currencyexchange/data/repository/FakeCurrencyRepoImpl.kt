package com.sadikul.currencyexchange.data.repository
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeCurrencyRepoImpl : CurrencyconversionRepo {
    val currencyList = mutableListOf<Currency>()
    var fromCurrency: Currency? = null
    var toCurrency: Currency? = null

    override suspend fun getCurrencies(latest: Boolean): Flow<Resource<List<Currency>>> = flow {
        val list = mutableListOf<Currency>()
        list.add(Currency("EUR", 1.0))
        list.add(Currency("USD", 0.969204))
        list.add(Currency("BDT", 98.546494))
        list.add(Currency("AED", 3.559867))
        list.add(Currency("JPY", 142.374546))
        emit(Resource.Success(list.toList()))
    }.flowOn(Dispatchers.IO)

    override suspend fun getCurrenciesFromLocal(): List<Currency> {
        return currencyList
    }

    override suspend fun insertCurrencies(currencies: List<Currency>) {
        currencyList.addAll(currencies)
    }

    override suspend fun getRate(currencyName: String): Double {
        return currencyList.find { it.currencyName == currencyName }?.currencyValue ?: 0.0
    }

    override suspend fun getFromCurrency(): Currency {
        return fromCurrency ?: Currency("EUR")
    }

    override suspend fun saveFromCurrency(currency: Currency) {
        fromCurrency = currency
    }

    override suspend fun getToCurrency(): Currency {
        return toCurrency ?: Currency("USD")
    }

    override suspend fun saveToCurrency(currency: Currency) {
        toCurrency = currency
    }
}

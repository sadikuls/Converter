package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import javax.inject.Inject

class SaveToCurrencyUseCase @Inject constructor(private val currencyRepoImpl: CurrencyconversionRepo)
{
    operator suspend fun invoke(toCurrency: Currency) = currencyRepoImpl.saveToCurrency(toCurrency)
}
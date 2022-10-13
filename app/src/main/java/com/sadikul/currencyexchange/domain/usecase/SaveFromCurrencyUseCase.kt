package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import javax.inject.Inject

class SaveFromCurrencyUseCase @Inject constructor(private val currencyconversionRepo: CurrencyconversionRepo)
{
    operator suspend fun invoke(toCurrency: Currency) = currencyconversionRepo.saveFromCurrency(toCurrency)

}
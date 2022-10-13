package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import javax.inject.Inject

class GetFromCurrencyUseCase @Inject constructor(private val currencyconversionRepo: CurrencyconversionRepo)
{
    operator suspend fun invoke() = currencyconversionRepo.getFromCurrency()
}
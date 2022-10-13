package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.data.local.Preference.PreferenceManager
import javax.inject.Inject

class GetToCurrencyUseCase @Inject constructor(private val appPreference: PreferenceManager)
{
    operator fun invoke() = appPreference.toCurrency
}
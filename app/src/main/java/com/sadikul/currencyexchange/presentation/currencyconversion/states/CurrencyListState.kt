package com.sadikul.currencyexchange.presentation.currencyconversion.states
import com.sadikul.currencyexchange.data.remote.dto.Currency

data class CurrencyListState(
    val items: List<Currency> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

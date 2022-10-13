package com.sadikul.currencyexchange.presentation.currencyconversion.states
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel

data class BalanceListState(
    val balanceList: List<CurrencyBalanceModel> = emptyList(),
)

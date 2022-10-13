package com.sadikul.currencyexchange.presentation.currencyconversion.states

data class ValidatorState(
    val isValid: Boolean = false,
    val msg: String = ""
)

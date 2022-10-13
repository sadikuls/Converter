package com.sadikul.currencyexchange.presentation.currencyconversion

data class ConversionModel(
    val fromCurrency:String,
    val toCurrency: String,
    val fromAmount: Double,
    val convertedAmount: Double,
    var commission:Double = 0.0
)

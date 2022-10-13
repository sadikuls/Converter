package com.sadikul.currencyexchange.domain.model

data class ConversionModel(
    val fromCurrency:String,
    val toCurrency: String,
    val fromAmount: Double,
    val convertedAmount: Double = 0.0,
    var commission:Double = 0.0
)

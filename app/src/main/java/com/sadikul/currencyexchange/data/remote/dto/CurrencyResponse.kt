package com.sadikul.currencyconverter.data.remote.dto

import java.io.Serializable

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
    val success: Boolean,
    val timestamp: Int
) : Serializable
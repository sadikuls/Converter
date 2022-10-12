package com.sadikul.currencyexchange.data.remote.dto
import com.sadikul.currencyexchange.data.local.db.entity.CurrencyEntity

class Currency(
    val currencyName: String,
    val currencyValue: Double = 0.0
)

fun Currency.toCurrencyEntity() : CurrencyEntity = CurrencyEntity(currency = currencyName, value = currencyValue)
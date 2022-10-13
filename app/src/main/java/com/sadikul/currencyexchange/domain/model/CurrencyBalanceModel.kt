package com.sadikul.currencyexchange.domain.model

import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity

class CurrencyBalanceModel(
    val currency: String = "",
    val balance: Double = 0.0,
    val soldAmount: Double = 0.0,
    val conversionCount: Int = 0
)

fun CurrencyBalanceModel.toEntity() = AccountBalanceEntity(
    currency = currency,
    balance = balance,
    soldAmount = soldAmount,
    conversionCount = conversionCount
)
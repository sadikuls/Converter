package com.sadikul.currencyexchange.domain.model

import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity

class CurrencyBalanceModel(
    val currency: String = "",
    val balance: Double = 0.0
)

fun CurrencyBalanceModel.toEntity()  = AccountBalanceEntity(currency= currency, balance = balance)
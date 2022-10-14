package com.sadikul.currencyexchange.presentation.currencyconversionscreen.states

import com.sadikul.currencyexchange.domain.model.ConversionModel

data class ConversionResultState(
    val conversionModel: ConversionModel?= null,
    val message: String? = ""
)

package com.sadikul.currencyexchange.presentation.currencyconversion.states

import com.sadikul.currencyexchange.domain.model.ConversionModel

data class ConversionCalculationState(
    val conversionData: ConversionModel? = null,
    val message: String ? = null
)

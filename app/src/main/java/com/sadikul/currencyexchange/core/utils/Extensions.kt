package com.sadikul.currencyexchange.core.utils

fun String.generateBase(): String{
    val prefix = "https://developers."
    val suffix = ".com"
    val middle = "pandaandyandsandeandranda"
    return prefix + middle.replace("and","")+ suffix
}

fun Double.Companion.stringValueUptoTwoDecimalPlace(amount:Double): String = String.format("%.2f",amount)
fun Double.Companion.stringValueUptoFourDecimalPlace(amount:Double): String = String.format("%.4f",amount)
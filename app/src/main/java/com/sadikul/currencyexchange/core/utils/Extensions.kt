package com.sadikul.currencyexchange.core.utils

fun String.generateBase(): String{
    val prefix = "https://developers."
    val suffix = ".com"
    val middle = "pandaandyandsandeandranda"
    return prefix + middle.replace("and","")+ suffix
}
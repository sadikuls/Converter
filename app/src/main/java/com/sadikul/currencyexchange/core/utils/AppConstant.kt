package com.sadikul.currencyexchange.core.utils


//const val APP_VERSION = "Android/${BuildConfig.VERSION_NAME}"
const val CONNECT_TIMEOUT_S: Long = 60
const val READ_TIMEOUT_S: Long = 60
const val WRITE_TIMEOUT_S: Long = 60

const val APP_PREF = "Android_Pref_Currency"
const val BASE_URL = "https://api.apilayer.com"
const val ENDPOINT_LATEST = "/tasks/api/currency-exchange-rates"
//const val API_KEY = "HBhvWzrB3TBTh6k4C3nK6ybfggmoRrn4"
val CURENCIES_FOR_DEFAULT_DATA = listOf("EUR")
const val INITIAL_BALANCE = 1000.0
const val NETWORK_CALL_DELAY = 5 * 1000L

//Applicable for every currency
const val COMMISSION_RATE = 0.7
const val MAX_FREE_CONVERSION_AMOUNT = 0.0
//Applicable for every currency
const val MAX_FREE_CONVERSION = 5
const val CURRENCY_EURO = "EUR"
//Applicable only for euro
const val MAX_FREE_AMOUNT_FOR_EURO = 200.0

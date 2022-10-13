package com.sadikul.currencyexchange.core.utils

internal const val HEADER_AUTHORIZATION = "Authorization"
const val HEADER_CONTENT_TYPE = "Content-Type"
const val HEADER_CONTENT_TYPE_VALUE = "application/json"
const val HEADER_AUTHORIZATION_TYPE = "Bearer "
const val HEADER_PLATFORM = "x-device-platform"
const val PLATFORM = "ANDROID"
const val HEADER_APP_VERSION = "X-APP-VERSION"
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

const val COMMISSION_RATE = 0.7
const val MAX_FREE_CONVERSION_AMOUNT = 200.0
const val MAX_FREE_CONVERSION = 5

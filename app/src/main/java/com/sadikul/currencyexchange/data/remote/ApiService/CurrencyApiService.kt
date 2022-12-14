package com.sadikul.currencyexchange.data.remote.ApiService
import com.sadikul.currencyconverter.data.remote.dto.CurrencyResponse
import com.sadikul.currencyexchange.core.utils.ENDPOINT_LATEST
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApiService {
    @GET(ENDPOINT_LATEST)
    suspend fun getData(): CurrencyResponse
}
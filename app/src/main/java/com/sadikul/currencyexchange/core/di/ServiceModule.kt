package com.sadikul.currencyexchange.core.di

import com.sadikul.currencyexchange.data.remote.ApiService.CurrencyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): CurrencyApiService = retrofit.create(CurrencyApiService::class.java)
}
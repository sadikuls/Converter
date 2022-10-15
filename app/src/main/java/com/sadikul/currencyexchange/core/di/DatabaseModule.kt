package com.sadikul.currencyexchange.core.di

import android.content.Context
import com.sadikul.currencyexchange.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context = context)


    @Singleton
    @Provides
    fun provideCurrencyDao(database: AppDatabase) = database.currencyDao()

    @Singleton
    @Provides
    fun provideBalanceDao(database: AppDatabase) = database.balanceDao()

}
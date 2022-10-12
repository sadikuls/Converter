package com.sadikul.currencyexchange.core.di

import android.content.Context
import com.google.gson.Gson
import com.sadikul.currencyexchange.data.local.Preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context,gson:Gson): PreferenceManager =
        PreferenceManager(context = context, gson = gson)

}
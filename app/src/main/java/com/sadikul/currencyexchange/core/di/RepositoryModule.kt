package com.sadikul.currencyexchange.core.di
import com.sadikul.currencyexchange.data.local.Preference.PreferenceManager
import com.sadikul.currencyexchange.data.local.db.AppDatabase
import com.sadikul.currencyexchange.data.remote.ApiService.CurrencyApiService
import com.sadikul.currencyexchange.data.remote.NetworkHelper
import com.sadikul.currencyexchange.data.repository.BalanceCalculatorRepoImpl
import com.sadikul.currencyexchange.data.repository.CurrencyRepoImpl
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(apiService: CurrencyApiService, appDatabase: AppDatabase, appPreference: PreferenceManager,networkHelper: NetworkHelper): CurrencyconversionRepo =
        CurrencyRepoImpl(apiService,appDatabase, appPreference,networkHelper)

    @Provides
    @Singleton
    fun provideBalanceCalculatorRepository(appDatabase: AppDatabase,appPreference: PreferenceManager): BalanceCalculatorRepo =
        BalanceCalculatorRepoImpl(appDatabase,appPreference)
}
package com.sadikul.currencyexchange.core.di
import com.sadikul.currencyexchange.data.local.Preference.PreferenceManager
import com.sadikul.currencyexchange.data.local.db.AppDatabase
import com.sadikul.currencyexchange.data.local.db.dao.AccountBalanceDao
import com.sadikul.currencyexchange.data.local.db.dao.CurrencyDao
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
    fun provideCurrencyRepository(apiService: CurrencyApiService, currencyDao: CurrencyDao, appPreference: PreferenceManager,networkHelper: NetworkHelper): CurrencyconversionRepo =
        CurrencyRepoImpl(apiService,currencyDao, appPreference,networkHelper)

    @Provides
    @Singleton
    fun provideBalanceCalculatorRepository(balanceDao: AccountBalanceDao): BalanceCalculatorRepo =
        BalanceCalculatorRepoImpl(balanceDao)
}
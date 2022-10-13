package com.sadikul.currencyexchange.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.data.repository.FakeBalanceCalculatorRepoImpl
import com.sadikul.currencyexchange.data.repository.FakeCurrencyRepoImpl
import com.sadikul.currencyexchange.presentation.currencyconversion.ConversionModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyCalculatorUseCaseTest{
    private lateinit var useCase: CurrencyCalculatorUseCase
    lateinit var currencyrepo: FakeCurrencyRepoImpl
    val errorMessage = "For calculation, Wrong data is not acceptable"
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        currencyrepo = FakeCurrencyRepoImpl()
        runBlocking {
            val currencies = currencyrepo.getCurrencies(true).first()
            currencyrepo.insertCurrencies((currencies as Resource.Success).data)
            useCase = CurrencyCalculatorUseCase(currencyrepo)
        }
    }

    @Test
    fun testCurrenciesIsInserted() = runBlocking{
        val repoData = currencyrepo.getCurrenciesFromLocal()
        println("size of the data :${repoData.size}")
        assertTrue(repoData.size > 0)
        for(item in repoData){
            assertTrue( item.currencyValue > 0)
        }
    }

    @Test
    fun currencyCalculationCheck() = runBlocking{
        useCase("EUR", "USD", 100.0).collect(object :
            FlowCollector<Resource<ConversionModel>> {
            override suspend fun emit(value: Resource<ConversionModel>) {
                println("result of conversion ${(value as Resource.Success).data.convertedAmount}")
                (value as Resource.Success).data.apply {
                    assertTrue(fromCurrency == "EUR")
                    assertTrue(toCurrency == "USD")
                    assertTrue(fromAmount == 100.0)
                    assertTrue(convertedAmount == 96.9204)
                    assertTrue(commission == 0.0)
                }
            }
        })
    }

    @Test
    fun currencyCalculationCheckWithWrongFromCurrency() = runBlocking{
        useCase("", "USD", 100.0).collect(object :
            FlowCollector<Resource<ConversionModel>> {
            override suspend fun emit(value: Resource<ConversionModel>) {
                println("result of conversion ${(value as Resource.Error).message}")
                assertTrue((value as Resource.Error).data == null)
                assertTrue((value as Resource.Error).message == errorMessage)
            }
        })
    }

    @Test
    fun currencyCalculationCheckWithWrongToCurrency() = runBlocking{
        useCase("EUR", "", 100.0).collect(object :
            FlowCollector<Resource<ConversionModel>> {
            override suspend fun emit(value: Resource<ConversionModel>) {
                println("result of conversion ${(value as Resource.Error).message}")
                assertTrue((value as Resource.Error).data == null)
                assertTrue((value as Resource.Error).message == errorMessage)
            }
        })
    }

    @Test
    fun currencyCalculationCheckWithWrongCurrencyValue() = runBlocking{
        useCase("EUR", "USD", 0.0).collect(object :
            FlowCollector<Resource<ConversionModel>> {
            override suspend fun emit(value: Resource<ConversionModel>) {
                println("result of conversion ${(value as Resource.Error).message}")
                assertTrue((value as Resource.Error).data == null)
                assertTrue((value as Resource.Error).message == errorMessage)
            }
        })
    }
}
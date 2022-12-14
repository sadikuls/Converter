package com.sadikul.currencyexchange.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.repository.FakeCurrencyRepoImpl
import com.sadikul.currencyexchange.domain.model.ConversionModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(InternalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
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
            val currencies = currencyrepo.getCurrencies().first()
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
                (value).data.apply {
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
                assertTrue((value).data == null)
                assertTrue((value).message == errorMessage)
            }
        })
    }


    @Test
    fun currencyCalculationCheckWithWrongToCurrency() = runBlocking{
        useCase("EUR", "", 100.0).collect(object :
            FlowCollector<Resource<ConversionModel>> {
            override suspend fun emit(value: Resource<ConversionModel>) {
                println("result of conversion ${(value as Resource.Error).message}")
                assertTrue((value).data == null)
                assertTrue((value).message == errorMessage)
            }
        })
    }

    @Test
    fun currencyCalculationCheckWithWrongCurrencyValue() = runBlocking {
        useCase("EUR", "USD", 0.0).collectLatest {
            (object :
                FlowCollector<Resource<ConversionModel>> {
                override suspend fun emit(value: Resource<ConversionModel>) {
                    println("result of conversion ${(value as Resource.Error).message}")
                    assertTrue((value).data == null)
                    assertTrue((value).message == errorMessage)
                }
            })
        }
    }
}
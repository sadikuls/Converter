package com.sadikul.currencyexchange.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sadikul.currencyexchange.core.utils.CURENCIES_FOR_DEFAULT_DATA
import com.sadikul.currencyexchange.core.utils.INITIAL_BALANCE
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.data.repository.FakeBalanceCalculatorRepoImpl
import com.sadikul.currencyexchange.data.repository.FakeCurrencyRepoImpl
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import com.sadikul.currencyexchange.presentation.currencyconversion.ConversionModel
import com.sadikul.currencyexchange.presentation.currencyconversion.ValidatorState
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyConversionValidatorUseCaseTest{
    private lateinit var useCase: CurrencyConversionValidatorUseCase
    lateinit var currencyrepo: FakeCurrencyRepoImpl
    lateinit var balanceRepo: FakeBalanceCalculatorRepoImpl

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        currencyrepo = FakeCurrencyRepoImpl()
        balanceRepo = FakeBalanceCalculatorRepoImpl()
        runBlocking {
            //Get all currency
            val currencies = currencyrepo.getCurrencies(true).first()
            //insert currencies to db
            currencyrepo.insertCurrencies((currencies as Resource.Success).data)
            //initialize balance data
            balanceRepo.initializeBalance(
                (currencies).data, CURENCIES_FOR_DEFAULT_DATA,
                INITIAL_BALANCE
            )

            useCase = CurrencyConversionValidatorUseCase(balanceRepo)
        }
    }

    @Test
    fun testInitailBalanceGenerated() = runBlocking{
        val repoData = balanceRepo.getBalanceList()
        println("size of the data :${repoData.size}")
        assertTrue(repoData.size > 0)
        assertTrue(repoData.sumOf { item -> item.balance } > 0)
        assertTrue(repoData.find { it.currency == "EUR"}?.balance == 1000.0 )
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
    fun validateConversationWithZeroAmount() = runBlocking{
        val conversationModel = ConversionModel(
            fromAmount = 0.0,
            fromCurrency = "EUR",
            toCurrency = "USD"
        )
        useCase(conversationModel).collect(object : FlowCollector<ValidatorState>{
            override suspend fun emit(value: ValidatorState) {
                assertTrue(value.isValid == false)
                assertTrue(value.msg.contains("Invalid amount."))
            }
        })
    }

    @Test
    fun validateAmountMoreThanBalance() = runBlocking{
        val conversationModel = ConversionModel(
            fromAmount = 1000.0,
            fromCurrency = "EUR",
            toCurrency = "USD",
            commission = .7
        )
        useCase(conversationModel).collect(object : FlowCollector<ValidatorState>{
            override suspend fun emit(value: ValidatorState) {
                assertTrue(value.isValid == false)
                assertTrue(value.msg.contains("Insufficient balance."))
            }
        })
    }

    @Test
    fun validateWithSameCurrency() = runBlocking{
        val conversationModel = ConversionModel(
            fromAmount = 1000.0,
            fromCurrency = "EUR",
            toCurrency = "EUR",
            commission = .7
        )
        useCase(conversationModel).collect(object : FlowCollector<ValidatorState>{
            override suspend fun emit(value: ValidatorState) {
                assertTrue(value.isValid == false)
                assertTrue(value.msg.contains("select differenct currency"))
            }
        })
    }

    @Test
    fun checkValidData() = runBlocking{
        val conversationModel = ConversionModel(
            fromAmount = 100.0,
            fromCurrency = "EUR",
            toCurrency = "USD",
            commission = .7
        )
        useCase(conversationModel).collect(object : FlowCollector<ValidatorState>{
            override suspend fun emit(value: ValidatorState) {
                assertTrue(value.isValid == true)
                assertTrue(value.msg.contains("success"))
            }
        })
    }
}
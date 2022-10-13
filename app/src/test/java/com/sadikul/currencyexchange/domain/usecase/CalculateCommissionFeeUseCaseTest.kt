package com.sadikul.currencyexchange.domain.usecase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sadikul.currencyexchange.core.utils.CURENCIES_FOR_DEFAULT_DATA
import com.sadikul.currencyexchange.core.utils.INITIAL_BALANCE
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.repository.FakeBalanceCalculatorRepoImpl
import com.sadikul.currencyexchange.data.repository.FakeCurrencyRepoImpl
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculateCommissionFeeUseCaseTest{
    lateinit var balanceRepo: FakeBalanceCalculatorRepoImpl
    lateinit var currencyrepo: FakeCurrencyRepoImpl
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
    fun calculateCurrencyWhenNoRuleApplied() = runBlocking{
        assertTrue(CommissionFeeCalculatorUseCase(repo = balanceRepo)(10.0, "EUR", .7, 0, 0.0) == 0.07)
    }

    @Test
    fun calculateCurrencyWhenNoRuleAppliedWithdifferentCommissionRate() = runBlocking{
        assertTrue(CommissionFeeCalculatorUseCase(repo = balanceRepo)(10.0, "EUR", 1.7, 0, 0.0) == 0.17)
    }

    @Test
    fun calculateCurrencyWhenAppliedMaxFreeAmountRule() = runBlocking{
        balanceRepo.getCurrencyDetails("EUR").let {
            balanceRepo.updateBalance(
                CurrencyBalanceModel(
                    currency = it.currency,
                    balance = it.balance,
                    soldAmount = it.soldAmount + 10,
                    conversionCount = it.conversionCount + 1
                )
            )
        }
        assertTrue(CommissionFeeCalculatorUseCase(repo = balanceRepo)(20.0, "EUR", 1.7, 0, 20.0) == 0.17)
        balanceRepo.getCurrencyDetails("EUR").let {
            balanceRepo.updateBalance(
                CurrencyBalanceModel(
                    currency = it.currency,
                    balance = it.balance,
                    soldAmount = it.soldAmount + 10,
                    conversionCount = it.conversionCount + 1
                )
            )
        }
        assertTrue(CommissionFeeCalculatorUseCase(repo = balanceRepo)(20.0, "EUR", 1.7, 0, 20.0) == 0.34)
    }


    @Test
    fun calculateCurrencyWhenAppliedFreeConversionRule() = runBlocking{
        assertTrue(CommissionFeeCalculatorUseCase(repo = balanceRepo)(20.0, "EUR", 1.7, 5, 20.0) == 0.0)
           }

    @Test
    fun calculateCurrencyWhenAppliedFreeConversionRuleSameAmount() = runBlocking{
        balanceRepo.getCurrencyDetails("EUR").let {
            balanceRepo.updateBalance(
                CurrencyBalanceModel(
                    currency = it.currency,
                    balance = it.balance,
                    soldAmount = it.soldAmount ,
                    conversionCount = 3
                )
            )
        }
        assertTrue(CommissionFeeCalculatorUseCase(repo = balanceRepo)(20.0, "EUR", 1.7, 3, 0.0) == 0.34)
    }

    @Test
    fun calculateWithZeroAmount() = runBlocking{
        assertTrue(CommissionFeeCalculatorUseCase(repo = balanceRepo)(0.0, "EUR", 1.7, 3, 20.0) == 0.0)
        println("calculated commission = ${CommissionFeeCalculatorUseCase(repo = balanceRepo)(20.0, "EUR", 1.7, 0, 10.0) }")
    }
}
package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import javax.inject.Inject

class CommissionFeeCalculatorUseCase @Inject constructor(private val repo: BalanceCalculatorRepo) {

    operator suspend fun invoke(
        amount: Double,
        currencyName: String,
        rate: Double,
        maxFreeConversion: Int,
        maxFreeAmount: Double
    ): Double {
        val commissionFeeZero = 0.0
        var conversionCount = 0
        var soldAmount = 0.0
        repo.getCurrencyDetails(currencyName)?.let {
            conversionCount = it.conversionCount
            soldAmount = it.soldAmount
        }
        if (amount <= 0.0 || conversionCount < maxFreeConversion) {
            return commissionFeeZero
        } else if (soldAmount <= maxFreeAmount) {
            val amountToApplyCommission =
                amount - (maxFreeAmount - soldAmount)
            if (amountToApplyCommission <= 0.0) return commissionFeeZero
           return rate * amountToApplyCommission / 100
        }
        return rate * amount / 100
    }
}
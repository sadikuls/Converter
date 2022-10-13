package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import javax.inject.Inject

class CommissionFeeCalculatorUseCase @Inject constructor(private val repo: BalanceCalculatorRepo) {
    companion object{
        const val COMMISSION_RATE = 0.7
        const val MAX_FREE_CONVERSION_AMOUNT = 200.0
        const val MAX_FREE_CONVERSION = 5
    }
    operator suspend fun invoke(
        amount: Double,
        currencyName: String,
        rate: Double,
        maxFreeConversion: Int,
        maxFreeAmount: Double
    ): Double {
        val commissionFeeZero = 0.0
        val currencyDetails = repo.getCurrencyDetails(currencyName)
        if (amount <= 0.0 || currencyDetails.conversionCount < maxFreeConversion) {
            return commissionFeeZero
        } else if (currencyDetails.soldAmount <= maxFreeAmount) {
            val amountToApplyCommission =
                amount - (maxFreeAmount - currencyDetails.soldAmount)
            if (amountToApplyCommission <= 0.0) return commissionFeeZero
           return rate * amountToApplyCommission / 100
        }
        return rate * amount / 100
    }
}
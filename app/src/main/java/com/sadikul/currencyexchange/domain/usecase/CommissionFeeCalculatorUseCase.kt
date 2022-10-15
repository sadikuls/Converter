package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.core.utils.COMMISSION_RATE
import com.sadikul.currencyexchange.core.utils.CURRENCY_EURO
import com.sadikul.currencyexchange.core.utils.MAX_FREE_AMOUNT_FOR_EURO
import com.sadikul.currencyexchange.core.utils.MAX_FREE_CONVERSION
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import javax.inject.Inject

class CommissionFeeCalculatorUseCase @Inject constructor(private val repo: BalanceCalculatorRepo) {

    operator suspend fun invoke(
        amount: Double,
        currencyName: String
    ): Double {
        val commissionFeeZero = 0.0
        var conversionCount = 0
        var soldAmount = 0.0
        repo.getCurrencyDetails(currencyName)?.let {
            conversionCount = it.conversionCount
            soldAmount = it.soldAmount
        }
        // Checking for every conversion if the amount is zero or conversion count
        // is less than the max free conversion
        if (amount <= 0.0 || conversionCount < MAX_FREE_CONVERSION) {
            return commissionFeeZero
        }

        // Checking for EURO, if the balance is less than the max free amount
        if(currencyName.equals(CURRENCY_EURO)){
            repo.getCurrencyDetails(CURRENCY_EURO)?.let {
                if (it.soldAmount <= MAX_FREE_AMOUNT_FOR_EURO) {
                    val amountToApplyCommission =
                        amount - (MAX_FREE_AMOUNT_FOR_EURO - it.soldAmount)
                    if (amountToApplyCommission <= 0.0) return commissionFeeZero
                    return COMMISSION_RATE * amountToApplyCommission / 100
                }
            }
        }

        // Condition for every item , which soldamount is less than commission free amount
/*        if (soldAmount <= maxFreeAmount) {
            val amountToApplyCommission =
                amount - (maxFreeAmount - soldAmount)
            if (amountToApplyCommission <= 0.0) return commissionFeeZero
           return rate * amountToApplyCommission / 100
        }
        */
        return COMMISSION_RATE * amount / 100
    }
}
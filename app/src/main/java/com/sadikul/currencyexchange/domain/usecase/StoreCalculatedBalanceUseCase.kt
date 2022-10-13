package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import com.sadikul.currencyexchange.domain.model.ConversionModel
import javax.inject.Inject

class StoreCalculatedBalanceUseCase @Inject constructor(private val repo: BalanceCalculatorRepo)
{
    operator suspend fun invoke(
        conversionModel: ConversionModel
    ) {
        repo.getCurrencyDetails(conversionModel.fromCurrency).apply {
            repo.updateBalance(
                CurrencyBalanceModel(
                    currency = this.currency,
                    balance = this.balance - (conversionModel.fromAmount + conversionModel.commission),
                    soldAmount = this.soldAmount + conversionModel.fromAmount,
                    conversionCount = this.conversionCount
                )
            )
        }
        repo.getCurrencyDetails(conversionModel.toCurrency).apply {
            repo.updateBalance(
                CurrencyBalanceModel(
                    currency = this.currency,
                    balance = balance + conversionModel.convertedAmount,
                    soldAmount = this.soldAmount,
                    conversionCount = this.conversionCount
                )
            )
        }
    }

}
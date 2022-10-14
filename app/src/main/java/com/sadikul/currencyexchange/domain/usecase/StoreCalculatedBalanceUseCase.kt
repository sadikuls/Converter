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
        val fromCurrencyDetails = repo.getCurrencyDetails(conversionModel.fromCurrency)
        val toCurrencyDetails = repo.getCurrencyDetails(conversionModel.toCurrency)

        fromCurrencyDetails?.let{
            repo.updateBalance(
                CurrencyBalanceModel(
                    currency = it.currency,
                    balance = it.balance - (conversionModel.fromAmount + conversionModel.commission),
                    soldAmount = it.soldAmount + conversionModel.fromAmount,
                    conversionCount = it.conversionCount + 1
                )
            )
        }

        if(toCurrencyDetails != null){
            toCurrencyDetails.let {
                repo.updateBalance(
                    CurrencyBalanceModel(
                        currency = it.currency,
                        balance = it.balance + conversionModel.convertedAmount,
                        soldAmount = it.soldAmount,
                        conversionCount = it.conversionCount
                    )
                )
            }
        }else{
            repo.insertBalance(
                CurrencyBalanceModel(
                    currency = conversionModel.toCurrency,
                    balance = conversionModel.convertedAmount,
                    soldAmount = 0.0,
                    conversionCount = 0
                )
            )

        }
    }

}
package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.domain.repository.CurrencyconversionRepo
import com.sadikul.currencyexchange.domain.model.ConversionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyCalculatorUseCase @Inject constructor(private val currencyRepo: CurrencyconversionRepo)
{
    operator suspend fun invoke(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Flow<Resource<ConversionModel>> = flow {
        try {
            if(fromCurrency.isEmpty() || toCurrency.isEmpty() || amount <= 0.0){
                emit(Resource.Error(message = "For calculation, Wrong data is not acceptable"))
                return@flow
            }
            val sourceCurrencyValue = currencyRepo.getRate(fromCurrency)
            val targetCurrencyValue = currencyRepo.getRate(toCurrency)
            val convertedAmount = (targetCurrencyValue / sourceCurrencyValue) * amount
            val data = ConversionModel(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                fromAmount = amount,
                convertedAmount = convertedAmount
            )
            if (fromCurrency.equals(toCurrency)) {
                emit(Resource.Error(data= data,message = "Please select different currency."))
            }else{
                emit(Resource.Success(data))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(Resource.Error(message = "Conversion failed. ${ex.message}"))
        }
    }
}
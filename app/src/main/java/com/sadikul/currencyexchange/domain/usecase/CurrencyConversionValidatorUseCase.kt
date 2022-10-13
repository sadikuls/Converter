package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.core.utils.stringValueUptoTwoDecimalPlace
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import com.sadikul.currencyexchange.presentation.currencyconversion.ConversionModel
import com.sadikul.currencyexchange.presentation.currencyconversion.ValidatorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CurrencyConversionValidatorUseCase @Inject constructor(
    private val repo: BalanceCalculatorRepo
) {
    operator suspend fun invoke(data: ConversionModel): Flow<ValidatorState> = flow {
        repo.getBalanceList().let {
            if (data.fromAmount <= 0.0) {
                emit(ValidatorState(false, "Invalid amount."))
            } else if (data.fromCurrency.equals(data.toCurrency)) {
                emit(ValidatorState(false, "Please select differenct currency."))
            } else {
                val balance = repo.getCurrencyDetails(data.fromCurrency).balance
                if (data.fromAmount + data.commission > balance) {
                    emit(
                        ValidatorState(
                            false,
                            "Insufficient balance. Required ${
                                Double.stringValueUptoTwoDecimalPlace(
                                    data.fromAmount + data.commission
                                )
                            } ${data.fromCurrency}. Current balance ${
                                Double.stringValueUptoTwoDecimalPlace(
                                    balance
                                )
                            } ${data.fromCurrency}"
                        )
                    )
                } else {
                    emit(ValidatorState(true, "success"))
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}
package com.sadikul.currencyexchange.domain.usecase
import com.sadikul.currencyexchange.core.utils.stringValueUptoFourDecimalPlace
import com.sadikul.currencyexchange.domain.repository.BalanceCalculatorRepo
import com.sadikul.currencyexchange.domain.model.ConversionModel
import com.sadikul.currencyexchange.presentation.currencyconversionscreen.states.ValidatorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
                val balance = repo.getCurrencyDetails(data.fromCurrency)?.balance ?: 0.0
                if (data.fromAmount + data.commission > balance) {
                    emit(
                        ValidatorState(
                            false,
                            "Insufficient balance. Required ${
                                Double.stringValueUptoFourDecimalPlace(
                                    data.fromAmount + data.commission
                                )
                            } ${data.fromCurrency}. Current balance ${
                                Double.stringValueUptoFourDecimalPlace(
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
    }
}
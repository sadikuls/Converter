package com.sadikul.currencyexchange.presentation.currencyconversion
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadikul.currencyexchange.core.utils.COMMISSION_RATE
import com.sadikul.currencyexchange.core.utils.MAX_FREE_CONVERSION
import com.sadikul.currencyexchange.core.utils.MAX_FREE_CONVERSION_AMOUNT
import com.sadikul.currencyexchange.core.utils.Resource
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.domain.model.ConversionModel
import com.sadikul.currencyexchange.domain.usecase.*
import com.sadikul.currencyexchange.presentation.currencyconversion.states.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyconversionViewmodel @Inject constructor(
    private val currenciesUseCase: GetCurrenciesUseCaseWithXtimesRequest,
    private val balanceListUseCase: BalanceListUseCase,
    private val initialBalanceGeneratorUseCase: BalanceListInitializeUseCase,
    private val submitBalanceUseCase: StoreCalculatedBalanceUseCase,
    private val commissionCalculatorUseCase: CommissionFeeCalculatorUseCase,
    private val currencyConversionUseCase: CurrencyCalculatorUseCase,
    private val saveFromCurrencyUseCase: SaveFromCurrencyUseCase,
    private val getFromCurrencyUseCase: GetFromCurrencyUseCase,
    private val saveToCurrencyUseCase: SaveToCurrencyUseCase,
    private val getToCurrencyUseCase: GetToCurrencyUseCase,
    private val validatorUseCase: CurrencyConversionValidatorUseCase,
): ViewModel() {
    private val _currencystate = MutableStateFlow(CurrencyListState())
    val currencyListState get() = _currencystate.asStateFlow()

    private val _balanceState = MutableStateFlow(BalanceListState())
    val balanceListState get() = _balanceState.asStateFlow()

    private val _conversionState = MutableStateFlow(ConversionCalculationState())
    val conversionState get() = _conversionState.asStateFlow()

    private val _submissionState = MutableSharedFlow<ConversionResultState>(replay = 0)
    val submissionState get() = _submissionState.asSharedFlow()


    private val _statusFlow = MutableSharedFlow<String>(replay = 0)
    val statusFlow get() = _statusFlow.asSharedFlow()

    private val _fromCurrencyNamestatusFlow = MutableStateFlow(CurrencyState())
    val fromCurrencyNameStateFlow get() = _fromCurrencyNamestatusFlow.asStateFlow()

    private val _toCurrencyNamestatusFlow = MutableStateFlow(CurrencyState())
    val toCurrencyNameStateFlow get() = _toCurrencyNamestatusFlow.asStateFlow()


/*    private val _fromCurrencyNameList = MutableStateFlow<List<String>>(mutableListOf())
    val fromCurrencyNameList get() = _fromCurrencyNameList.asStateFlow()*/

    val fromCurrencyNameList = balanceListState.map { data ->
        data.balanceList.filter { it.balance > 0 }.map { it.currency }.sorted()
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(),null)

    init {
        getFromCurrency()
        getToCurrency()
        getCurrencies()
        getBalances()
    }

    private fun getBalances() {
        viewModelScope.launch(Dispatchers.IO) {
            initialBalanceGeneratorUseCase()
            balanceListUseCase().onEach {
                _balanceState.value = BalanceListState(it)
            }.launchIn(this)
        }
    }

    private fun getCurrencies() = viewModelScope.launch(Dispatchers.IO) {
        currenciesUseCase().onEach {
            when (it) {
                is Resource.Loading -> {
                    Log.w("CurrencyValue", "loding : data size" + it.data?.size)
                    if(it.data != null){
                        _currencystate.value = CurrencyListState(items = it.data,isLoading = true)
                    }else{
                        _currencystate.value = CurrencyListState(isLoading = true)
                    }
                }
                is Resource.Error -> {
                    //_currencystate.value = CurrencyListState(isLoading = false)
                    Log.w("CurrencyValue", "error : data size" + it.data?.size)
                    if(it.data == null){
                        _currencystate.value = CurrencyListState(error = it.message)
                    }else{
                        _currencystate.value = CurrencyListState(items = it.data, error = it.message)
                    }
                }
                is Resource.Success -> {
                    Log.w("CurrencyValue", "success : data size" + it.data.size)
                    _currencystate.value = CurrencyListState(items = it.data)

                }
            }
        }.launchIn(this)
    }


    fun convertCurrency(fromCurrency: String,toCurrency: String,amount: Double) = viewModelScope.launch(Dispatchers.IO) {
        currencyConversionUseCase(fromCurrency,toCurrency,amount).collect {
            when (it) {
                is Resource.Error -> {
                    _conversionState.value = ConversionCalculationState(conversionData = it.data,message = it.message)

                }
                is Resource.Success -> {
                    _conversionState.value = ConversionCalculationState(conversionData = it.data)
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun submit(data: ConversionModel) = viewModelScope.launch(Dispatchers.IO) {
        data.let {
            it.commission = commissionCalculatorUseCase(
                data.fromAmount,
                data.fromCurrency,
                COMMISSION_RATE,
                MAX_FREE_CONVERSION,
                MAX_FREE_CONVERSION_AMOUNT
            )
            validatorUseCase(it).onEach { result ->
                if (result.isValid) {
                    submitBalanceUseCase(
                        it
                    )
                    _submissionState.emit(
                        ConversionResultState(
                            conversionModel = it
                        )
                    )
                } else {
                    _statusFlow.emit(result.msg)
                }
            }.launchIn(this)
        }
    }

    fun saveFromCurrency(fromCurrency: Currency) = viewModelScope.launch {
        fromCurrency.apply {
            saveFromCurrencyUseCase(this)
            _fromCurrencyNamestatusFlow.value = CurrencyState(this)
        }
    }
    fun saveToCurrency(toCurrency: Currency) = viewModelScope.launch {
        toCurrency.apply {
            saveToCurrencyUseCase(this)
            _toCurrencyNamestatusFlow.value = CurrencyState(this)
        }
    }

    private fun getFromCurrency() = viewModelScope.launch{
        getFromCurrencyUseCase().let {
            _fromCurrencyNamestatusFlow.value = CurrencyState(it)
        }
    }

    private fun getToCurrency() = viewModelScope.launch {
        getToCurrencyUseCase()?.let {
            _toCurrencyNamestatusFlow.value = CurrencyState(it)
        }
    }
}
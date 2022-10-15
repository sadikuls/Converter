package com.sadikul.currencyexchange.presentation.currencyconversionscreen

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rbddevs.splashy.Splashy
import com.sadikul.currencyexchange.R
import com.sadikul.currencyexchange.core.utils.Util
import com.sadikul.currencyexchange.core.utils.stringValueUptoFourDecimalPlace
import com.sadikul.currencyexchange.data.remote.dto.Currency
import com.sadikul.currencyexchange.databinding.LayoutCurrencyConversionFragmentBinding
import com.sadikul.currencyexchange.domain.adapter.BalanceListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ConversionFragment : Fragment() {

    private val TAG = "ConversionFragment"
    private val viewModel: CurrencyconversionViewmodel by viewModels()
    private var sellAmount: Double = 100.0
    private lateinit var binding: LayoutCurrencyConversionFragmentBinding
    private val balanceListAdapter by lazy{ BalanceListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutCurrencyConversionFragmentBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSplashy()
        setupUi()
        setupRecyclerView()
        observeData()
    }


    fun setSplashy(){
        Splashy(requireActivity())    // For JAVA : new Splashy(this)
            .setLogo(R.drawable.usd_euro_transparent)
            .setTitle("Currency Converter")
            .setTitleColor("#009be0")
            .setSubTitle("Make life EASY")
            .setSubTitleColor(R.color.main_color)
            .setProgressColor(R.color.main_color)
            .setFullScreen(true)
            .setTime(3000)
            .show()
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.balanceListState.collectLatest {
                it.balanceList.let {
                    balanceListAdapter.differ.submitList(it.filter { it.balance > 0 }.toMutableList())
                    balanceListAdapter.notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.fromCurrencyNameList.collectLatest { names ->
                names?.let {
                    if (names.size > 0) setupSellSpinner(names)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.currencyListState.collect {
                if (it.isLoading) {
                    //Toast.makeText(requireContext(), "Loading started", Toast.LENGTH_SHORT).show()
                    showLoader(true)
                }

                if (it.error.isNotBlank()) {
                    //updateUi(true, null)
                    //Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    if (it.items.size <= 0) {
                        binding.tvInternetStatus.visibility = View.VISIBLE
                        binding.tvInternetStatus.text = it.error
                        showLoader(true)
                    } else {
                        //binding.tvInternetStatus.text = "Data synced"
                        showLoader(false)
                    }
                }

                if (it.items.size > 0) {
                    showLoader(false)
                    setupBuySpinner()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.conversionState.collect {
                binding.layoutBuy.apply {
                    it.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                    it.conversionData?.let {
                        inputTextAmount.setText("+${Double.stringValueUptoFourDecimalPlace(it.convertedAmount)}")
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.submissionState.collectLatest {
                it.conversionModel?.apply {
                    val message =
                        "You have converted ${Double.stringValueUptoFourDecimalPlace(fromAmount)} ${fromCurrency} to " +
                                "${Double.stringValueUptoFourDecimalPlace(convertedAmount)} ${toCurrency}." +
                                if (commission > 0) " Commission Fee - ${
                                    Double.stringValueUptoFourDecimalPlace(
                                        commission
                                    )
                                } ${fromCurrency}." else ""
                    showDialog(showTitle = true, msg = message)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.statusFlow.collectLatest {
                showDialog(msg = it)
            }
        }
    }

    private fun setupUi() {
        binding.apply {
            layoutSell.apply {
                ivArrow.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_baseline_arrow_upward_24,
                        null
                    )
                )
                ivArrow.background = resources.getDrawable(R.drawable.up_arrow_bg, null)
                tvTextSellOrBuy.text = "Sell"
                inputTextAmount.setBackgroundResource(android.R.color.transparent);
                inputTextAmount.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable) {}

                    override fun beforeTextChanged(
                        s: CharSequence, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                        if (!s.isEmpty() && s.length > 0 && Util.isNumericToX(s.toString())) {
                            sellAmount = s.toString().toDouble()
                            convertCurrency()
                        }
                    }
                })
            }

            layoutBuy.apply {
                ivArrow.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_baseline_arrow_downward_24,
                        null
                    )
                )
                ivArrow.background = resources.getDrawable(R.drawable.up_down_bg, null)
                tvTextSellOrBuy.text = "Receive"
                inputTextAmount.isEnabled = false
                inputTextAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.buy_color))
                inputTextAmount.setBackgroundResource(android.R.color.transparent);

            }
            btnSubmit.setOnClickListener {
                viewModel.conversionState.value.conversionData?.let { it1 -> viewModel.submit(it1) }
            }
        }

    }

    private fun convertCurrency() {
        viewModel.fromCurrencyNameStateFlow.value.currency?.currencyName?.let {
            viewModel.toCurrencyNameStateFlow.value.currency?.currencyName?.let { it1 ->
/*                if (it.equals(it1)) {
                    Toast.makeText(
                        requireContext(),
                        "Please select different currency",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }*/
                viewModel.convertCurrency(
                    fromCurrency = it,
                    toCurrency = it1,
                    amount = sellAmount
                )
            }
        }
    }

    private fun setupBuySpinner() {
        val buyNames =
            viewModel.currencyListState.value.items.map { currency -> currency.currencyName } as MutableList<String>
        val adapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                buyNames
            )
        }

        binding.layoutBuy.apply {
            spinner.adapter = adapter
            val indexToSelect = buyNames.indexOf(viewModel.toCurrencyNameStateFlow.value.currency?.currencyName)
            spinner.setSelection(if(indexToSelect == -1) buyNames.indexOf("USD") else indexToSelect)
            spinner.setOnItemSelectedListener(object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    viewModel.currencyListState.value.items.find {
                        it.currencyName == buyNames.get(
                            position
                        )
                    }?.let { viewModel.saveToCurrency(it) }
                    convertCurrency()
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            })
        }
    }

    private fun setupSellSpinner(names: List<String>) {

        val arrayAdapter = activity?.let {
            ArrayAdapter(
                it, android.R.layout.simple_spinner_item, names
            )
        }
        binding.layoutSell.spinner.apply {
            adapter = arrayAdapter
            val indexToSelect = names.indexOf(viewModel.fromCurrencyNameStateFlow.value.currency?.currencyName)
            setSelection( if (indexToSelect == -1) names.indexOf("EUR") else indexToSelect)
            setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedItemView?.let {
                        viewModel.currencyListState.value.items.find { b ->
                            b.currencyName == names.get(
                                position
                            )
                        }?.let {
                            viewModel.saveFromCurrency(
                                Currency(
                                    it.currencyName,
                                    it.currencyValue
                                )
                            )
                            convertCurrency()
                        }
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }

            })


        }
    }

    fun setupRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.balanceRecyclerview.apply {
            layoutManager = linearLayoutManager
            adapter = balanceListAdapter
        }
    }

    fun showDialog(showTitle: Boolean = false, msg: String) {
        val builder = AlertDialog.Builder(requireContext())
        if (showTitle) builder.setTitle("Currency Converted")
        builder.setMessage(msg)
        builder.setPositiveButton("Done") { dialog, which ->
        }
        builder.show()
    }

    fun showLoader(show:Boolean){
        binding.apply {
            if(show){
                progressLoader.visibility = View.VISIBLE
                layoutBuy.container.visibility = View.GONE
                layoutSell.container.visibility = View.GONE
                layoutButton.visibility = View.GONE
            }else{
                progressLoader.visibility = View.GONE
                binding.tvInternetStatus.visibility = View.GONE
                layoutBuy.container.visibility = View.VISIBLE
                layoutSell.container.visibility = View.VISIBLE
                layoutButton.visibility = View.VISIBLE

            }


        }
    }
}




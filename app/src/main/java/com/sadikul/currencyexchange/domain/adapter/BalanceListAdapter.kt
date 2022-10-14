package com.sadikul.currencyexchange.domain.adapter
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sadikul.currencyexchange.core.utils.stringValueUptoFourDecimalPlace
import com.sadikul.currencyexchange.databinding.ItemCurrencyRecyclerLayoutBinding
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel

class BalanceListAdapter : RecyclerView.Adapter<BalanceListAdapter.BalanceListViewHolder>() {
    private val TAG = "BalanceListAdapter"
    init {
        Log.d(TAG, "called BalanceListAdapter")
    }
    private val differCallback = object : DiffUtil.ItemCallback<CurrencyBalanceModel>(){
        override fun areItemsTheSame(oldItem: CurrencyBalanceModel, newItem: CurrencyBalanceModel): Boolean {
            return  oldItem.currency == newItem.currency
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CurrencyBalanceModel, newItem: CurrencyBalanceModel): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)


    inner class BalanceListViewHolder(binding: ItemCurrencyRecyclerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val _binding: ItemCurrencyRecyclerLayoutBinding = binding
        fun bind(currency: CurrencyBalanceModel) {
            _binding.apply {
                tvCurrency.text = "${Double.stringValueUptoFourDecimalPlace(currency.balance)} ${currency.currency}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceListViewHolder {
        val binding =
            ItemCurrencyRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BalanceListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BalanceListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])

    }

    override fun getItemCount()= differ.currentList.size

}
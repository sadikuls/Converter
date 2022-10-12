package com.sadikul.currencyexchange.data.local.db.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sadikul.currencyexchange.domain.model.CurrencyBalanceModel

@Entity(tableName = "account_balance")
data class AccountBalanceEntity(

    @PrimaryKey
    @ColumnInfo(name = "currency")
    val currency: String,

    @ColumnInfo(name = "balance")
    val balance: Double
)

fun AccountBalanceEntity.toBalanceModel() = CurrencyBalanceModel(currency= currency, balance = balance)
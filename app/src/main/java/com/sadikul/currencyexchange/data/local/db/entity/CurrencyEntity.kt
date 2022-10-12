package com.sadikul.currencyexchange.data.local.db.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sadikul.currencyexchange.data.remote.dto.Currency

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    @ColumnInfo(name = "currency")
    val currency: String,

    @ColumnInfo(name = "value")
    val value: Double
)

fun CurrencyEntity.toCurrency(): Currency = Currency(currency,value)
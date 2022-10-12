package com.sadikul.currencyexchange.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sadikul.currencyexchange.data.local.db.dao.AccountBalanceDao
import com.sadikul.currencyexchange.data.local.db.dao.CurrencyDao
import com.sadikul.currencyexchange.data.local.db.entity.AccountBalanceEntity
import com.sadikul.currencyexchange.data.local.db.entity.CurrencyEntity

@Database(
    entities = [
        CurrencyEntity::class,
        AccountBalanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {


    abstract fun currencyDao(): CurrencyDao
    abstract fun balanceDao(): AccountBalanceDao

    companion object {
        private var appDb: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (appDb == null) {
                appDb = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                    }).build()
            }
            return appDb!!
        }

        const val DATABASE_NAME: String = "sb_db_currency_exchange"
    }
}
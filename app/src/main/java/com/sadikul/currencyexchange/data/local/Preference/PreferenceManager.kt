package com.sadikul.currencyexchange.data.local.Preference

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.sadikul.currencyexchange.core.utils.APP_PREF
import com.sadikul.currencyexchange.data.remote.dto.Currency
import javax.inject.Inject

open class PreferenceManager @Inject constructor(
    private val context: Context,
    private val gson: Gson? = null
) :
    SharedPref(context) {

    var fromCurrency: Currency?
        get() {
            val json = getString(FROM_CURRENCY, "")
            Log.d(TAG,"native item ${json}")
            if (TextUtils.isEmpty(json)) return null

            return gson?.fromJson(json, Currency::class.java)
        }
        set(currency: Currency?) {
            Log.d(TAG,"from item ${gson?.toJson(currency)}")
            gson?.toJson(currency)?.let { writeStringToPref(FROM_CURRENCY, it) }
        }


    var toCurrency: Currency?
        get() {
            val json = getString(TO_CURRENCY, "")
            Log.d(TAG,"to item ${json}")
            if (TextUtils.isEmpty(json)) return null

            return gson?.fromJson(json, Currency::class.java)
        }
        set(currency: Currency?) {
            Log.d(TAG,"native raw item ${gson?.toJson(currency)}")
            gson?.toJson(currency)?.let { writeStringToPref(TO_CURRENCY, it) }
        }


    var numberOfConversion: Int
        get() {
            return getInt(NUMBER_OF_CONVERSION)
        }
        set(value){
            writeIntToPref(NUMBER_OF_CONVERSION, value)
        }

    var totalConvertedAmount: Double?
        get() {
            return getDouble(TOTAL_CONVERTED_AMOUNT)
        }
        set(value){
            if (value != null) {
                writeDoubleToPref(TOTAL_CONVERTED_AMOUNT, value)
            }
        }


    companion object {
        const val FROM_CURRENCY = "FROM_CURRENCY"
        const val TO_CURRENCY = "TO_CURRENCY"
        const val NUMBER_OF_CONVERSION = "NUMBER_OF_CONVERSION"
        const val TOTAL_CONVERTED_AMOUNT = "TOTAL_CONVERTED_AMOUNT"
        private val mInstance: PreferenceManager? = null
        val TAG = PreferenceManager::class.java.simpleName
        fun getInstance(context: Context): PreferenceManager {
            return mInstance ?: PreferenceManager(context.applicationContext)
        }
    }

}

open class SharedPref(context: Context) {

    var mContext: Context? = null
    var mSharedPreference: SharedPreferences? = null

    init {
        this.mContext = context
        mSharedPreference = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
    }

    /***
     * @param key : key for shared preference
     * @param value : String value for respective key
     * @return return true if sucessfully write to preference
     */
    fun writeStringToPref(key: String, value: String): Boolean {
        val editor = mSharedPreference!!.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    /***
     * @param key : key for shared preference
     * @param value : int value for respective key
     * @return return true if sucessfully write to preference
     */
    fun writeIntToPref(key: String, value: Int): Boolean {
        val editor = mSharedPreference!!.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    /***
     * @param key : key for shared preference
     * @param value : bool value for respective key
     * @return return true if sucessfully write to preference
     */
    fun writeBoolToPref(key: String, value: Boolean): Boolean {
        val editor = mSharedPreference!!.edit()
        editor.putBoolean(key, value)
        return editor.commit()
    }

    /***
     * @param key : key of respective value
     * @param default_value : default string if value is null
     * @return return default string if value is null
     */
    fun getString(key: String, default_value: String): String? {
        return mSharedPreference!!.getString(key, default_value)
    }

    /***
     * @param key : key of respective value
     * @return return false if value is null
     */
    fun getBoolean(key: String): Boolean {
        return mSharedPreference!!.getBoolean(key, false)
    }

    /***
     * @param key : key of respective value
     * @return return -1 if value is null
     */
    fun getInt(key: String): Int {
        return mSharedPreference?.getInt(key, 0)!!
    }

    /***
     * @param key : key of respective value
     * @return return -1 if value is null
     */
    fun getDouble(key: String): Double? {
        return mSharedPreference?.getLong(key,0L)?.let { java.lang.Double.longBitsToDouble(it) }
    }

    /***
     * @param key : key for shared preference
     * @param value : int value for respective key
     * @return return true if sucessfully write to preference
     */
    fun writeDoubleToPref(key: String, value: Double): Boolean {
        val editor = mSharedPreference!!.edit()
        editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        return editor.commit()
    }
}

package com.sadikul.currencyexchange.core.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson

object Util {
    private const val TAG = "Util"

    fun isNumericToX(toCheck: String): Boolean {
        return toCheck.toDoubleOrNull() != null
    }

    fun hideKeyboard(context: Context,view: View) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }



    inline fun <reified T> loadFromJson(
        context: Context, fileName: String
    ): Resource<T> {
        try {
            lateinit var jsonString: String
            jsonString = context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }
            val data = Gson().fromJson(jsonString, T::class.java)
            if (data is T) return (Resource.Success(data))
            else return Resource.Error(message = "Error while parsing json")

        } catch (e: Exception) {
            return Resource.Error(message = e.message+"")
        }
    }

}
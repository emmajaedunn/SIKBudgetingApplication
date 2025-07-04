package com.example.st10298850_prog7313_p2_lp.utils

import android.content.Context
import android.content.SharedPreferences

object UserSessionManager {
    private const val PREF_NAME = "user_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_CURRENCY = "currency"
    private const val CURRENCY_PREFS = "user_prefs"

    fun saveCurrency(context: Context, currency: String) {
        val prefs = context.getSharedPreferences(CURRENCY_PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CURRENCY, currency).apply()
    }

    fun getCurrency(context: Context): String {
        val prefs = context.getSharedPreferences(CURRENCY_PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENCY, "ZAR") ?: "ZAR"
    }

    fun getCurrencySymbol(context: Context): String {
        return when (getCurrency(context)) {
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "NGN" -> "₦"
            "ZAR" -> "R"
            else -> "R" // default
        }
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserId(context: Context, userId: Long) {
        val editor = getPreferences(context).edit()
        editor.putLong(KEY_USER_ID, userId)
        editor.apply()
    }

    fun getUserId(context: Context): Long {
        return getPreferences(context).getLong(KEY_USER_ID, -1L)
    }

    fun clearSession(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
}

package com.project.myapp
import android.content.Context
import android.content.SharedPreferences

/* Saves and gets saved filled information
*/

class Preferences(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    fun saveText(key: String, text: String) {
        pref.edit().putString(key, text).apply()
    }

    fun getSavedText(key: String): String? {
        return pref.getString(key, DEFAULT_STRING_VALUE)
    }

    fun saveBoolean(key: String, value: Boolean) {
        return pref.edit().putBoolean(key, value).apply()
    }

    fun getSavedBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    fun clearPreferences() {
        val editor: SharedPreferences.Editor? = this.pref.edit()
        editor?.clear()
        editor?.apply()
    }

    companion object {
        const val DEFAULT_STRING_VALUE = ""
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
        const val NAME_KEY = "name_key"
        const val CHECKBOX_KEY = "checkBox_key"
    }
}

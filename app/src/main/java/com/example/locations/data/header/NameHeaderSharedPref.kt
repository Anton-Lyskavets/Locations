package com.example.locations.data.header

import android.content.Context
import android.content.SharedPreferences
import com.example.locations.R

const val SAVE_KEY = "save_key"

class NameSectionSharedPreferences(private val context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(SAVE_KEY, Context.MODE_PRIVATE)

    fun initSharedPref(): String {
        return preferences.getString(SAVE_KEY,
            context.getText(R.string.default_name_header).toString()).toString()
    }

    fun editSharedPref(nameSection: String) {
        val editPref: SharedPreferences.Editor = preferences.edit()
        editPref.putString(SAVE_KEY, nameSection).apply()
    }
}
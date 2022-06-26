package com.example.YUmarket.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {



    val PREF_KEY_MY_EDITTEXT = "position"

     val prefs: SharedPreferences = context.getSharedPreferences("position", Context.MODE_PRIVATE)

     var myEditText: String
        get() = prefs.getString(PREF_KEY_MY_EDITTEXT,"")!!
        set(value) = prefs.edit().putString(PREF_KEY_MY_EDITTEXT, value).apply()
}
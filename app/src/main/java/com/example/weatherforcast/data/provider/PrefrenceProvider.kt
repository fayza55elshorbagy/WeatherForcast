package com.example.weatherforcast.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
    abstract class PrefrenceProvider(context: Context) {
        private val appContext = context.applicationContext

        protected val preferences: SharedPreferences
            get() = PreferenceManager.getDefaultSharedPreferences(appContext)
    }

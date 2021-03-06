package com.example.weatherforcast.data.provider

import android.content.Context
import com.example.weatherforcast.util.LanguageSystem
const val LANGUAGE_SYSTEM = "LANGUAGE_SYSTEM"

class LanguageProviderImpl(context: Context) :PrefrenceProvider(context), LanguageProvider {
    override fun getLanguageSystem(): LanguageSystem {
        val selectedName = preferences.getString(LANGUAGE_SYSTEM, LanguageSystem.English.name)
        return LanguageSystem.valueOf(selectedName!!)
    }
}
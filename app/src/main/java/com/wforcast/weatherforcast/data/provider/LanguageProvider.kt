package com.wforcast.weatherforcast.data.provider

import com.wforcast.weatherforcast.util.LanguageSystem

interface LanguageProvider {
    fun getLanguageSystem(): LanguageSystem
}
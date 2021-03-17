package com.example.weatherforcast.data.provider

import com.example.weatherforcast.util.LanguageSystem

interface LanguageProvider {
    fun getLanguageSystem(): LanguageSystem
}
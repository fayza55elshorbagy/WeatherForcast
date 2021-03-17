package com.example.weatherforcast.data.provider

import com.example.weatherforcast.util.UniteSystem

interface UnitProvider {
    fun getUnitSystem(): UniteSystem
}
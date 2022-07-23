package com.wforcast.weatherforcast.data.provider

import com.wforcast.weatherforcast.util.UniteSystem

interface UnitProvider {
    fun getUnitSystem(): UniteSystem
}
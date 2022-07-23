package com.wforcast.weatherforcast.data.provider

import android.content.Context
import com.wforcast.weatherforcast.util.UniteSystem

const val UNIT_SYSTEM = "UNIT_SYSTEM"

class UnitProviderImpl(context: Context) :PrefrenceProvider(context), UnitProvider {

    override fun getUnitSystem(): UniteSystem {
        val selectedName = preferences.getString(UNIT_SYSTEM, UniteSystem.METRIC.name)
        return UniteSystem.valueOf(selectedName!!)
    }
}
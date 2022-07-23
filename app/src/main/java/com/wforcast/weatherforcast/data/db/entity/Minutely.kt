package com.wforcast.weatherforcast.data.db.entity


import com.google.gson.annotations.SerializedName

data class Minutely(
    val dt: Int,
    val precipitation: Double
)
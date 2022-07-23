package com.wforcast.weatherforcast.data.db.entity


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)
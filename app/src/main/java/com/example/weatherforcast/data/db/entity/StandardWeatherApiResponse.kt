package com.example.weatherforcast.data.db.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

const val WEATHER_LOCATION_ID = 0


@Entity(tableName = "weather_location")
data class StandardWeatherApiResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val alerts:List<Alerts>?,
    @Embedded(prefix = "locationStandardWeather_")
    @SerializedName("current")
    val standardWeather: StandardWeather? = null


)
{
    @PrimaryKey(autoGenerate = false)
    var id: Int = WEATHER_LOCATION_ID


    @Ignore
    val minutely: List<Minutely>? = null

    val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(timezoneOffset.toLong())
            val zoneId = ZoneId.of(timezone)
            return ZonedDateTime.ofInstant(instant,zoneId)
        }
}
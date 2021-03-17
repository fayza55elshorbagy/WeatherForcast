package com.example.weatherforcast.ui.weather

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforcast.R
import java.text.SimpleDateFormat
import java.util.*


class DailyWeatherAdapter(private val context: Context, private val dateName:ArrayList<String>, private val description:ArrayList<String>, private val dailtTemp:ArrayList<String>, private val dailyWind:ArrayList<String>, private val dailyHumidity:ArrayList<String>, private val dailyPressure:ArrayList<String>, private val dailyCloud:ArrayList<String>, private val weatherImages:ArrayList<String>): RecyclerView.Adapter<DailyWeatherAdapter.ViewHolderExample>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderExample {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.daily_weather_item,parent,false)
        return ViewHolderExample(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolderExample, position: Int) {
        holder.dateName.text = timeAndDateFormat(dateName.get(position))
        holder.description.text = description.get(position)
        holder.dailyTemp.text = dailtTemp.get(position)
        holder.dailyHumidity.text = dailyHumidity.get(position)
        holder.dailyPressure.text = dailyPressure.get(position)
        holder.dailyWind.text = dailyWind.get(position)
        holder.dailyCloud.text = dailyCloud.get(position)
        Glide.with(context)
                .load("http://openweathermap.org/img/w/" + weatherImages.get(position) + ".png")
                .into(holder.dailyIcon)
        //String iconUrl = "http://openweathermap.org/img/w/" + iconCode + ".png";


    }
    override fun getItemCount() = dailtTemp.size

    class ViewHolderExample(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dateName: TextView = itemView.findViewById(R.id.daily_date_name)
        val description : TextView = itemView.findViewById(R.id.daily_decription)
        val dailyTemp : TextView = itemView.findViewById(R.id.textView_temp_daily)
        val dailyWind : TextView = itemView.findViewById(R.id.textView_wind_daily)
        val dailyHumidity : TextView = itemView.findViewById(R.id.textView_humidity_daily)
        val dailyPressure : TextView = itemView.findViewById(R.id.textView_cloud_daily)
        val dailyCloud : TextView = itemView.findViewById(R.id.textView_cloud_daily)
        val dailyIcon : ImageView = itemView.findViewById(R.id.daily_weather_icon)
    }
    private fun timeAndDateFormat(date:String) : String
    {
        try {
            var locale:Locale?=null
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val item = sharedPreferences.getString("LANGUAGE_SYSTEM","enmcm")
            Log.i("info","Language "+sharedPreferences.getString("LANGUAGE_SYSTEM","enmcm"))
            when (item) {
                "Arabic" -> {
                    locale = Locale("ar")
                }
                "English" -> {
                    locale = Locale("en")
                }
            }
            val calendar = Calendar.getInstance()
            val tz = TimeZone.getDefault()
            calendar.timeInMillis = date.toLong() * 1000
            val sdf = SimpleDateFormat("EEEE'Q'dd-MMM-yyyy'T'HH:mm",locale)
            val currenTimeZone = calendar.time as Date
            val time = sdf.format(currenTimeZone).split("Q")
            return time[0]
        } catch (e: Exception) {
        }
        return ""
    }



}
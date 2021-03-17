package com.example.weatherforcast.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforcast.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HourlyWeatherAdapter(private val context: Context, private val time:ArrayList<String>, private val temp:ArrayList<String>, private val weatherImages:ArrayList<String>): RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolderExample>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderExample {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hourly_weather_item,parent,false)
        return ViewHolderExample(itemView)
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolderExample, position: Int) {
        holder.time.text = timeAndDateFormat(time.get(position))
        holder.hourlyTemp.text = temp.get(position)+"°"
        Glide.with(context)
                .load("http://openweathermap.org/img/w/" + weatherImages.get(position) + ".png")
                .into(holder.hourlyImg)
        //String iconUrl = "http://openweathermap.org/img/w/" + iconCode + ".png";
        if(position == 0 ||position == 2||position == 4||position == 6||position == 8||position == 10||position == 12||position == 14||position == 16||position == 18||position == 20||position == 22||position == 24)
        {
            holder.card.setCardBackgroundColor(Color.parseColor("#FDBF00"))
        }
        else
            holder.card.setCardBackgroundColor(Color.parseColor("#FFFFFF"))



    }
    override fun getItemCount() = temp.size

    class ViewHolderExample(itemView: View) : RecyclerView.ViewHolder(itemView){
        val time: TextView = itemView.findViewById(R.id.time)
        val hourlyTemp : TextView = itemView.findViewById(R.id.hourly_temp)
        val hourlyImg : ImageView = itemView.findViewById(R.id.hourly_weather_icon)
        val card : CardView = itemView.findViewById(R.id.card_view)

    }
    private fun timeAndDateFormat(date:String) : String
    {
        Log.i("info",date+"gdljlg")
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
            val sdf = SimpleDateFormat("EEEE-dd-MMM-yyyy,hh:mm",locale)
            val currenTimeZone = calendar.time as Date
            val time = sdf.format(currenTimeZone).split(",")
            return time[1]
        } catch (e: Exception) {
        }
        return ""
    }



}
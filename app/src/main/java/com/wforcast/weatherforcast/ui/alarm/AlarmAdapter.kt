package com.wforcast.weatherforcast.ui.alarm

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wforcast.weatherforcast.R
import com.wforcast.weatherforcast.data.db.entity.AlertData
import com.wforcast.weatherforcast.ui.base.Listener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AlarmAdapter (private val listener: Listener,private val context : Context, private val alarmdata : List<AlertData>): RecyclerView.Adapter<AlarmAdapter.ViewHolderExample>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderExample {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item,parent,false)
        return ViewHolderExample(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderExample, position: Int) {
        Log.i("info","position : "+position.toString())
        val curentItem = alarmdata.get(position)
        holder.alarm_name.text =curentItem.alertName
        holder.alarm_date.text = curentItem.alertTime
        holder.remove.setOnClickListener {
            GlobalScope.launch (Dispatchers.Main){
                listener.deletAlert(curentItem.Id)
            }
            GlobalScope.launch(Dispatchers.IO) {
                listener.cancelAlarm(curentItem.Id)
            }


        }
    }

    override fun getItemCount() = alarmdata.size

    class ViewHolderExample(itemView: View) : RecyclerView.ViewHolder(itemView){
        val alarm_name: TextView = itemView.findViewById(R.id.alarm_name)
        val alarm_date : TextView = itemView.findViewById(R.id.alarm_date)
        val remove : ImageView = itemView.findViewById(R.id.alert_remove)
    }

}
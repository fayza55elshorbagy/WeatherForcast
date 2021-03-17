package com.example.weatherforcast.ui.locationlist

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforcast.R
import com.example.weatherforcast.data.db.entity.MapLocation
import com.example.weatherforcast.ui.base.Listener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class LocationsAdapter (private val list: Listener, private val context : Context, private val locationData: List<MapLocation>): RecyclerView.Adapter<LocationsAdapter.ViewHolderExample>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderExample {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.location_item,parent,false)
        return ViewHolderExample(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderExample, position: Int) {
        val currentItem = locationData[position]
        holder.location_name.text = updateLocation(currentItem.maplat,currentItem.maplon)
        holder.card_view.setOnClickListener {
           list.go_home(currentItem.maplat,currentItem.maplon)
        }
        holder.remove.setOnClickListener {
            deleteFromList(updateLocation(currentItem.maplat,currentItem.maplon))
        }

    }


    override fun getItemCount() = locationData.size

    class ViewHolderExample(itemView: View) : RecyclerView.ViewHolder(itemView){
        val location_name: TextView = itemView.findViewById(R.id.location_name)
        val card_view : CardView = itemView.findViewById(R.id.location_list_card_view)
        val remove : ImageView = itemView.findViewById(R.id.location_remove)
    }
    private fun updateLocation(lat: Double, lon: Double): String {
        val gcd = Geocoder(context, Locale.getDefault())
        val addresses = gcd.getFromLocation(lat, lon, 1)
        return   addresses.get(0).countryName + ", "+addresses.get(0).locality
    }
    private fun deleteFromList(address:String)  {
        GlobalScope.launch {
            list.deleteMapLocatio(address)
        }

    }
}
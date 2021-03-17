package com.example.weatherforcast.ui.locationlist

import android.app.AlertDialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforcast.R
import com.example.weatherforcast.data.db.entity.AlertData
import com.example.weatherforcast.data.db.entity.MapLocation
import com.example.weatherforcast.data.provider.CUSTOM_LOCATION
import com.example.weatherforcast.ui.base.ScopedFragment
import com.example.weatherforcast.ui.base.Listener
import com.example.weatherforcast.ui.weather.CurrentWeatherFragment
import kotlinx.android.synthetic.main.location_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList

class LocationListFragment: ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: LocationListViewModelFactory by instance()
    private lateinit var viewModel: LocationListViewModel
    private lateinit var  lisener: Listener
    var new_updated_List = ArrayList<MapLocation>()


    companion object{
        var retrieved_locations = ArrayList<MapLocation>()
    }
    override fun onStart() {
        bindREc()
        super.onStart()
    }

    private fun bindREc() = launch(Dispatchers.Main) {
         retrieved_locations = viewModel.map_location.await() as ArrayList<MapLocation>

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        location_list_rec.layoutManager = layoutManager
        location_list_rec.adapter = LocationsAdapter(lisener,requireContext(),retrieved_locations!!)
        Log.i("info", retrieved_locations.toString()+"mapppppppppppppp")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this,viewModelFactory).get(LocationListViewModel::class.java)

        return inflater.inflate(R.layout.location_list_fragment, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()
        lisener = object: Listener
        {
            override fun go_home(lat: Double, lon: Double) {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                sharedPreferences.edit().putString(CUSTOM_LOCATION, updateLocation(lat,lon)).commit()
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, CurrentWeatherFragment())?.addToBackStack(null)?.commit()

            }

            override suspend fun deleteMapLocatio(address: String) {
                // viewModel.delete_location.await()
                GlobalScope.launch(Dispatchers.Main) {
                    val diaBox: AlertDialog = AskOption(address)
                    diaBox.show()
                }
            }

            override suspend fun deletAlert(id: Int): List<AlertData> {
                return emptyList()
            }

            override fun cancelAlarm(id: Int) {
            }
        }
    }

    private fun AskOption(address: String): AlertDialog {
        return AlertDialog.Builder(context) // set message, title, and icon
            .setTitle("Delete")
            .setMessage("Do you want to Delete")
            .setIcon(R.drawable.ic_baseline_delete_forever_24)
            .setPositiveButton(
                "Delete"
            ) { dialog, whichButton -> //your deleting code
                GlobalScope.launch(Dispatchers.Main) {
                    new_updated_List = viewModel.delete(address) as ArrayList<MapLocation>
                    notifyAdapterListchanged(new_updated_List)
                    dialog.dismiss()
                }
            }
            .setNegativeButton(
                "cancel"
            ) { dialog, which -> dialog.dismiss() }
            .create()
    }

    private fun notifyAdapterListchanged(newUpdatedList: ArrayList<MapLocation>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        location_list_rec.layoutManager = layoutManager
        location_list_rec.adapter = LocationsAdapter(lisener,requireContext(),newUpdatedList)
        Log.i("info", newUpdatedList.toString()+"newList")
    }

    private fun bindUI(){
        add_locations.setOnClickListener {
            val intent = Intent (getActivity(),MapActivity::class.java)
            getActivity()?.startActivity(intent)
            getActivity()?.finish()
      }
    }

    private fun updateLocation(lat: Double, lon: Double): String {
        val gcd = Geocoder(context, Locale.getDefault())
        val addresses = gcd.getFromLocation(lat, lon, 1)
        return   addresses.get(0).countryName + ", "+addresses.get(0).locality
    }

}
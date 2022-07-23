package com.wforcast.weatherforcast.ui.locationlist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wforcast.weatherforcast.MainActivity
import com.wforcast.weatherforcast.R
import com.wforcast.weatherforcast.data.db.entity.AlertData
import com.wforcast.weatherforcast.data.db.entity.MapLocation
import com.wforcast.weatherforcast.data.provider.CUSTOM_LOCATION
import com.wforcast.weatherforcast.ui.base.Listener
import com.wforcast.weatherforcast.ui.locationlist.LocationListFragment.Companion.retrieved_locations
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.location_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.*


class MapActivity: AppCompatActivity(),KodeinAware {
    override val kodein: Kodein by closestKodein()
    private lateinit var locationListViewModel: LocationListViewModel
    private val viewModelFactory: LocationListViewModelFactory by instance()
    var wifiManager: WifiManager? = null
    private val PLACE_PICKER_REQUEST = 999
    private lateinit var  listen: Listener
    var map_updated_List = ArrayList<MapLocation>()

    override fun onStart() {
        super.onStart()
        Log.i("info", "map started")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_list_fragment)
        locationListViewModel = ViewModelProvider(this,viewModelFactory).get(LocationListViewModel::class.java)

        wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager!!.isWifiEnabled = false
        listen = object: Listener {
            override fun go_home(lat: Double,lon: Double) {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MapActivity)
                sharedPreferences.edit().putString(CUSTOM_LOCATION, updateLocation(lat,lon)).commit()
                startActivity(Intent(this@MapActivity, MainActivity::class.java))
                finish()

            }

            override suspend fun deleteMapLocatio(address: String) {
                Log.i("info", address+" address to be delet from map")
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
        add_locations.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
            finish()
        }
        openPlacePicker()
    }
    private fun openPlacePicker() {
        val builder = PlacePicker.IntentBuilder()
        try {
            wifiManager!!.isWifiEnabled = true
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
            //Enable Wifi
        } catch (e: GooglePlayServicesRepairableException) {
            Log.d("Exception", e.message!!)
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.d("Exception", e.message!!)
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(this@MapActivity, data)
                    val latitude = place.latLng.latitude
                    val longitude = place.latLng.longitude
                    val l = MapLocation(updateLocation(latitude,longitude),latitude, longitude)
                    runBlocking {
                        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MapActivity)
                        sharedPreferences.edit().putString(CUSTOM_LOCATION, updateLocation( place.latLng.latitude,place.latLng.longitude)).commit()
                        val map_locations = locationListViewModel.map_location.await()
                        if(map_locations.filter { it.address == l.address }.any())
                        {
                            Log.i("info","true")
                            bindRec(retrieved_locations)
                        }
                        else
                        {
                            retrieved_locations.add(l)
                            bindRec(retrieved_locations)
                            Log.i("info","false : "+ retrieved_locations)

                        }

                        Log.i("info", sharedPreferences.getString(CUSTOM_LOCATION,"u")+"inside map")
                    }

                }
            }
        }
        else
        {
            alertDialog()
        }
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.title)
        builder.setMessage(R.string.description)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(R.string.positive){dialogInterface, which ->
            startActivity(Intent(this,MapActivity::class.java))
            finish()
        }
        builder.setNeutralButton(R.string.cancel){dialogInterface , which ->
//            var mainFragment: LocationListFragment = LocationListFragment()
//            fragmentManager.beginTransaction().add(R.id.nav_host_fragment, mainFragment)
//                    .commit()
        }
        builder.setNegativeButton(R.string.negative){dialogInterface, which ->
//            var mainFragment: LocationListFragment = LocationListFragment()
//            supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, mainFragment)
//                    .commit()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun bindRec(ll: ArrayList<MapLocation>?) {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        location_list_rec.layoutManager = layoutManager
        location_list_rec.adapter = LocationsAdapter(listen,this,ll!!)

    }

    private fun updateLocation(lat: Double, lon: Double): String {
        val gcd = Geocoder(applicationContext, Locale.getDefault())
        val addresses = gcd.getFromLocation(lat, lon, 1)
        return   addresses.get(0).countryName + ", "+addresses.get(0).locality

    }
    private fun AskOption(address: String): AlertDialog {

        return AlertDialog.Builder(this) // set message, title, and icon
            .setTitle("Delete")
            .setMessage("Do you want to Delete")
            .setIcon(R.drawable.ic_baseline_delete_forever_24)
            .setPositiveButton(
                "Delete"
            ) { dialog, whichButton -> //your deleting code
                GlobalScope.launch(Dispatchers.Main) {
                    map_updated_List  = locationListViewModel.delete(address) as ArrayList<MapLocation>
                    Log.i("info", map_updated_List.toString()+" address to be delet from map")
                    Thread.sleep(500)
                    notifyAdapterListchanged(map_updated_List)
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
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        location_list_rec.layoutManager = layoutManager
        location_list_rec.adapter = LocationsAdapter(listen,this,newUpdatedList)
        Log.i("info", newUpdatedList.toString()+"newList")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("info", "map destroyed")
    }

}
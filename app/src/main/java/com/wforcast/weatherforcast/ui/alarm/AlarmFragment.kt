package com.wforcast.weatherforcast.ui.alarm

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wforcast.weatherforcast.R
import com.wforcast.weatherforcast.data.db.entity.AlertData
import com.wforcast.weatherforcast.ui.base.Listener
import com.wforcast.weatherforcast.ui.base.ScopedFragment
import com.wforcast.weatherforcast.ui.base.SharedViewModel
import kotlinx.android.synthetic.main.fragment_alarm.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AlarmFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val alarmViewModelFactory: AlarmViewModelFactory by instance()
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var dialog : DialogFragment
    private lateinit var listener: Listener
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var alarmService: AlarmService


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        alarmViewModel = ViewModelProvider(this,alarmViewModelFactory).get(AlarmViewModel::class.java)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_alarm, container, false)
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)
        dialog = AlarmSettingFragment()
        alarmService = AlarmService(requireActivity())

        bindRecyclerView()
        addBtn.setOnClickListener {
            dialog.show(requireFragmentManager(), "add Alarm")
        }
        sharedViewModel.getSelected()?.observe(viewLifecycleOwner, Observer {
            bindRecyclerView()
            Log.i("info","5----f2")

        })
        listener=object:Listener
        {
            override fun go_home(lat: Double, lon: Double) {
            }
            override suspend fun deleteMapLocatio(address: String) {
            }
            override suspend fun deletAlert(id: Int): List<AlertData> {
                GlobalScope.launch(Dispatchers.Main) {
                    val diaBox: AlertDialog = AskOption(id)
                    diaBox.show()
                }
                return emptyList()
            }

            override fun cancelAlarm(id: Int) {
                alarmService.cancelAlarm(id)

            }

        }
    }

    private  fun  AskOption(id: Int): AlertDialog {
        return AlertDialog.Builder(requireContext()) // set message, title, and icon
            .setTitle("Delete")
            .setMessage("Do you want to Delete")
            .setIcon(R.drawable.ic_baseline_delete_forever_24)
            .setPositiveButton(
                "Delete"
            ) { dialog, whichButton -> //your deleting code
                GlobalScope.launch(Dispatchers.Main) {
                   var  list = alarmViewModel.deleteAlarm(id)
                    delay(500)
                    notifyDataChanged(list)
                    dialog.dismiss()
                }
            }
            .setNegativeButton(
                "cancel"
            ) { dialog, which -> dialog.dismiss() }
            .create()
    }

    private fun bindRecyclerView() = launch {
        var updatedlist =  ArrayList<AlertData>()
        val df: DateFormat = SimpleDateFormat("dd MM yyyy hh:mm", Locale.getDefault())
        val currentDateAndTime: String = df.format(Date())
        Log.i("info",currentDateAndTime)
        val list = alarmViewModel.alarm_data.await()
        for (i in list.indices)
        {
            if(list.get(i).alertTime.compareTo(currentDateAndTime) > 0 )
                updatedlist.add(list.get(i))

        }
        Log.i("info",updatedlist.toString())

        val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
       alarm_list_rec.layoutManager = layoutManager
        alarm_list_rec.adapter = AlarmAdapter(listener,requireContext(),updatedlist)
    }
    private fun notifyDataChanged(list : List<AlertData>) {
        var updatedlist =  ArrayList<AlertData>()
        val df: DateFormat = SimpleDateFormat("dd MM yyyy hh:mm", Locale.getDefault())
        val currentDateAndTime: String = df.format(Date())
        Log.i("info",currentDateAndTime)
        for (i in list.indices)
        {
            if(list.get(i).alertTime.compareTo(currentDateAndTime) > 0 )
                updatedlist.add(list.get(i))

        }
        Log.i("info",updatedlist.toString())
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        alarm_list_rec.layoutManager = layoutManager
        alarm_list_rec.adapter = AlarmAdapter(listener,requireContext(),updatedlist)}



}
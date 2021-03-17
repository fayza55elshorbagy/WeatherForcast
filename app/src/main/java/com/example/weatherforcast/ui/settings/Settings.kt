package com.example.weatherforcast.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.AbsListView
import androidx.core.app.ActivityCompat.recreate
import androidx.navigation.Navigation
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.weatherforcast.util.LanguageSystem
import com.example.weatherforcast.R
import com.example.weatherforcast.data.provider.LANGUAGE_SYSTEM
import com.example.weatherforcast.ui.base.LocalHelper
import com.example.weatherforcast.ui.locationlist.LocationListFragment
import java.util.*


class Settings :  PreferenceFragmentCompat()  {
    private lateinit var locale : Locale
    var language: ListPreference? = null
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefrences)
        language = findPreference("LANGUAGE_SYSTEM") as ListPreference?
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        language?.setOnPreferenceChangeListener { preference, newValue ->
            chooseLanguage(preference, newValue as String)
            LocalHelper().persist(requireContext(),"en")
            requireActivity().recreate()
            true
        }
    }

    private fun chooseLanguage(preference: Preference?, newValue: String){
        var item: String = newValue
        if (preference!!.key.equals("LANGUAGE_SYSTEM")) {
            when (item) {
                "Arabic" -> {
                    setLocale(context as Activity,"ar")
                    requireActivity().recreate()
                }
                "English" -> {
                    setLocale(context as Activity,"en")
                    requireActivity().recreate()
                }
            }
        }
    }
     fun setLocale(activity: Activity, languageCode: String?): Unit {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        }
        resources.updateConfiguration(config, resources.displayMetrics)
    }


}
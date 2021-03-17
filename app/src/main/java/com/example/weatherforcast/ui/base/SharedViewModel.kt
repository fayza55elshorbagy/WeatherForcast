package com.example.weatherforcast.ui.base

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    private val selected: MutableLiveData<Int> = MutableLiveData<Int>()

    fun select(item: Int) {
        selected.setValue(item)
    }

    fun getSelected(): LiveData<Int>? {
        return selected
    }
}
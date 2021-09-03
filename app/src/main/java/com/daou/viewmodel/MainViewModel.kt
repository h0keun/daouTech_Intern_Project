package com.daou.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*

class MainViewModel(application: Application) : AndroidViewModel(application){
    val TAG = "MainViewModel"

    val speed = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val distance = MutableLiveData<String>()

    init {
        speed.value = "0"
        time.value = "0"
        distance.value = "0"
    }

    override fun onCleared() {
        Log.d(TAG, "## MainViewModel - onCleared() called!!")
        super.onCleared()
    }
}
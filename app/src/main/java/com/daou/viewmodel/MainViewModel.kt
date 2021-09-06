package com.daou.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*

class MainViewModel(application: Application) : AndroidViewModel(application){
    val TAG = "MainViewModel"
    val goHistory = SingleLiveEvent<Any>()

    fun historyButton() {
        goHistory.call()
    }

    override fun onCleared() {
        Log.d(TAG, "## MainViewModel - onCleared() called!!")
        super.onCleared()
    }
}
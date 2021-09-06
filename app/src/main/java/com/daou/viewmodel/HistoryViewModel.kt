package com.daou.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.daou.data.local.AppDatabase
import com.daou.data.local.History
import com.daou.repository.LocalRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocalRepository =
        LocalRepository(AppDatabase.getDatabase(application))

    var allHistory : LiveData<List<History>> = repository.allHistory
}
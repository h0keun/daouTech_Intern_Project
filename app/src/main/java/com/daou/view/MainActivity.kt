package com.daou.view

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daou.databinding.ActivityMainBinding
import com.daou.viewmodel.MainViewModel
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.daou.R
import com.daou.viewmodel.MainViewModelFactory
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import java.lang.Math.*
import java.util.*
import com.daou.MyNavigationService


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
        }

        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.vm2 = viewModel

        viewModel.goHistory.observe(this, Observer {
            startActivity(Intent(this, HistoryActivity::class.java))
        })

        serviceState()
        buttonState()

        val broadcast = MyBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction("intentData")
        registerReceiver(broadcast, filter)
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            CoroutineScope(Dispatchers.Main).launch {
                binding.time.text = intent?.getStringExtra("time").toString()
                binding.speed.text = intent?.getStringExtra("speed").toString()
                binding.distance.text = intent?.getStringExtra("distance").toString()
            }
        }
    }

    private fun serviceState() {
        if (isServiceRunning()) {
            binding.stateTextView.text = "주행중 입니다!"
            binding.startButton.text = "종료하기"
        } else {
            binding.stateTextView.text = "주행 시작하기"
            binding.startButton.text = "시작하기"
        }
    }

    private fun buttonState(){
        binding.startButton.setOnClickListener {
            val serviceIntent = Intent(this@MainActivity, MyNavigationService::class.java)
            if (!isServiceRunning()) {
                binding.stateTextView.text = "주행중 입니다!"
                binding.startButton.text = "종료하기"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent)
                } else {
                    startService(serviceIntent)
                }
            } else {
                binding.stateTextView.text = "주행 시작하기"
                binding.startButton.text = "시작하기"
                stopService(serviceIntent)
                clearAll()
            }
        }
    }

    private fun clearAll() {
        binding.speed.text = "0"
        binding.distance.text = "0"
        binding.time.text = "0"
    }

    private fun isServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("com.daou.MyNavigationService" == service.service.className) {
                return true
            }
        }
        return false
    }
}


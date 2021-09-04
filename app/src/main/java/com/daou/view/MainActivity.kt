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

    // todo 쓰레드말고 코루틴으로
    // todo onNewIntent
    // todo 로직분리하기

    @SuppressWarnings("deprecation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
        }

        val viewModelFactory = MainViewModelFactory(application)
        // todo koin을 사용하면 필요하지 않은 부분임!!
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.vm2 = viewModel

        binding.listButton.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        if (isServiceRunning()) {
            binding.stateTextView.text = "주행중 입니다!"
            binding.startButton.text = "종료하기"
        } else {
            binding.stateTextView.text = "주행 시작하기"
            binding.startButton.text = "시작하기"
        }

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

        val broadcast = MyBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction("time")
        filter.addAction("speed")
        filter.addAction("distance")
        registerReceiver(broadcast, filter)
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            runOnUiThread {
                binding.time.text = intent?.getStringExtra("time").toString()
                binding.speed.text = intent?.getStringExtra("speed").toString()
                binding.distance.text = intent?.getStringExtra("distance").toString()
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


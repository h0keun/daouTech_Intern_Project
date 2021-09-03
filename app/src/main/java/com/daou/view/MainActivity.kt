package com.daou.view

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daou.databinding.ActivityMainBinding
import com.daou.viewmodel.MainViewModel
import android.content.Intent
import android.content.pm.PackageManager
import com.daou.MyNavigationService
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daou.R
import com.daou.data.local.AppDatabase
import com.daou.data.local.History
import com.daou.viewmodel.MainViewModelFactory
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var curlatitude: String? = "0.0"
    private var curlongitude: String? = "0.0"
    private var startTime: String? = null
    private var endTime: String? = null
    private var totalDistance: String? = null
    private var totalTime: String? = null
    private var locationXY = arrayListOf<String>()
    private var location = listOf<String>()
    //private var locationmanager : Location? = null

    // todo 쓰레드말고 코루틴으로
    // todo onNewIntent
    // todo 로직분리하기

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

        val serviceIntent = Intent(this@MainActivity, MyNavigationService::class.java)

        binding.listButton.setOnClickListener{
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.startButton.setOnClickListener {
            val buttonState =
                getSharedPreferences("ButtonState", 0).getBoolean("ButtonState", false)
            val state = getSharedPreferences("ButtonState", 0)
            val editor = state.edit()

            val now = System.currentTimeMillis()
            val currentTime = Date(now)
            val time = SimpleDateFormat("hh시mm분", Locale.getDefault())

            if (!buttonState) {
                editor.putBoolean("ButtonState", true)
                editor.apply()
                binding.stateTextView.text = "주행중 입니다!"
                binding.startButton.text = "종료하기"
                startTime = time.format(currentTime)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent)
                    buildGoogleApiClient()
                } else {
                    startService(serviceIntent)
                    buildGoogleApiClient()
                }

            } else {
                editor.putBoolean("ButtonState", false)
                editor.apply()
                binding.stateTextView.text = "주행기록 시작하기"
                binding.startButton.text = "시작하기"
                endTime = time.format(currentTime)
                location = locationXY.toList()

                val db = AppDatabase.getDatabase(applicationContext)
                Thread {
                    db?.historyDao()?.insert(
                        History(
                            null,
                            startTime,
                            endTime,
                            totalTime,
                            totalDistance,
                            location
                        )
                    )
                }.start()
                // todo 코루틴으로

                locationXY.clear()
                fusedLocationClient?.removeLocationUpdates(locationCallback)
                stopService(serviceIntent)
            }
        }


        viewModel.speed.observe(this, Observer {

        })

        viewModel.time.observe(this, Observer {

        })

        viewModel.distance.observe(this, Observer {

        })
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null) {
                    curlatitude = location.latitude.toString()
                    curlongitude = location.longitude.toString()
                    locationXY.add("${curlatitude}/${curlongitude}")
                    Log.d("잘 받아지는가", "$locationXY")
                    // Update UI with location data

                }
            }
            locationResult.locations
        }

    }
    @Synchronized
    private fun buildGoogleApiClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 3000L

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO : Consider calling
            // TODO : ActivityCompat#requestPermissions

            return
        }
        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}


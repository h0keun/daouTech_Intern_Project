package com.daou

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.daou.view.MainActivity
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import com.daou.data.local.AppDatabase
import com.daou.data.local.History
import com.google.android.gms.location.*
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

class MyNavigationService : Service() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var curlatitude: Double? = null
    private var curlongitude: Double? = null
    private var startTime: String? = null
    private var endTime: String? = null

    private var intervalDistance: Int = 0
    private var totalDistance: Int = 0
    private var distanceToData: String? = null

    private var timerTask: Timer? = null
    private var timer: Int = 0
    private var totalTime: String? = null
    private var timeToData: String? = null
    private var curSpeed: Int = 0

    private var locationXY = arrayListOf<String>()
    private var location = listOf<String>()

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val now = System.currentTimeMillis()
        val currentTime = Date(now)
        val time = SimpleDateFormat("hh시mm분", Locale.getDefault())
        startTime = time.format(currentTime)
        timeToData = ""
        distanceToData = ""
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        buildGoogleApiClient()

        var thread = Thread(Runnable {
            timerTask = kotlin.concurrent.timer(period = 1000) {
                timer++
                val s = timer % 60
                val m = timer / 60
                val h = timer / 3600

                totalTime = when {
                    h == 0 && m == 0 -> {
                        "${s}초"
                    }
                    h == 0 && m != 0 -> {
                        "${m}분 ${s}초"
                    }
                    else -> {
                        "${h}시간 ${m}분 ${s}초"
                    }
                }
                val intent = Intent()
                intent.action = "time"
                intent.putExtra("time", totalTime)
                intent.putExtra("speed", "$curSpeed m/s")
                intent.putExtra("distance", "$totalDistance m")
                sendBroadcast(intent)
            }
        }).start()

        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "주행거리 기록중"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel =
                NotificationChannel(MyNavigationService.CHANNEL_ID, name, importance)
            val notificationManager =
                getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        } else {
            val builder2 = NotificationCompat.Builder(this, "default")

            builder2.setContentTitle("주행거리 기록중")
            builder2.setContentText("주행거리 기록중!!")

        }

        val notificationIntent = Intent(baseContext, MainActivity::class.java).also {
            it.action = (Intent.ACTION_MAIN)
            it.addCategory(Intent.CATEGORY_LAUNCHER)
            it.flags = (Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val pendingIntent = PendingIntent.getActivity(
            baseContext,
            0,
            notificationIntent,
            FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, MyNavigationService.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_add_location_24)
            .setContentText("주행거리 기록중!!")
            .setContentIntent(pendingIntent)
        startForeground(1, builder.build())
    }

    @DelicateCoroutinesApi
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null) {
                    curlatitude = location.latitude
                    curlongitude = location.longitude
                    locationXY.add("${curlatitude.toString()}/${curlongitude.toString()}")

                    if (locationXY.size > 1) {
                        intervalDistance = DistanceManager.getDistance(
                            locationXY[locationXY.size - 2].split("/")[0].toDouble(),
                            locationXY[locationXY.size - 2].split("/")[1].toDouble(),
                            curlatitude!!,
                            curlongitude!!
                        )
                        totalDistance = totalDistance.plus(intervalDistance)
                    }
                    curSpeed = intervalDistance / 3
                }
                locationResult.locations
            }
        }
    }

    override fun onDestroy() {
        val now = System.currentTimeMillis()
        val currentTime = Date(now)
        val time = SimpleDateFormat("hh시mm분", Locale.getDefault())
        endTime = time.format(currentTime)

        location = locationXY.toList()
        timeToData = totalTime
        distanceToData = totalDistance.toString()

        val db = AppDatabase.getDatabase(applicationContext)
        Thread {
            db?.historyDao()?.insert(
                History(
                    null,
                    startTime,
                    endTime,
                    timeToData,
                    "$distanceToData m",
                    location
                )
            )
        }.start()

        timerTask?.cancel()
        locationXY.clear()
        curSpeed = 0
        timer = 0
        totalDistance = 0
        totalTime = "0"
        fusedLocationClient?.removeLocationUpdates(locationCallback)
        super.onDestroy()
        stopForeground(true)
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

    object DistanceManager {
        private const val R = 6372.8 * 1000

        fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = kotlin.math.sin(dLat / 2).pow(2.0) + kotlin.math.sin(dLon / 2)
                .pow(2.0) * kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(
                Math.toRadians(
                    lat2
                )
            )
            val c = 2 * kotlin.math.asin(kotlin.math.sqrt(a))
            return (R * c).toInt()
        }
    }

    companion object {
        private const val CHANNEL_ID = "myChannelId"
    }
}
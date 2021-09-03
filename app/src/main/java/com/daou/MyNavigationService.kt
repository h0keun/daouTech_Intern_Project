package com.daou

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.daou.view.MainActivity
import androidx.annotation.Nullable

class MyNavigationService : Service() {

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
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
        val notificationIntent = Intent(baseContext, MainActivity::class.java).apply {
            Intent.ACTION_MAIN
            Intent.CATEGORY_LAUNCHER
            Intent.FLAG_ACTIVITY_NEW_TASK
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

    companion object {
        private const val CHANNEL_ID = "myChannelId"
    }
}
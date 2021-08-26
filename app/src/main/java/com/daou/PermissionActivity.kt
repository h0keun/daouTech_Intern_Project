package com.daou

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class PermissionActivity : AppCompatActivity() {

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        checkStatus()
    }

    @SuppressLint("NewApi")
    private fun checkStatus() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                val goLoginIntent = Intent(this, LoginActivity::class.java)
                startActivity(goLoginIntent)
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
            -> {
                showPermissionContextPopup()
            } // 불필요한 코드인가??
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                showPermissionContextPopup()
            }
            else
            -> {
                requestPermissions(requiredPermissions, REQUEST_PERMISSION)
            }
        }
    }

    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val permissionGranted =
            (requestCode == REQUEST_PERMISSION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)

        if (!permissionGranted) {
            showPermissionContextPopup()
        } else {
            val goLoginIntent = Intent(this, LoginActivity::class.java)
            startActivity(goLoginIntent)
        }
    }

    @SuppressLint("NewApi")
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한을 허용해 주세요!")
            .setMessage("앱 사용을 위해 권한 동의가 필요합니다!")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(requiredPermissions, REQUEST_PERMISSION)
            }
            .create()
            .show()
    }

    companion object {
        private const val REQUEST_PERMISSION = 201
    }
}
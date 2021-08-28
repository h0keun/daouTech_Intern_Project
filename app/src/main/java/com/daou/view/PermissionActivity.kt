package com.daou.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.daou.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {

    private var permissionLoop = 0
    private lateinit var binding: ActivityPermissionBinding

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    private val requireBackgroundPermissions = arrayOf(
        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissionGranted = getSharedPreferences("state",0).getBoolean("state",false)

        if (!permissionGranted) checkVersion()
        else checkStatus()

    }

    private fun checkStatus() {
        val statePermission = getSharedPreferences("state",0).getBoolean("state",false)

        if (statePermission &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            binding.warningText.isVisible = true
            binding.guideText.isVisible = true
        }
    }

    private fun checkVersion() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            checkHighVersionPermission()
        }
    }

    private fun checkHighVersionPermission() {
        ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val permissionGranted =
                (requestCode == REQUEST_PERMISSION &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED)

            if (!permissionGranted) {
                showPermissionContextPopup()
            } else {
                val state = getSharedPreferences("state",0)
                val editor = state.edit()
                editor.putBoolean("state", true)
                editor.apply()

                checkStatus()
            }
        } else {
            when {
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                -> {
                    showPermissionContextPopup()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
                -> {
                    showPermissionContextPopup()
                }
                else
                -> {
                    if (!(requestCode == REQUEST_PERMISSION &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    ) {
                        requestPermissions(requiredPermissions, REQUEST_PERMISSION)
                    } else if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        val state = getSharedPreferences("state",0)
                        val editor = state.edit()
                        editor.putBoolean("state", true)
                        editor.apply()

                        checkStatus()
                    } else {
                        showBackgroundPermissionContextPopup()
                    }
                }
            }
        }
    }


    private fun showPermissionContextPopup() {
        val state = getSharedPreferences("state",0)
        val editor = state.edit()
        editor.putBoolean("state", true)
        editor.apply()

        AlertDialog.Builder(this)
            .setTitle("권한을 허용해 주세요!")
            .setMessage("앱 사용을 위해 권한 동의가 필요합니다!")
            .setPositiveButton("동의") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(requiredPermissions, REQUEST_PERMISSION)
                }
            }
            .create()
            .show()
    }

    private fun backgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissions(requireBackgroundPermissions, REQUEST_BACKGROUND_PERMISSION)
        }
    }

    private fun showBackgroundPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("백그라운드 위치 권한")
            .setMessage("'항상허용' 으로 설정해주세요!!")
            .setPositiveButton("동의") { _, _ ->
                backgroundPermission()
            }
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        permissionLoop++
    }

    override fun onPause() {
        super.onPause()
        if(permissionLoop > 10) finish()
    }

    companion object {
        private const val REQUEST_PERMISSION = 201
        private const val REQUEST_BACKGROUND_PERMISSION = 202
    }

}
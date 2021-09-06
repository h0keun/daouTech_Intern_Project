package com.daou.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.daou.R
import com.daou.data.local.DetailModel
import com.daou.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private var startPoint = LatLng(0.0, 0.0)
    private var endPoint = LatLng(0.0, 0.0)
    private var arrayPoint: MutableList<LatLng> = mutableListOf(LatLng(0.0,0.0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailData = intent.getSerializableExtra("detailModel") as DetailModel

        binding.startTimeMap.text = detailData.start_Time
        binding.endTimeMap.text = detailData.end_Time
        binding.totalDistanceMap.text = detailData.total_Distance

        if(detailData.locationXY.isNullOrEmpty()){
            startPoint = LatLng(0.0,0.0)
            endPoint = LatLng(0.0,0.0)
        }else{
            startPoint = LatLng(
                detailData.locationXY[0].split("/").get(index = 0).toDouble(),
                detailData.locationXY[0].split("/").get(index = 1).toDouble())

            endPoint = LatLng(
                detailData.locationXY[detailData.locationXY.size -1].split("/").get(index = 0).toDouble(),
                detailData.locationXY[detailData.locationXY.size -1].split("/").get(index = 1).toDouble())

            arrayPoint.clear()
            for (i in detailData.locationXY) {
                arrayPoint.add(LatLng(i.split("/")[0].toDouble(), i.split("/")[1].toDouble()))
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {

        val detailData = intent.getSerializableExtra("detailModel") as DetailModel
        Log.d("좌표 체크", "${detailData.locationXY}")

        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(startPoint).title("출발"))
        mMap.addMarker(MarkerOptions().position(endPoint).title("도착"))

        val polylineOptions = PolylineOptions()
            .addAll(arrayPoint)
        mMap.addPolyline(polylineOptions)

        val builder = LatLngBounds.builder()
        builder.include(startPoint)
        builder.include(endPoint)
        val bounds = builder.build()

        val dm = applicationContext.resources.displayMetrics
        val width = dm.widthPixels
        val height = dm.heightPixels

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,width,height,130))
    }
}


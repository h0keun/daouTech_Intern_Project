package com.daou.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.daou.adapter.HistoryAdapter
import com.daou.data.local.AppDatabase
import com.daou.data.local.DetailModel
import com.daou.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initHistoryRecyclerView()
        showHistoryRecyclerView()

    }

    private fun initHistoryRecyclerView() {

        historyAdapter = HistoryAdapter(onItemClicked = { historyModel ->
            val detail = DetailModel(
                start_Time = historyModel.startTime,
                end_Time = historyModel.endTime,
                total_Time = historyModel.totalTime,
                total_Distance = historyModel.totalDistance,
                locationXY = historyModel.locationXY
            )

            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("detailModel", detail)
            startActivity(intent)
        })

        binding.historyList.layoutManager = LinearLayoutManager(this)
        binding.historyList.adapter = historyAdapter

    }

    private fun showHistoryRecyclerView() {
        val db = AppDatabase.getDatabase(applicationContext)
        Thread {
            val data = db!!.historyDao().getAll()
            runOnUiThread {
                historyAdapter.submitList(data)
            }
        }.start()
        // todo 코루틴 사용하기 이 프로젝트에서 쓰는 쓰레드들 전부 코루틴으로 바꾸는게 좋을듯
    }
}
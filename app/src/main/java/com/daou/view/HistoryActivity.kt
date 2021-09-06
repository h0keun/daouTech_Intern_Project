package com.daou.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.daou.R
import com.daou.adapter.HistoryAdapter
import com.daou.data.local.DetailModel
import com.daou.databinding.ActivityHistoryBinding
import com.daou.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        binding.apply {
            lifecycleOwner = this@HistoryActivity
        }

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)//

        viewModel.allHistory.observe(this, Observer {
            it.let{
                historyAdapter.submitList(it.reversed())
            }
        })

        initHistoryRecyclerView()
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
}
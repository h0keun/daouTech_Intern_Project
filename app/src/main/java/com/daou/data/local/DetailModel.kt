package com.daou.data.local

import java.io.Serializable

data class DetailModel(
    val start_Time: String?,
    val end_Time: String?,
    val total_Time: String?,
    val total_Distance: String?,
    val locationXY: List<String>?
): Serializable


//업무일지
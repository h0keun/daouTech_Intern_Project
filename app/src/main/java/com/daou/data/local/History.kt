package com.daou.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "startTime") val startTime: String?,
    @ColumnInfo(name = "endTime") val endTime: String?,
    @ColumnInfo(name = "totalTime") val totalTime: String?,
    @ColumnInfo(name = "totalDistance") val totalDistance: String?,
    @ColumnInfo(name = "location") val locationXY: List<String>?
)

{
    constructor() : this(0,"","","","", locationXY = null)
}
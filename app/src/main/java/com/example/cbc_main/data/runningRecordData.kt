package com.example.cbc_main.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class runningRecordData(
    @ColumnInfo(name = "riding_time") val ridingTime: String?,
    @ColumnInfo(name = "riding_distance") val ridingDistance: Int?,
    @ColumnInfo(name = "riding_speed") val ridingSpeed: Int?,
    @ColumnInfo(name = "riding_kcal") val ridingKcal: Int?
)

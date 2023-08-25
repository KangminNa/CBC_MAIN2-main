package com.example.cbc_main

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RidingRecord(
    var date: String,
    var time: String,
    var distance: String,
    var speed: String,
    var calorie: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

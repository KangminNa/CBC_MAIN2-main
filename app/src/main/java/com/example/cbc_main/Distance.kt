package com.example.cbc_main

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Distance(
    var distance: Int
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

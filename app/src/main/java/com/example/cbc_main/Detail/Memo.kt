package com.example.cbc_main.Detail

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memo(
    var date: String,
    var memo: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

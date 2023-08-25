package com.example.cbc_main

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cbc_main.Detail.Memo
import com.example.cbc_main.Detail.MemoDao

@Database(entities = [Distance::class], version = 1)
abstract class DistanceDatabase: RoomDatabase() {
    abstract fun DistanceDao(): DistanceDao
}
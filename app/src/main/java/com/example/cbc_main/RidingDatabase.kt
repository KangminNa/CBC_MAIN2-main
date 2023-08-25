package com.example.cbc_main

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cbc_main.Detail.Memo
import com.example.cbc_main.Detail.MemoDao

@Database(entities = [RidingRecord::class], version = 1)
abstract class RidingDatabase: RoomDatabase() {
    abstract fun RidingDao(): RidingDao
}
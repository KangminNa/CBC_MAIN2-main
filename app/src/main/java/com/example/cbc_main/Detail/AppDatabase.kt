package com.example.cbc_main.Detail

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cbc_main.RidingDao

@Database(entities = [Memo::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun MemoDao(): MemoDao
}
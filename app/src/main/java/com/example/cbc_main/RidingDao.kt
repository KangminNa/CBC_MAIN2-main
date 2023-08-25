package com.example.cbc_main

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RidingDao {
    @Insert
    fun insert(record: RidingRecord)

    @Query("delete from RidingRecord where date =:date")
    fun deleteRecord(date: String)

    @Query("SELECT time FROM RidingRecord WHERE date= :date")
    fun getTime(date: String): String

    @Query("SELECT distance FROM RidingRecord WHERE date= :date")
    fun getDistance(date: String): String

    @Query("SELECT speed FROM RidingRecord WHERE date= :date")
    fun getSpeed(date: String): String

    @Query("SELECT calorie FROM RidingRecord WHERE date= :date")
    fun getCalorie(date: String): String

    @Query("SELECT date FROM RidingRecord")
    fun getDate(): List<String>
}
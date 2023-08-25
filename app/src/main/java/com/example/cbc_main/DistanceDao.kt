package com.example.cbc_main

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DistanceDao {
    @Insert
    fun insert(distance: Distance)

    @Query("UPDATE Distance SET distance=:distance")
    fun update(distance: Int)

    @Query("SELECT distance FROM Distance ORDER BY distance DESC")
    fun getDistance(): Int

    @Query("DELETE FROM Distance")
    fun deleteDistance()
}
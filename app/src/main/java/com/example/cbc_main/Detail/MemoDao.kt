package com.example.cbc_main.Detail

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemoDao {
    @Query("SELECT * FROM Memo")
    fun getAll(): List<Memo>

    @Query("SELECT memo FROM Memo WHERE date= :date")
    fun getMemo(date: String): String

    @Query("SELECT date FROM Memo")
    fun getDate(): List<String>

    @Insert
    fun insert(todo: Memo)

    @Query("update Memo set memo= :memo where date =:date")
    fun updateMemo(memo: String, date: String)

    @Query("delete from Memo where date =:date")
    fun deleteMemo(date: String)
}
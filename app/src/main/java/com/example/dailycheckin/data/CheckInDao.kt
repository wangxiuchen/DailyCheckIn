package com.example.dailycheckin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CheckInDao {
    @Query("SELECT * FROM check_in_records WHERE date = :date LIMIT 1")
    suspend fun getRecordByDate(date: String): CheckInRecord?

    @Query("SELECT * FROM check_in_records ORDER BY date DESC")
    suspend fun getAllRecords(): List<CheckInRecord>

    // IGNORE 与 date 唯一索引共同保证同一天不会产生两条记录。
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: CheckInRecord): Long
}


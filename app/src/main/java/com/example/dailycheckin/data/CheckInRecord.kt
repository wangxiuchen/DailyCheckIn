package com.example.dailycheckin.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "check_in_records",
    indices = [Index(value = ["date"], unique = true)],
)
data class CheckInRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val checkInTime: String,
    val createdAt: Long,
)


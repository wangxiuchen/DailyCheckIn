package com.example.dailycheckin.repository

import com.example.dailycheckin.data.CheckInDao
import com.example.dailycheckin.data.CheckInRecord
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CheckInRepository(
    private val dao: CheckInDao,
    private val clock: Clock = Clock.systemDefaultZone(),
) {
    suspend fun checkInAndLoad(): List<CheckInRecord> {
        val today = LocalDate.now(clock).toString()
        val time = LocalTime.now(clock).format(TIME_FORMATTER)

        dao.insert(
            CheckInRecord(
                date = today,
                checkInTime = time,
                createdAt = clock.millis(),
            ),
        )

        return dao.getAllRecords()
    }

    companion object {
        private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
    }
}


package com.example.dailycheckin.repository

import com.example.dailycheckin.data.CheckInDao
import com.example.dailycheckin.data.CheckInRecord
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class CheckInResult(
    val records: List<CheckInRecord>,
    val didCreateToday: Boolean,
)

class CheckInRepository(
    private val dao: CheckInDao,
    private val clock: Clock = Clock.systemDefaultZone(),
) {
    suspend fun checkInAndLoad(): CheckInResult {
        val today = LocalDate.now(clock).toString()
        val time = LocalTime.now(clock).format(TIME_FORMATTER)

        val insertedId = dao.insert(
            CheckInRecord(
                date = today,
                checkInTime = time,
                createdAt = clock.millis(),
            ),
        )

        return CheckInResult(
            records = dao.getAllRecords(),
            didCreateToday = insertedId != INSERT_IGNORED,
        )
    }

    companion object {
        private const val INSERT_IGNORED = -1L
        private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
    }
}

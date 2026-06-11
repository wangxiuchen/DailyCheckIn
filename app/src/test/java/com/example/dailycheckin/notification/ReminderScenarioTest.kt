package com.example.dailycheckin.notification

import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Test

// 按用户给出的四个场景，验证一天里到底会弹出哪些打卡提醒。
// 提醒触发时刻：上午 10:00、晚上 22:00。
// 规则：到点时若今天还没打卡就提醒，已打卡则跳过。
class ReminderScenarioTest {
    private val morning = LocalTime.of(10, 0)
    private val evening = LocalTime.of(22, 0)

    // 模拟某个触发时刻：给定今天的打卡时间（null = 全天未打卡），
    // 在该时刻数据库里是否已经存在今天的打卡记录。
    // 打卡发生在 checkInTime；只要触发时刻不早于打卡时刻，记录就已存在。
    private fun hasRecordAt(trigger: LocalTime, checkInTime: LocalTime?): Boolean =
        checkInTime != null && !checkInTime.isAfter(trigger)

    // 返回当天真正会弹出的提醒（按触发时刻），直接调用生产代码里的 shouldShowReminder。
    private fun firedReminders(checkInTime: LocalTime?): List<LocalTime> =
        listOf(morning, evening).filter { trigger ->
            shouldShowReminder(hasRecordForToday = hasRecordAt(trigger, checkInTime))
        }

    @Test
    fun `未打卡 - 早上10点和晚上10点两次通知`() {
        assertEquals(listOf(morning, evening), firedReminders(checkInTime = null))
    }

    @Test
    fun `早上10点之前打卡 - 两次通知都取消`() {
        assertEquals(emptyList<LocalTime>(), firedReminders(checkInTime = LocalTime.of(8, 30)))
    }

    @Test
    fun `早上10点到晚上10点之间打卡 - 只取消晚上10点通知`() {
        assertEquals(listOf(morning), firedReminders(checkInTime = LocalTime.of(15, 0)))
    }

    @Test
    fun `晚上10点之后打卡 - 两次通知完整保留`() {
        assertEquals(listOf(morning, evening), firedReminders(checkInTime = LocalTime.of(23, 0)))
    }

    // 边界补充：正好 10:00 打卡。上午触发时记录已存在；到晚上更已存在，因此两条都跳过，
    // 等同于“早上10点之前打卡”的情形。
    @Test
    fun `恰好早上10点打卡 - 两次通知都取消`() {
        assertEquals(emptyList<LocalTime>(), firedReminders(checkInTime = LocalTime.of(10, 0)))
    }
}

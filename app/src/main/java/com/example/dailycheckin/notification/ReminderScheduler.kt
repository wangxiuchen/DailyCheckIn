package com.example.dailycheckin.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

// 负责把“每天 10 点和 22 点的提醒”登记给 WorkManager。
// WorkManager 自带持久化，手机重启后会自动恢复已登记的任务，无需自己写开机广播。
object ReminderScheduler {
    const val MORNING_HOUR = 10
    const val EVENING_HOUR = 22

    const val WORK_MORNING = "daily_reminder_morning"
    const val WORK_EVENING = "daily_reminder_evening"
    const val KEY_HOUR = "reminder_hour"

    // 根据开关状态统一处理：开启则登记两个提醒，关闭则全部取消。
    fun apply(context: Context, enabled: Boolean) {
        if (enabled) {
            scheduleNext(context, MORNING_HOUR, WORK_MORNING)
            scheduleNext(context, EVENING_HOUR, WORK_EVENING)
        } else {
            cancelAll(context)
        }
    }

    // 登记“下一次”某个整点的提醒。任务触发后会在 Worker 里再登记下一天，形成每日循环。
    fun scheduleNext(context: Context, hour: Int, workName: String) {
        val delay = millisUntilNextHour(hour, LocalDateTime.now())
        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(KEY_HOUR to hour))
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }

    fun cancelAll(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_MORNING)
        WorkManager.getInstance(context).cancelUniqueWork(WORK_EVENING)
    }

    // 根据小时数找回对应的唯一任务名，供 Worker 续约时使用。
    fun workNameForHour(hour: Int): String =
        if (hour == EVENING_HOUR) WORK_EVENING else WORK_MORNING
}

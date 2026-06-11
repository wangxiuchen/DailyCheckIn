package com.example.dailycheckin.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dailycheckin.data.AppDatabase
import com.example.dailycheckin.data.SettingsStore
import java.time.LocalDate

// 到点后执行：判断今天是否已打卡，决定弹不弹提醒，然后登记下一天的同一时间。
class ReminderWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext
        val hour = inputData.getInt(ReminderScheduler.KEY_HOUR, ReminderScheduler.MORNING_HOUR)

        val settings = SettingsStore(context)
        // 开关如果被关掉就什么都不做，也不再续约。
        if (!settings.notificationsEnabled) {
            return Result.success()
        }

        // 查询今天是否已经有打卡记录。
        val today = LocalDate.now().toString()
        val record = AppDatabase.getInstance(context).checkInDao().getRecordByDate(today)

        // 只有今天还没打卡时才提醒；已打卡则跳过（这正是“最后一个通知可取消”的逻辑）。
        if (shouldShowReminder(hasRecordForToday = record != null)) {
            NotificationHelper.showReminder(context, hour)
        }

        // 登记下一天的同一时间，保证每日循环。
        ReminderScheduler.scheduleNext(context, hour, ReminderScheduler.workNameForHour(hour))
        return Result.success()
    }
}

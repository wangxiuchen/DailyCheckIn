package com.example.dailycheckin.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.dailycheckin.MainActivity
import com.example.dailycheckin.R

// 负责通知渠道的创建、弹出提醒、以及清除提醒。
object NotificationHelper {
    private const val CHANNEL_ID = "daily_check_in_reminder"
    private const val CHANNEL_NAME = "打卡提醒"
    private const val REMINDER_NOTIFICATION_ID = 1001

    // Android 8.0+ 必须先建好通知渠道，否则通知不会显示。
    fun ensureChannel(context: Context) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existing = manager.getNotificationChannel(CHANNEL_ID)
        if (existing == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "提醒你今天还没有打卡"
            }
            manager.createNotificationChannel(channel)
        }
    }

    // Android 13+ 需要用户授予通知权限；低版本默认允许。
    fun hasNotificationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 弹出“今天还没打卡”的提醒。hour 用于区分上午/晚上的文案。
    fun showReminder(context: Context, hour: Int) {
        ensureChannel(context)
        if (!hasNotificationPermission(context)) {
            return
        }

        // 点击通知后打开 App，App 会自动完成今天的打卡。
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val text = if (hour < 12) {
            "今天还没打卡，点开记录一下吧"
        } else {
            "今天还没打卡，别断了连续记录哦"
        }

        // 通知主标题固定为这句激励语；具体的打卡提醒作为副文案。
        // App 名“每日打卡”会由系统自动显示在通知顶部，无需重复。
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("自律是为了更好的自己")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context)
            .notify(REMINDER_NOTIFICATION_ID, notification)
    }

    // 清除已经弹出的提醒（用户完成打卡后调用）。
    fun cancelReminder(context: Context) {
        NotificationManagerCompat.from(context).cancel(REMINDER_NOTIFICATION_ID)
    }
}

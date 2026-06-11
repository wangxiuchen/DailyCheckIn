package com.example.dailycheckin.data

import android.content.Context

// 用 SharedPreferences 保存“是否开启打卡提醒”这个开关。
// 默认开启：用户主动要求了提醒功能，安装后即生效（仍受系统通知权限约束）。
class SettingsStore(context: Context) {
    private val prefs =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) {
            prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()
        }

    companion object {
        private const val PREFS_NAME = "daily_check_in_settings"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
    }
}

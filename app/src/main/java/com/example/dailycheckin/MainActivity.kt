package com.example.dailycheckin

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.dailycheckin.data.AppDatabase
import com.example.dailycheckin.data.SettingsStore
import com.example.dailycheckin.notification.NotificationHelper
import com.example.dailycheckin.notification.ReminderScheduler
import com.example.dailycheckin.repository.CheckInRepository
import com.example.dailycheckin.ui.HistoryScreen
import com.example.dailycheckin.ui.HomeScreen
import com.example.dailycheckin.ui.theme.DailyCheckInTheme
import com.example.dailycheckin.viewmodel.CheckInViewModel

class MainActivity : ComponentActivity() {
    private val checkInViewModel: CheckInViewModel by viewModels {
        CheckInViewModel.Factory(
            CheckInRepository(
                AppDatabase.getInstance(applicationContext).checkInDao(),
            ),
        )
    }

    private val settings by lazy { SettingsStore(applicationContext) }

    // 提醒开关的当前状态，驱动首页 UI。
    private var notificationsEnabled by mutableStateOf(true)

    // Android 13+ 申请通知权限的回调。
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                setNotificationsEnabled(true)
            } else {
                // 用户拒绝授权，把开关恢复为关闭，避免“开着却收不到通知”的困惑。
                setNotificationsEnabled(false)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationsEnabled = settings.notificationsEnabled
        // 启动时若开关是开的，确保权限与调度都就位。
        if (notificationsEnabled) {
            ensurePermissionAndSchedule()
        }

        setContent {
            DailyCheckInTheme {
                val state by checkInViewModel.uiState.collectAsState()
                var showHistory by remember { mutableStateOf(false) }

                if (showHistory) {
                    HistoryScreen(
                        state = state,
                        onBack = { showHistory = false },
                    )
                } else {
                    HomeScreen(
                        state = state,
                        onOpenHistory = { showHistory = true },
                        onCheckInAnimationFinished =
                            checkInViewModel::consumeCheckInSuccessAnimation,
                        notificationsEnabled = notificationsEnabled,
                        onToggleNotifications = ::onToggleNotifications,
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkInViewModel.checkInIfNeeded()
        // 用户已经打开了 App，今天会被自动打卡，这里顺手清掉可能还挂着的提醒通知。
        NotificationHelper.cancelReminder(applicationContext)
    }

    // 首页开关被切换时调用。
    private fun onToggleNotifications(enabled: Boolean) {
        if (enabled) {
            ensurePermissionAndSchedule()
        } else {
            setNotificationsEnabled(false)
        }
    }

    // 开启提醒前先确认通知权限：已授权则直接调度，未授权则发起申请。
    private fun ensurePermissionAndSchedule() {
        val needsRequest = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !NotificationHelper.hasNotificationPermission(applicationContext)
        if (needsRequest) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            setNotificationsEnabled(true)
        }
    }

    // 统一更新：保存开关、刷新 UI 状态、登记或取消提醒任务。
    private fun setNotificationsEnabled(enabled: Boolean) {
        settings.notificationsEnabled = enabled
        notificationsEnabled = enabled
        ReminderScheduler.apply(applicationContext, enabled)
    }
}

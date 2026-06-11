package com.example.dailycheckin.notification

// 到点（10:00 或 22:00）时，是否应该弹出打卡提醒的核心规则：
// 今天还没有打卡记录就提醒，已经有记录就跳过。
// 抽成不依赖 Android 的纯函数，方便用 JUnit 直接验证各种场景。
fun shouldShowReminder(hasRecordForToday: Boolean): Boolean = !hasRecordForToday

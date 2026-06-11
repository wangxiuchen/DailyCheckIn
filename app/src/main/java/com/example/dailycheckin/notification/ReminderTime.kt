package com.example.dailycheckin.notification

import java.time.Duration
import java.time.LocalDateTime

// 纯计算逻辑：距离“下一次某个整点”还有多少毫秒。
// 抽成不依赖 Android 的普通函数，方便用 JUnit 直接测试。
fun millisUntilNextHour(hour: Int, now: LocalDateTime): Long {
    // 先取今天的目标时间，例如今天 10:00。
    var next = now.toLocalDate().atTime(hour, 0)
    // 如果今天这个时间已经到了或正好相等，就顺延到明天同一时间。
    if (!next.isAfter(now)) {
        next = next.plusDays(1)
    }
    return Duration.between(now, next).toMillis()
}

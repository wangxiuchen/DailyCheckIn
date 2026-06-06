# 每日打卡 Daily Check-In

[![Build Android APK](https://github.com/wangxiuchen/DailyCheckIn/actions/workflows/build-apk.yml/badge.svg)](https://github.com/wangxiuchen/DailyCheckIn/actions/workflows/build-apk.yml)
![Android 8.0+](https://img.shields.io/badge/Android-8.0%2B-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?logo=kotlin&logoColor=white)
![Version](https://img.shields.io/badge/version-2.4-2D6A4F)

一款简单、安静、完全离线的 Android 每日打卡应用。

每天第一次打开 App 时自动完成打卡，无需寻找按钮，也不会在同一天重复记录。所有数据仅保存在手机本地，不需要账号、网络、服务器或后台常驻。

> 打开即打卡，把注意力留给真正想坚持的事情。

## 核心特性

- **打开即打卡**：当天首次打开 App 时自动记录日期和时间
- **每天仅一条记录**：以本地日期作为唯一标识，重复打开不会重复写入
- **成功反馈动画**：首次打卡完成后显示轻量动画，当天不会重复播放
- **连续打卡统计**：展示当前连续天数、历史最长连续天数和累计天数
- **月度进度**：展示本月打卡天数、完成率和每日状态
- **月历与列表**：支持按月查看，也可以按日期倒序浏览全部记录
- **完全本地存储**：使用 Room 数据库持久化，关闭 App 后记录仍然保留
- **隐私友好**：不申请网络、定位、通知、相机、通讯录或后台权限
- **云端构建**：通过 GitHub Actions 测试并生成 APK，不依赖 Android Studio

## 适合谁

- 想用最少操作记录每日坚持的人
- 不需要账号、社交、提醒或复杂习惯管理的人
- 希望数据只留在自己手机中的人
- 想学习 Kotlin、Jetpack Compose、Room 和 GitHub Actions 的 Android 初学者

## 工作方式

```text
打开 App
   ↓
读取手机本地日期
   ↓
查询今天是否已有记录
   ├─ 没有：写入首次打开时间，并播放成功动画
   └─ 已有：读取原记录，不重复打卡
   ↓
更新连续天数、月度进度和历史记录
```

本项目不会在后台定时打卡。只有用户打开或回到 App 时，才会检查当天是否已有记录。

## 下载 APK

当前项目通过 GitHub Actions 提供可安装的 debug APK：

1. 打开仓库的 [Actions 页面](https://github.com/wangxiuchen/DailyCheckIn/actions)。
2. 选择 `Build Android APK`。
3. 打开最新一次带绿色对勾的构建。
4. 在页面底部找到 `Artifacts`。
5. 下载 `daily-check-in-debug-apk`。
6. 解压 zip，得到 `app-debug.apk`。

GitHub 可能要求登录后才能下载 Actions artifact。Artifact 也可能按 GitHub 的保留策略自动过期，过期后重新运行工作流即可生成。

## 安装到 Android 手机

项目最低支持 Android 8.0（API 26）。

1. 通过微信、QQ、数据线、网盘或邮件把 APK 发送到手机。
2. 在文件管理器中找到并点击 `app-debug.apk`。
3. 如果系统阻止安装，按提示允许当前文件来源安装未知应用。
4. 返回安装页面继续安装。
5. 后续升级请直接覆盖安装，不要先卸载旧版本。

卸载 App 会同时清除手机中的本地打卡记录。安装前请确认 APK 来自本仓库的 GitHub Actions 构建。

## 不安装 Android Studio 也能构建

1. Fork 本仓库。
2. 打开自己仓库的 `Actions` 页面。
3. 选择 `Build Android APK`。
4. 点击 `Run workflow`，分支选择 `main`。
5. 构建成功后，从 `Artifacts` 下载 APK。

每次向 `main` 分支提交代码时，工作流也会自动运行：

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

工作流使用 Ubuntu 和 JDK 17，配置文件位于：

```text
.github/workflows/build-apk.yml
```

## 技术栈

| 类别 | 技术 |
| --- | --- |
| 开发语言 | Kotlin |
| 用户界面 | Jetpack Compose + Material 3 |
| 本地存储 | Room |
| 状态管理 | ViewModel + StateFlow |
| 构建系统 | Gradle Kotlin DSL |
| 代码生成 | KSP |
| 自动化 | GitHub Actions |
| 最低系统 | Android 8.0 / API 26 |
| 目标系统 | Android API 35 |

## 项目结构

```text
DailyCheckIn/
├── .github/workflows/build-apk.yml
├── app/src/main/java/com/example/dailycheckin/
│   ├── data/          # Room 实体、DAO 和数据库
│   ├── repository/    # 自动打卡与记录读取
│   ├── ui/            # 首页、月历和历史记录界面
│   ├── ui/theme/      # 颜色、字体和主题
│   ├── viewmodel/     # 页面状态与统计逻辑
│   └── MainActivity.kt
├── app/src/test/      # 连续打卡和月历单元测试
├── design/            # 可编辑图标源文件
├── gradle/wrapper/    # Gradle Wrapper
└── README.md
```

## 数据与隐私

- 数据保存在 App 的本地 Room 数据库中
- 不上传任何打卡记录
- 不需要注册或登录
- 不包含广告、埋点或第三方统计 SDK
- 不申请网络权限和敏感系统权限
- 清除应用数据或卸载 App 后，记录无法恢复

## 当前版本

当前版本：**2.4**

V2.4 增加了当天首次打卡成功动画，并保证重复打开、页面重组或从历史页返回时不会重复播放。V2 系列还包含新版绿色视觉系统、月历、列表、统计信息和自适应应用图标。

## 参与贡献

欢迎通过 Issue 和 Pull Request 提交问题、改进建议或代码：

1. Fork 本仓库。
2. 创建功能分支。
3. 完成修改并确认 GitHub Actions 构建通过。
4. 提交 Pull Request，说明修改内容和验证方式。

提交代码时请保持项目简单、离线和隐私友好，避免引入不必要的权限、服务端依赖或复杂第三方库。

## 常见问题

### 为什么同一天再次打开没有新的记录？

这是预期行为。每天只保存第一次打开 App 时的打卡时间。

### 为什么以前没有打开 App 的日期没有变绿？

本项目不会自动补签。月历只展示数据库中真实存在的打卡记录。

### 为什么本月完成率不是按整月计算？

完成率按照本月截至今天的天数计算，未来日期不会提前降低完成率。

### App 会在后台自动运行吗？

不会。项目没有后台任务，也不会定时唤醒手机。

### GitHub Actions 构建失败怎么办？

打开失败的工作流，进入红色步骤，查看第一段关键报错。提交 Issue 时请附上报错文本、相关提交和复现步骤。

## 开源许可

当前仓库尚未包含开源许可证。公开仓库并不自动授予他人复制、修改和分发代码的权利。正式开源前，请由项目维护者选择并添加 `LICENSE`，常见选择包括 MIT License 和 Apache License 2.0。

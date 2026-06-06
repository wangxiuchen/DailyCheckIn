# AGENTS.md

## 项目名称

每日自动打卡 App

## 项目一句话总结

这是一个极简 Android 本地 App：**每天第一次打开 App 自动打卡，当天不重复打卡，首页展示状态和统计，历史页展示记录，最终通过 GitHub Actions 云端生成 APK，用户下载后安装到小米 15 自用。**

---

## 项目背景

用户想做一个个人自用的 Android App，用于每日自动打卡。

用户是开发新手，本机电脑没有任何 Android 开发环境，并且用户不想安装 Android Studio。
因此，本项目必须优先采用：

> Codex 创建和维护代码
> GitHub 托管项目
> GitHub Actions 云端构建 APK
> 用户从 GitHub Actions 页面下载 APK
> 用户手动安装到小米 15

请不要要求用户安装 Android Studio。
请不要要求用户配置 Android SDK。
请不要要求用户本机运行 Gradle。
请尽量让用户只通过浏览器、GitHub 网页、Codex 完成第一版交付。

---

## 用户情况

* 用户是 Android 开发新手。
* 用户本机电脑没有开发环境。
* 用户不想安装 Android Studio。
* 用户希望 Codex 尽量完成完整项目。
* 用户希望通过云端构建获得 APK。
* 用户最终只需要自己安装使用，不需要上架应用商店。
* 目标手机：小米 15。
* 目标产物：可安装的 Android APK。
* 使用方式：个人自用。

---

## 核心目标

创建一个 Android 原生 App，实现以下能力：

1. 用户打开 App。
2. App 自动判断今天是否已经打卡。
3. 如果今天没有打卡，自动创建今天的打卡记录。
4. 如果今天已经打卡，不重复创建记录。
5. 首页展示今日打卡状态、今日打卡时间、当前连续打卡天数、累计打卡天数。
6. 提供历史记录页面，展示所有打卡记录。
7. 数据保存在手机本地，关闭 App 后再打开仍然存在。
8. 不需要联网即可使用。
9. 不需要登录。
10. 不需要服务器。
11. 不需要后台常驻。
12. 最终通过 GitHub Actions 自动构建 APK。

---

## 第一版 MVP 范围

第一版只做以下功能。

### 必须实现

* 打开 App 自动打卡。
* 每天最多生成一条打卡记录。
* 首页展示：

  * App 标题：每日自动打卡
  * 今日状态：今日已打卡 ✅
  * 今日打卡时间，例如：打卡时间：08:32
  * 当前连续打卡天数，例如：已连续打卡 12 天
  * 累计打卡天数，例如：累计打卡 36 天
  * 查看历史记录按钮
* 历史记录页面：

  * 标题：打卡记录
  * 按日期倒序展示打卡记录
  * 每条记录展示日期和时间，例如：2026-06-06  08:32
* 本地持久化存储。
* 标准 Android 项目结构。
* GitHub Actions 云端构建 debug APK。
* GitHub Actions 构建成功后上传 APK artifact。
* README.md 说明用户如何：

  * 创建 GitHub 仓库
  * 上传或提交代码
  * 触发 GitHub Actions
  * 下载 APK
  * 安装 APK 到小米 15

### 暂时不要实现

第一版不要实现以下功能：

* 账号登录
* 注册
* 云同步
* 服务器
* 后台自动运行
* 定时自动打卡
* 推送通知
* 桌面小组件
* 补签
* 多习惯打卡
* 主题切换
* 广告
* 会员功能
* 社交分享
* 应用商店上架
* 复杂动画
* 数据导出
* release 签名配置
* Play Store 打包
* CI/CD 发布到应用市场

除非用户明确提出，否则不要主动添加这些功能。

---

## 非目标

本项目第一版不是一个完整商业 App。
本项目第一版不是一个云同步习惯打卡平台。
本项目第一版不是一个后台自动运行工具。
本项目第一版不是一个需要复杂账号系统的产品。
本项目第一版只追求：

> 简单、稳定、可打包、可安装、可本地保存数据。

---

## 技术选型

请优先使用以下技术：

* Android 原生开发
* Kotlin
* Jetpack Compose
* Room 数据库
* Gradle Wrapper
* GitHub Actions
* Debug APK 构建

建议：

* 使用标准 Android Gradle Plugin。
* 使用 Kotlin DSL Gradle 文件。
* 使用现代稳定版本依赖，但不要追求最新实验版本。
* 最低 SDK 选择适合现代 Android 的稳定版本。
* 目标 SDK 选择当前稳定 Android 构建链支持的版本。
* 使用本地日期和本地时间。
* 尽量避免复杂依赖。
* 不需要网络权限。
* 不需要后台权限。
* 不需要定位权限。
* 不需要通知权限。

---

## 重要限制

由于用户不安装 Android Studio，请特别注意：

1. 项目必须可以通过命令行构建。
2. 项目必须包含 `gradlew` 和 `gradlew.bat`。
3. 项目必须包含 Gradle Wrapper 相关文件。
4. GitHub Actions 必须可以直接运行 `./gradlew assembleDebug`。
5. 不要依赖 Android Studio 专属操作。
6. 不要要求用户打开 Android Studio。
7. 不要要求用户本机配置 `ANDROID_HOME`。
8. 不要要求用户本机安装 Android SDK。
9. 不要要求用户本机连接手机调试。
10. 所有构建和打包都应在 GitHub Actions 云端完成。

---

## GitHub Actions 要求

必须添加 GitHub Actions 工作流文件：

```text
.github/workflows/build-apk.yml
```

该工作流需要满足：

1. 支持 push 到 main 分支时自动构建。
2. 支持用户在 GitHub 网页手动触发构建。
3. 使用 Ubuntu runner。
4. 设置 JDK。
5. 给 `gradlew` 添加执行权限。
6. 执行 debug APK 构建。
7. 上传 APK 作为 artifact。

推荐工作流内容：

```yaml
name: Build Android APK

on:
  workflow_dispatch:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout source code
        uses: actions/checkout@v4

      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17

      - name: Make Gradle executable
        run: chmod +x ./gradlew

      - name: Build debug APK
        run: ./gradlew assembleDebug

      - name: Upload APK
        uses: actions/upload-artifact@v4
        with:
          name: daily-check-in-debug-apk
          path: app/build/outputs/apk/debug/*.apk
```

如果项目使用的 Android Gradle Plugin 需要更高版本 JDK，可以调整 JDK 版本，但必须保证 GitHub Actions 可成功构建。

---

## APK 交付方式

第一版交付 debug APK 即可。
debug APK 可用于用户个人自测和自用安装。

不要求第一版配置正式 release 签名。

README.md 中必须写清楚：

1. 打开 GitHub 仓库。
2. 进入 Actions 页面。
3. 选择 `Build Android APK` 工作流。
4. 点击最新一次成功构建。
5. 在页面底部找到 Artifacts。
6. 下载 `daily-check-in-debug-apk`。
7. 解压下载的 zip。
8. 找到 `.apk` 文件。
9. 传输到小米 15。
10. 在小米 15 上允许安装未知来源应用。
11. 点击 APK 安装。
12. 安装后打开 App 验收功能。

---

## 小米 15 安装说明要求

README.md 中需要给新手写清楚小米手机安装 APK 的步骤。

至少包括：

1. 把 APK 文件发送到手机：

   * 微信文件传输助手
   * QQ
   * 数据线
   * 网盘
   * 邮件
   * 任选一种即可
2. 在手机文件管理器中找到 APK。
3. 点击 APK。
4. 如果系统提示“禁止安装未知来源应用”，按照提示进入设置。
5. 允许当前应用安装未知来源应用。
6. 返回后继续安装。
7. 安装成功后打开 App。
8. 如果系统提示安全风险，说明这是未上架应用，用户确认自己生成的 APK 后继续安装。

不要要求用户使用 adb 安装，除非用户主动要求。

---

## 数据模型

建议使用 Room 存储打卡记录。

### CheckInRecord

字段建议：

* `id`: Long，自增主键
* `date`: String，格式为 `yyyy-MM-dd`，必须唯一
* `checkInTime`: String 或 Long，记录具体打卡时间
* `createdAt`: Long，创建时间戳

要求：

* `date` 字段必须有唯一约束。
* 每天只能有一条记录。
* 判断是否已打卡时，以 `date` 字段为准。
* 展示时间时，格式化为 `HH:mm`。
* 展示日期时，格式化为 `yyyy-MM-dd`。

---

## 打卡逻辑

App 启动或进入首页时执行自动打卡逻辑。

流程：

1. 获取手机本地日期 `today`。
2. 查询数据库中是否存在 `date == today` 的记录。
3. 如果存在：

   * 不新增记录。
   * 读取该记录作为今日打卡记录。
4. 如果不存在：

   * 新增一条今日打卡记录。
   * 记录当前时间。
5. 重新计算统计信息。
6. 更新首页 UI。

伪代码：

```kotlin
fun checkInIfNeeded() {
    val today = LocalDate.now()
    val existing = dao.getRecordByDate(today.toString())

    if (existing == null) {
        dao.insert(
            CheckInRecord(
                date = today.toString(),
                checkInTime = currentTime,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    refreshUiState()
}
```

---

## 连续打卡逻辑

当前连续打卡天数计算规则：

1. 获取所有打卡日期，按日期倒序排列。
2. 从今天开始往前检查。
3. 如果今天打卡，连续天数至少为 1。
4. 如果昨天也打卡，连续天数 +1。
5. 继续向前检查，直到遇到某一天没有打卡为止。
6. 如果今天没有打卡，连续天数可以显示为 0；但正常情况下，用户打开 App 后今天会自动打卡，所以首页通常不会出现 0。

示例：

```text
2026-06-06 已打卡
2026-06-05 已打卡
2026-06-04 已打卡
```

连续天数：3

如果：

```text
2026-06-06 已打卡
2026-06-05 未打卡
2026-06-04 已打卡
```

连续天数：1

---

## 页面设计

### 首页

首页尽量简洁。

建议布局：

```text
每日自动打卡

✅ 今日已打卡
打卡时间：08:32

已连续打卡 12 天
累计打卡 36 天

[查看历史记录]
```

要求：

* 打开 App 后直接显示首页。
* 首页自动触发当天打卡。
* 首页不要要求用户手动点击“打卡”按钮。
* 可以有“查看历史记录”按钮。
* 可以显示简单的成功提示，但不是必须。

---

### 历史记录页

建议布局：

```text
打卡记录

2026-06-06    08:32
2026-06-05    09:10
2026-06-04    07:58
```

要求：

* 按日期倒序。
* 如果暂无记录，可以显示“暂无打卡记录”。
* 提供返回首页的方式。
* 不需要编辑或删除记录。

---

## 项目结构建议

请尽量使用清晰、标准的 Android 项目结构，例如：

```text
DailyCheckIn/
├── AGENTS.md
├── README.md
├── settings.gradle.kts
├── build.gradle.kts
├── gradle/
├── gradlew
├── gradlew.bat
├── .github/
│   └── workflows/
│       └── build-apk.yml
└── app/
    ├── build.gradle.kts
    └── src/
        └── main/
            ├── AndroidManifest.xml
            ├── java/
            │   └── com/
            │       └── example/
            │           └── dailycheckin/
            │               ├── MainActivity.kt
            │               ├── data/
            │               │   ├── CheckInRecord.kt
            │               │   ├── CheckInDao.kt
            │               │   └── AppDatabase.kt
            │               ├── repository/
            │               │   └── CheckInRepository.kt
            │               ├── ui/
            │               │   ├── HomeScreen.kt
            │               │   ├── HistoryScreen.kt
            │               │   └── theme/
            │               └── viewmodel/
            │                   └── CheckInViewModel.kt
            └── res/
```

如果为了简化 MVP 需要调整结构，可以调整，但要保持清晰。

---

## 代码风格要求

* 使用 Kotlin。
* 使用 Jetpack Compose。
* 使用简单清晰的命名。
* 不要过度抽象。
* 不要为了架构复杂而复杂。
* 新手能看懂优先。
* 每个关键文件建议加少量中文注释。
* 避免引入不必要的第三方库。
* 如果使用 Room，请确保 KSP 或 kapt 配置正确。
* 代码应能通过 GitHub Actions 编译。
* 优先保证 `./gradlew assembleDebug` 成功。

---

## 权限要求

第一版原则上不需要任何敏感权限。

不要添加以下权限：

* 网络权限
* 定位权限
* 通讯录权限
* 短信权限
* 相机权限
* 麦克风权限
* 通知权限
* 后台运行相关权限

除非代码编译必须，否则不要添加额外权限。

---

## README.md 要求

README.md 必须面向新手，不能假设用户懂 Android 开发。

README.md 至少包括：

1. 项目介绍。
2. 功能说明。
3. 不需要 Android Studio 的说明。
4. 如何创建 GitHub 仓库。
5. 如何上传项目到 GitHub。
6. 如何触发 GitHub Actions 构建。
7. 如何下载 APK artifact。
8. 如何解压 APK。
9. 如何安装到小米 15。
10. 如何验证 App 是否正常。
11. 常见问题。
12. 如果构建失败，应该把 Actions 报错复制给 Codex 继续修复。

不要写“打开 Android Studio”。
不要写“在 Android Studio 中运行”。
不要写“连接手机调试”。
不要写“使用 adb 安装”，除非作为可选高级方案。

---

## GitHub 网页操作说明要求

README.md 中应尽量写成可执行步骤，例如：

```text
1. 打开 GitHub。
2. 点击右上角 +。
3. 点击 New repository。
4. Repository name 输入 DailyCheckIn。
5. 选择 Public 或 Private。
6. 点击 Create repository。
7. 按照 GitHub 页面提示上传项目文件。
8. 上传完成后，点击 Actions。
9. 选择 Build Android APK。
10. 点击 Run workflow。
```

如果某些步骤因为用户使用方式不同无法完全确定，请给出最简单路径。

---

## 验收标准

完成后必须满足：

1. App 可以通过 GitHub Actions 成功构建。
2. GitHub Actions 会上传 APK artifact。
3. 用户可以从 GitHub 网页下载 APK。
4. 用户可以把 APK 安装到小米 15。
5. 第一次打开 App 时自动生成当天打卡记录。
6. 当天第二次打开 App 不会重复生成记录。
7. 第二天打开 App 会生成第二天的新记录。
8. 首页能显示今日打卡时间。
9. 首页能显示当前连续打卡天数。
10. 首页能显示累计打卡天数。
11. 历史记录页面能看到所有打卡记录。
12. 关闭 App 后再次打开，历史数据仍然存在。
13. App 不联网也能正常使用。
14. App 不需要登录。
15. App 不需要任何服务器。
16. App 不需要任何后台常驻能力。
17. App 不需要 Android Studio 才能完成 APK 构建。

---

## 测试建议

请至少考虑以下测试场景。

### 场景 1：首次打开

* 数据库为空。
* 打开 App。
* 自动生成今天记录。
* 首页显示今日已打卡。

### 场景 2：当天重复打开

* 今天已经有记录。
* 重新打开 App。
* 不新增第二条记录。
* 今日打卡时间保持第一次打卡时间。

### 场景 3：连续打卡

* 数据库中有昨天记录。
* 今天首次打开 App。
* 连续天数正确 +1。

### 场景 4：中断后打卡

* 数据库中没有昨天记录。
* 今天首次打开 App。
* 连续天数重置为 1。

### 场景 5：历史记录

* 存在多天记录。
* 历史页按日期倒序显示。

### 场景 6：GitHub Actions 构建

* push 到 main 分支。
* Actions 自动运行。
* `./gradlew assembleDebug` 成功。
* artifact 中包含 APK 文件。

---

## 不确定时的处理原则

如果遇到多个实现选择，请按以下优先级决策：

1. GitHub Actions 能稳定构建。
2. 用户不需要安装 Android Studio。
3. App 能生成 APK。
4. APK 能安装到手机。
5. MVP 功能正确。
6. 代码简单清晰。
7. 依赖少。
8. 后续容易扩展。

不要因为追求高级架构而牺牲可运行性。
不要因为追求美观而增加复杂依赖。
不要因为未来扩展而加入第一版不需要的功能。

---

## Codex 工作方式要求

当用户要求开发时，请按以下顺序工作：

1. 先阅读本 `AGENTS.md`。
2. 确认当前项目结构。
3. 如果项目不存在，创建标准 Android 项目。
4. 实现 MVP 功能。
5. 添加 GitHub Actions 工作流。
6. 补充 README.md。
7. 确保项目支持 `./gradlew assembleDebug`。
8. 如果能运行测试或构建检查，请运行并记录结果。
9. 如果构建失败，根据报错修复。
10. 每次修改后说明改了哪些文件。
11. 给出下一步用户应该怎么操作。

---

## 交付物

最终应包含：

* 完整 Android 项目代码
* `AGENTS.md`
* `README.md`
* `.github/workflows/build-apk.yml`
* 可编译的 Gradle 配置
* App 源码
* GitHub Actions 构建 APK 说明
* GitHub 网页下载 APK 说明
* 小米 15 安装 APK 说明
* 常见问题说明

---

## 最重要的提醒

用户不想安装 Android Studio。
用户不想配置 Android SDK。
用户不想本机运行 Gradle。
用户想要的是：

> Codex 写代码，GitHub Actions 云端打包，自己下载 APK 安装到小米 15 使用。

所有实现和说明都应围绕这个目标展开。

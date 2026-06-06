# 每日自动打卡 App

这是一个个人自用的极简 Android App。每天第一次打开 App 时会自动打卡，当天重复打开不会新增记录。所有数据只保存在手机本地，不需要登录、联网或服务器。

## 第一版功能

- 打开 App 自动记录当天日期和第一次打开时间
- 每天最多保存一条记录
- 首页显示今日状态、打卡时间、连续天数和累计天数
- 历史页按日期倒序显示全部记录
- 使用 Room 数据库在手机本地持久保存
- 不申请网络、定位、通知等敏感权限
- 通过 GitHub Actions 云端生成 debug APK

## 不需要安装 Android Studio

本项目已经包含 Android 源码、Gradle Wrapper 和 GitHub Actions 工作流。正常使用流程是：

1. 把项目上传到 GitHub。
2. 在 GitHub 网页触发云端构建。
3. 从 GitHub 下载 APK。
4. 把 APK 发到小米 15 并安装。

你不需要在电脑上安装 Android Studio、Android SDK，也不需要在本机运行 Gradle。

## 方法一：使用 Codex 和 Git 上传（推荐）

当前目录还不是 Git 仓库时，可让 Codex 继续完成“初始化 Git、提交并发布到 GitHub”。发布前需要先在 GitHub 创建一个空仓库：

1. 打开 [GitHub](https://github.com/) 并登录。
2. 点击右上角 `+`。
3. 点击 `New repository`。
4. `Repository name` 输入 `DailyCheckIn`。
5. 选择 `Public` 或 `Private`，个人自用均可。
6. 不要勾选自动创建 README、`.gitignore` 或 License。
7. 点击 `Create repository`。
8. 保留页面，后续把仓库地址交给 Codex。

## 方法二：只使用 GitHub 网页上传

GitHub 网页不能直接上传空文件夹，因此要确保所有文件都按本项目目录结构上传。文件较多时，使用 Git 提交通常更稳妥。

1. 创建一个名为 `DailyCheckIn` 的空仓库。
2. 在仓库首页点击 `uploading an existing file` 或 `Add file` > `Upload files`。
3. 将项目文件拖入上传区域。
4. 确认 `.github/workflows/build-apk.yml`、`gradle/wrapper` 和 `app/src` 都已包含。
5. 在页面底部填写提交说明，例如 `Create Android MVP`。
6. 点击 `Commit changes`。

注意：GitHub 网页对隐藏目录和复杂目录结构的上传体验有限。若 `.github` 没有成功上传，请让 Codex 使用 Git 方式发布。

## 触发 GitHub Actions 构建

### 自动触发

代码 push 到 `main` 分支后，`Build Android APK` 工作流会自动运行。

### 手动触发

1. 打开 GitHub 上的项目仓库。
2. 点击仓库顶部的 `Actions`。
3. 第一次使用 Actions 时，如果页面要求确认，点击允许工作流运行。
4. 在左侧选择 `Build Android APK`。
5. 点击右侧 `Run workflow`。
6. 分支选择 `main`。
7. 再点击绿色的 `Run workflow`。
8. 等待页面出现新的运行记录。
9. 黄色圆点表示正在构建，绿色对勾表示构建成功，红色叉号表示失败。

工作流会先运行单元测试，再执行：

```text
./gradlew assembleDebug
```

整个过程在 GitHub 的 Ubuntu 云端机器上完成。

## 下载 APK

1. 打开 GitHub 仓库。
2. 进入 `Actions` 页面。
3. 选择 `Build Android APK` 工作流。
4. 点击最新一次带绿色对勾的成功构建。
5. 滚动到页面底部的 `Artifacts` 区域。
6. 点击 `daily-check-in-debug-apk` 下载。
7. 下载得到的是一个 `.zip` 压缩包。
8. 在电脑上解压这个 zip。
9. 解压后找到 `app-debug.apk`。

GitHub 要求登录后才能下载 Actions artifact。如果看不到 Artifacts，请确认打开的是某一次具体的成功运行记录。

## 安装到小米 15

1. 用以下任意一种方式把 `app-debug.apk` 发到手机：
   - 微信文件传输助手
   - QQ
   - 数据线
   - 网盘
   - 邮件
2. 在小米 15 的文件管理器或接收文件的 App 中找到 APK。
3. 点击 `app-debug.apk`。
4. 如果提示“禁止安装未知来源应用”，按页面提示进入设置。
5. 允许当前打开 APK 的应用安装未知来源应用。例如从文件管理器打开，就允许文件管理器安装。
6. 返回上一页，再次点击或继续安装。
7. 如果系统提示安全风险，请确认 APK 是你自己的 GitHub Actions 构建产物，然后继续安装。
8. 安装完成后点击打开。

不同 HyperOS 版本的提示文字可能略有差异，但核心步骤都是允许“当前来源”安装未知应用。

## 验收功能

安装后按以下步骤检查：

1. 第一次打开 App，首页应显示“今日已打卡”和当前时间。
2. 点击“查看历史记录”，应看到今天的一条记录。
3. 关闭并重新打开 App。
4. 今天仍应只有一条记录，时间保持第一次打开时的时间。
5. 第二天再次打开 App 后，应新增第二天记录。
6. 如果两天连续打开，连续打卡天数应增加。
7. 开启飞行模式后 App 仍应正常使用。

卸载 App 会同时删除手机内的本地打卡数据库。覆盖安装同一应用通常会保留数据。

## 项目结构

```text
DailyCheckIn/
├── .github/workflows/build-apk.yml
├── app/
│   ├── build.gradle.kts
│   └── src/
├── gradle/wrapper/
├── gradlew
├── gradlew.bat
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

主要代码：

- `data`：Room 数据表、DAO 和数据库
- `repository`：自动打卡与记录读取
- `viewmodel`：页面状态和连续天数统计
- `ui`：首页、历史页和主题

## 常见问题

### Actions 没有自动运行

确认代码位于 `main` 分支，并确认仓库中存在 `.github/workflows/build-apk.yml`。也可以进入 Actions 页面手动点击 `Run workflow`。

### Actions 构建失败

1. 打开失败的运行记录。
2. 点击红色的失败步骤。
3. 展开日志。
4. 复制从 `FAILURE` 或第一段红色报错附近开始的内容。
5. 把报错发给 Codex，让 Codex根据实际日志继续修复。

### 找不到下载按钮

只有成功完成的构建才会上传 APK。进入带绿色对勾的构建详情，在页面底部找 `Artifacts`。

### 手机阻止安装

这是未上架应用的常见提示。确认 APK 来自你自己的 GitHub 仓库后，按系统提示允许当前文件来源安装未知应用。

### 安装新版后数据是否还在

只要应用包名相同，并且没有先卸载旧版，直接覆盖安装通常会保留本地记录。卸载会删除数据。

### App 是否会在后台自动打卡

不会。第一版只在你打开 App 时自动打卡，不申请后台运行权限，也没有定时任务。

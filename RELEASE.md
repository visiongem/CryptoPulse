# 上架 Google Play SOP

> 这是一份完整的发版与上架手册。第一次跑全流程预计耗时 4–8 小时（不算 Play 审核等待）。后续每次发版只需 30 分钟。

---

## 〇、一次性准备

### 1. Play Console 账号

- 访问 https://play.google.com/console/
- 注册个人开发者账号（**$25 一次性费用**，终身有效）
- 完成身份验证（护照 / 身份证）—— 通常 24–48 小时通过
- 完成 D-U-N-S 验证（个人账号可跳过）
- 启用两步验证（**强烈建议**，保护账号）

### 2. 隐私政策线上托管

Play Console 要求隐私政策必须是**可公开访问的 URL**。最简单的方式：用 GitHub Pages 把仓库里的 `PRIVACY.md` 直接渲染。

#### 方式 A：GitHub Pages（最快）

在仓库 Settings → Pages → Source 选 `main` 分支 `/ (root)`。GitHub 会自动给你 `https://visiongem.github.io/CryptoPulse/PRIVACY.html` 这种 URL（实际渲染 `.md` 需要主题，Pages 默认会用 Jekyll）。

更稳妥的做法：在仓库根加 `index.md` 包含隐私政策内容，URL 直接是 `https://visiongem.github.io/CryptoPulse/`。

#### 方式 B：直接用 GitHub Raw（够用）

URL 就是 `https://raw.githubusercontent.com/visiongem/CryptoPulse/main/PRIVACY.md`

Play Console 接受这种 URL。**但用户体验是纯文本**，不美观。

#### 方式 C：自建静态站点（专业）

如果你已经有个人域名 `visiongem.dev`，丢个 `/privacy/cryptopulse` 页面。可后续考虑。

**v1 推荐方式 A 或 B**。

---

## 一、生成签名 Keystore（一次）

```bash
cd ~/PersonalProjects/CryptoPulse
keytool -genkey -v \
    -keystore cryptopulse-release.keystore \
    -alias cryptopulse \
    -keyalg RSA -keysize 2048 -validity 25000
```

按提示填：
- 密钥库口令（**记牢**——丢了就发不了更新）
- 名字 / 组织 / 城市等
- 密钥口令（可以和密钥库口令一致）

**生成的 `cryptopulse-release.keystore` 文件极其重要**：

- ⚠️ **不要提交到 Git**（已在 `.gitignore` 排除）
- ⚠️ **备份到 1Password / iCloud / U 盘**（推荐三处冗余）
- ⚠️ 如果丢了，再也无法给已上架的 App 发更新

把它放到一个稳定位置：

```bash
mv cryptopulse-release.keystore ~/.android/cryptopulse-release.keystore
```

---

## 二、配置签名（一次）

### 1. 创建 `keystore.properties`（不入库）

在项目根目录建文件 `keystore.properties`：

```properties
storeFile=/Users/nia/.android/cryptopulse-release.keystore
storePassword=你设的密钥库口令
keyAlias=cryptopulse
keyPassword=你设的密钥口令
```

确认 `.gitignore` 包含 `keystore.properties`（应该已经有，因为 `.gitignore` 排除了 `local.properties` 一类，建议显式加一行）。

### 2. 修改 `app/build.gradle.kts`

在 `android { }` 块顶部加：

```kotlin
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = java.util.Properties().apply {
    if (keystorePropertiesFile.exists()) {
        load(keystorePropertiesFile.inputStream())
    }
}

android {
    // ... existing ...
    
    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            // ... existing
        }
    }
}
```

### 3. 验证签名

```bash
./gradlew assembleRelease
```

成功输出 → `app/build/outputs/apk/release/app-release.apk`。

---

## 三、构建发版 AAB

Play Store 现在**只接受 AAB 格式**（不要再传 APK）。

```bash
./gradlew bundleRelease
```

产物：`app/build/outputs/bundle/release/app-release.aab`

**验证 AAB**：
```bash
# 用 bundletool 反编译为 APK 看看
# 下载 bundletool: https://github.com/google/bundletool
bundletool build-apks \
    --bundle=app/build/outputs/bundle/release/app-release.aab \
    --output=app-release.apks
bundletool install-apks --apks=app-release.apks
```

如果能装上手机正常运行 → 可以上架。

---

## 四、Play Console 创建应用

### 1. 进 Console → Create App

- App name：`CryptoPulse`
- Default language：English (United States)（默认）
- App / Game：**App**
- Free / Paid：**Free**
- 同意三项政策声明（developer program policies, US export laws, Play Console Terms）

### 2. Dashboard 任务清单

Play Console 会列一堆任务，按顺序完成：

#### App access
- 是否需要登录访问？→ **No, all functionality is available without restrictions**

#### Ads
- 是否含广告？→ **No**

#### Content rating
- 填问卷（约 5 分钟），CryptoPulse 没敏感内容，全选 No
- 通常评级 **Everyone**

#### Target audience
- 目标年龄段：**13+**
- 是否吸引儿童？→ **No**

#### Data safety
- ⭐ **关键项** ⭐
- 是否收集数据？→ **No, my app does not collect or share any user data**
- 是否处理用户数据？→ **No**
- App 数据是否加密传输？→ Yes (HTTPS + WSS)
- 用户能否请求删除数据？→ N/A（不收集）

#### News app
- 是否新闻类？→ **No**

#### Government app
- 政府类？→ **No**

#### Financial app
- 金融类？→ **No**（注意：行情查看类一般不被归为金融 App，除非提供交易 / 钱包功能）

### 3. Store listing

- **App name**：CryptoPulse — Crypto Watchlist（来自 `fastlane/metadata/android/en-US/title.txt`）
- **Short description**：用 `fastlane/.../short_description.txt`
- **Full description**：用 `fastlane/.../full_description.txt`
- **App icon**：512×512 PNG，32-bit（透明背景可选）
- **Feature graphic**：1024×500 PNG
- **Phone screenshots**：至少 2 张，最多 8 张（见 `docs/screenshots.md`）
- **Tablet screenshots**：可跳过
- **Privacy policy**：粘贴你的隐私政策 URL（见上面〇.2）
- **Category**：**Finance** 或 **Tools**（推荐 Tools，避免被归为金融类增加审核）
- **Tags**：crypto, watchlist, widget
- **Contact details**：email + 网站（GitHub 仓库 URL）

中文 listing：同样填一遍，复制 `fastlane/metadata/android/zh-CN/` 下的文案。

---

## 五、上传第一个版本

### 选择 Track

- **Internal testing**（推荐起点）：最多 100 个测试者，10 分钟生效
- **Closed testing**：邀请制，几天生效
- **Open testing**：任何人，几天生效
- **Production**：正式，3-7 天审核

**第一次发版 → 走 Internal testing**：
1. Console → Test → Internal testing → Create new release
2. 上传 AAB
3. Release name：自动填 `1.0.0 (6)`（来自 versionCode）
4. Release notes：粘贴 `fastlane/metadata/android/en-US/changelogs/6.txt`
5. Save → Review release → Start rollout to Internal testing
6. 添加测试者邮箱（自己的、好友的）
7. Console 会给你一个邀请链接，发给测试者

### Internal → Closed → Production 演进

- Internal 测一周，没 crash → **Promote to Closed testing**
- Closed 测 1-2 周 → **Promote to Open testing**（可选，可跳过）
- Open 测一阵 → **Promote to Production**

第一次正式上线 Production 通常需要 **3-7 天审核**。后续更新通常几小时到 1 天。

---

## 六、Fastlane 自动化（可选，每次省 20 分钟）

### 1. 安装

```bash
brew install fastlane
cd ~/PersonalProjects/CryptoPulse
fastlane init
# 选 Android, 跟向导走
```

### 2. 配置 service account

- Console → Setup → API access → Create new service account
- 在 Google Cloud Console 给该账号下载 JSON key
- 放到 `~/.fastlane/cryptopulse-play.json`（不要入库）

### 3. `fastlane/Fastfile`

```ruby
default_platform(:android)

platform :android do
  desc "Upload to Internal testing"
  lane :internal do
    gradle(task: "bundle", build_type: "Release")
    upload_to_play_store(
      track: "internal",
      aab: "app/build/outputs/bundle/release/app-release.aab",
      json_key: "~/.fastlane/cryptopulse-play.json",
      skip_upload_apk: true,
      skip_upload_metadata: false,
      skip_upload_changelogs: false,
      skip_upload_images: false,
      skip_upload_screenshots: false,
    )
  end
  
  desc "Promote Internal to Production"
  lane :promote do
    upload_to_play_store(
      track: "internal",
      track_promote_to: "production",
      json_key: "~/.fastlane/cryptopulse-play.json",
      skip_upload_aab: true,
    )
  end
end
```

### 4. 之后每次发版

```bash
fastlane internal   # 构建 + 上传 internal track
# 测一周
fastlane promote    # 提升到 production
```

Fastlane 会自动读 `fastlane/metadata/android/*/` 同步 listing 文案和截图。

---

## 七、常见拒审 + 应对

| 问题 | 现象 | 修复 |
|------|------|------|
| Privacy policy 404 | URL 打不开 | 用 GitHub Pages 或换 raw URL |
| Data safety 不一致 | 声明不收集但被检测到第三方 SDK | 移除/审核任何分析 SDK |
| 元数据违规 | 描述含夸张词（"best ever"）| 改成中性描述 |
| Crash on launch | Robo test 启动崩溃 | 本地用 release 包测一遍 |
| Permissions 不合理 | 申请了未声明用途的权限 | 删掉未用 permission |

---

## 八、发版后的事

- **监控**：Console → Vitals 看 crash rate、ANR rate
- **Reviews**：及时回复用户评论
- **更新节奏**：建议每 2-4 周一次小版本，保持活跃信号
- **签名备份再确认**：你真的备份了 keystore 吗？

---

## 九、Q&A

**Q：可以不上 Play Store，只发 APK 让大家自己下载吗？**
A：可以。在 GitHub Releases 上传 APK，README 加下载链接。优点：完全自由，无审核。缺点：用户需要开启"未知来源安装"，传播范围有限，不在简历上"装机量"那里加分。

**Q：要不要同时上 F-Droid？**
A：可以。F-Droid 适合 100% 开源应用。提交流程见 https://f-droid.org/en/docs/Submitting_to_F-Droid_Quick_Start_Guide/

**Q：能放 AdMob 广告吗？**
A：技术上可以，但违背 CryptoPulse 的产品定位（PRODUCT.md "不收订阅费"扩展为"不展示广告"）。如果你想盈利，更建议放可选打赏链接到 Patreon / GitHub Sponsors。

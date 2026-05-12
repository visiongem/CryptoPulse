# Play Store 截图脚本

> 8 张截图的精确配方。按顺序录制可以讲述一个完整的产品故事：**功能 → 差异化 → 隐私**。

## 总体设置

- **设备**：Pixel 8 模拟器（1080×2400，420dpi），或真机 6.1"+
- **状态栏清洁**：模拟器开 `demo mode`
  ```bash
  adb shell settings put global sysui_demo_allowed 1
  adb shell am broadcast -a com.android.systemui.demo -e command enter
  adb shell am broadcast -a com.android.systemui.demo -e command clock -e hhmm 0925
  adb shell am broadcast -a com.android.systemui.demo -e command battery -e plugged false -e level 100
  adb shell am broadcast -a com.android.systemui.demo -e command network -e wifi show -e level 4
  adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false
  ```
- **录屏 / 截图**：`adb shell screencap -p /sdcard/shot.png && adb pull /sdcard/shot.png`
  - 或 Android Studio: View → Tool Windows → Logcat → 相机图标
- **数据**：联网真实数据。**禁用涟漪动效**截图前（避免捕捉中间帧）

---

## 8 张截图清单

每张配 **标题** + **场景设置** + **要展示什么**。

### 1️⃣ 行情列表 · 亮色 · 涟漪进行中（hero shot）

- **目的**：核心功能 + 差异化（涟漪）
- **场景**：行情 Tab，搜索栏可见，前 6 个币（BTC/ETH/SOL/BNB/XRP/DOGE）
- **状态**：开启涟漪，刚刷新过、BTC 行**轻微闪绿**（中间帧）
- **截图技巧**：用录屏模式连续 10 帧，挑一帧涟漪 alpha ≈ 0.10 的
- **顶部条幅文字**（PS 合成）："Real-time prices, gently animated"

### 2️⃣ 行情列表 · 暗色 · 全屏

- **目的**：展示暗色主题
- **场景**：行情 Tab，向下滚到第 7-10 个币
- **状态**：暗色，无涟漪
- **顶部条幅**："Built for late-night charting"

### 3️⃣ 详情页 · K 线 · 7D · 十字线激活

- **目的**：详情页 + 图表的精致感
- **场景**：BTC 详情，7D 周期，**手指按在第 5 天位置**，十字线显示
- **截图技巧**：用 adb 模拟点击 + 立即截屏
  ```bash
  adb shell input tap 540 1200 && sleep 0.2 && adb shell screencap -p /sdcard/shot.png
  ```
- **顶部条幅**："Touch anywhere on the chart"

### 4️⃣ 详情页 · K 线 · 1Y · 上涨周期

- **目的**：展示不同时间尺度
- **场景**：BTC 详情，1Y 周期，绿色线（一年上涨）
- **截图技巧**：先切到 1Y，等图表加载完，截屏
- **顶部条幅**："Zoom out to see the bigger picture"

### 5️⃣ 自选 Tab · 含星标

- **目的**：自选功能 + 本地存储
- **场景**：自选 Tab，4-5 个星标的币
- **状态**：所有星都是 amber 金色（已加自选）
- **顶部条幅**："Your watchlist, locally stored"

### 6️⃣ 小组件 · 三档堆叠在桌面

- **目的**：**杀手锏功能**——必须展示
- **场景**：手机桌面（清空其他 app），从上到下三个 widget：
  - 上：Small 单 BTC
  - 中：Medium 三币
  - 下：Large 五币 + sparkline
- **截图技巧**：手动调三个 widget，长按桌面截图
- **顶部条幅**："Home-screen widgets in three sizes"
- ⭐ **这张必须做精，是最强吸引力**

### 7️⃣ 设置 Tab · 涟漪开关

- **目的**：可定制 + 隐私（无任何"账户/邮箱"项）
- **场景**：设置 Tab，外观区 + 关于区都可见
- **状态**：涟漪开关 ON，version 1.0.0
- **顶部条幅**："Adjustable, private by default"

### 8️⃣ 搜索框激活 + 输入"sol"

- **目的**：搜索功能
- **场景**：行情 Tab，搜索栏聚焦，输入框显示"sol"，列表过滤到 Solana
- **截图技巧**：用 adb input
  ```bash
  adb shell input tap 540 200    # 点击搜索栏
  adb shell input text "sol"
  ```
- **顶部条幅**："Find any coin in two letters"

---

## Feature Graphic（必须）

**1024 × 500 px**，横向。Play Store 列表顶部大图。

### 设计建议

```
┌────────────────────────────────────────────────────────────────────┐
│                                                                    │
│   [CryptoPulse logo + 文字]                                        │
│                                                                    │
│   Local-first crypto watchlist                                     │
│   Compose + Glance · Open source                                   │
│                                                                    │
│                                                                    │
│                                          [phone mockup w/ app]    │
└────────────────────────────────────────────────────────────────────┘
```

- 配色：CryptoPulse Cyan (#06B6D4) 渐变到深色背景
- 文字：左侧大标题 + 副标题
- 右侧：手机 mockup 套上 1️⃣ 号截图

工具：
- Figma（推荐，免费）
- Canva（更易上手）
- screenshots.pro（专门做应用商店）

---

## App Icon 精修

当前是占位的脉冲波形。建议升级：

### Vector 优化方向

1. **加圆形背景**：cyan 渐变（#06B6D4 → #0EA5E9），不是纯色
2. **波形优化**：让脉冲线条更细，加发光阴影
3. **添加小元素**：右上角一个**白色小三角**（向上箭头），暗示"涨"
4. **Adaptive**：背景层 + 前景层分离，前景留 33% safe zone

### 不会设计的话

- Fiverr / Upwork 找设计师，**$30-80** 出图
- Brief：
  ```
  Android app icon for "CryptoPulse" — a minimal crypto price watchlist.
  Style: clean, modern, like Robinhood or Apple Stocks.
  Colors: cyan (#06B6D4) base.
  Concept: heartbeat / pulse waveform, suggesting "the pulse of the market".
  Deliverables: 512×512 PNG (Play Store) + vector source + adaptive layers.
  ```

---

## 顶部条幅文字（中文版）

如果做中文 listing 的对应截图：

| # | 中文条幅 |
|---|---------|
| 1 | 实时价格，优雅动效 |
| 2 | 为深夜看盘而生 |
| 3 | 长按图表，精确到分钟 |
| 4 | 从分钟看到年 |
| 5 | 你的自选，只存在你的设备 |
| 6 | 三种尺寸的桌面小组件 |
| 7 | 可调整，默认隐私 |
| 8 | 两个字母找到任何币 |

---

## 文件命名约定（fastlane 兼容）

放到 `fastlane/metadata/android/{en-US,zh-CN}/images/`：

```
phoneScreenshots/
├── 01_markets_light.png
├── 02_markets_dark.png
├── 03_detail_chart_7d.png
├── 04_detail_chart_1y.png
├── 05_watchlist.png
├── 06_widgets_three_sizes.png
├── 07_settings.png
└── 08_search_active.png
featureGraphic/
└── featureGraphic.png  (1024x500)
icon/
└── icon.png  (512x512)
```

fastlane 上传时会自动取这些文件，无需手动选。

---

## 时间估算

| 任务 | 预计耗时 |
|------|---------|
| 配模拟器 demo mode | 10 min |
| 8 张截图录制 | 60 min |
| Feature Graphic 设计 | 60 min（Figma 模板基础上改）|
| App icon 精修（自己做）| 90 min |
| 上传 + 文案补全 | 30 min |
| **总计** | **~4 小时** |

如果外包 icon 和 feature graphic，节省 2.5 小时，但要等设计交付。

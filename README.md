# CryptoPulse

> 一款本地优先、隐私友好的加密资产看板。Android 原生，Compose + Glance。

[![CI](https://github.com/visiongem/CryptoPulse/actions/workflows/ci.yml/badge.svg)](https://github.com/visiongem/CryptoPulse/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](./LICENSE)
[![Privacy](https://img.shields.io/badge/privacy-local--first-06B6D4.svg)](./PRIVACY.md)

## 项目愿景

CryptoPulse 不是又一个 CoinGecko / Binance 客户端。它的设计原则是：

1. **小组件优先** —— 90% 的人加完小组件就再也不开 App 了。我们把小组件做到 S 级。
2. **本地优先** —— 无登录、无账号、无云同步。自选币种存在你的设备上。
3. **极简美学** —— 不堆指标、不抄行情软件那套花里胡哨的配色。
4. **价格涟漪** —— Compose 自绘动效，让数字"活"起来。
5. **工程师友好** —— 全开源、可自部署、API 可换源。

## 功能

| | |
|---|---|
| 📈 行情列表 | CoinGecko REST 拉取 top 50，本地搜索、下拉刷新 |
| ⚡ 实时价格 | Binance Public WebSocket 实时推送，断线指数退避重连 |
| 💚 涟漪动效 | 价格变动时绿/红脉冲，800ms 渐变，可关 |
| ⭐ 自选 | 本地 DataStore 持久化，首装默认 BTC/ETH/SOL/BNB/XRP |
| 📊 K 线详情 | Compose Canvas 自绘，1D/7D/30D/1Y 切换，按住显示十字线 |
| 🧩 桌面小组件 | Glance + WorkManager，自适应 3 档尺寸，大尺寸含 7 天 sparkline |
| 🌗 暗色模式 | Material 3 + GlanceTheme 自动跟随系统 |
| 🔗 Deep Link | 点击小组件直达对应币种详情 |

## 技术栈

- **语言**：Kotlin 2.1 + Coroutines + Serialization
- **UI**：Jetpack Compose（Material 3）
- **小组件**：Jetpack Glance 1.1
- **网络**：Retrofit + OkHttp WebSocket
- **持久化**：DataStore Preferences
- **图片**：Coil
- **后台任务**：WorkManager
- **数据源**：CoinGecko 公共 API + Binance Public WebSocket

## 架构

```
app/src/main/java/io/github/visiongem/cryptopulse/
├── data/
│   ├── local/         DataStore preferences
│   ├── model/         DTO + domain models + mappers
│   ├── network/       Retrofit + WebSocket clients
│   └── repository/    MarketsRepository, ChartRepository, MarketsStore (app-scoped)
├── di/                Manual ServiceLocator (no DI framework)
├── domain/            AppError, LoadResult sealed classes
├── feature/
│   ├── markets/       List + search
│   ├── watchlist/     Filtered favorites
│   ├── detail/        Coin detail + Canvas chart
│   ├── settings/      Preferences UI
│   └── widget/        Glance widget + WorkManager job
├── nav/               NavHost + bottom navigation
├── ui/                Theme + shared components (priceFlash, ErrorState)
└── util/              PriceFormatter, DateFormatter
```

## 开发

### 环境

- Android Studio Ladybug 或更新
- JDK 17
- Gradle 8.11.1（首次同步会自动下载 wrapper）

### 启动

```bash
git clone git@github.com:visiongem/CryptoPulse.git
cd CryptoPulse
# 用 Android Studio 打开根目录，等 Gradle 同步完成后 Run
```

### CI

GitHub Actions 在每个 push / PR 上自动跑：
- `assembleDebug` —— 构建验证
- `testDebugUnitTest` —— 单元测试

## 隐私

[完整隐私政策](./PRIVACY.md) —— 简而言之：**我们什么都不收集**。

## License

[MIT](./LICENSE)

## 文档

| 文件 | 内容 |
|------|------|
| [ROADMAP.md](./ROADMAP.md) | 6 周开发路线图（已完成 v1.0）|
| [PRODUCT.md](./PRODUCT.md) | 产品定位与差异化策略 |
| [PRIVACY.md](./PRIVACY.md) | 隐私政策 |
| [RELEASE.md](./RELEASE.md) | Google Play 上架完整 SOP |
| [docs/screenshots.md](./docs/screenshots.md) | Play Store 截图录制脚本 |
| [docs/v2-ideas.md](./docs/v2-ideas.md) | v2 候选功能与组合建议 |
| [.claude/CLAUDE.md](./.claude/CLAUDE.md) | AI 协作配置 |

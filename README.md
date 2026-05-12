# CryptoPulse

> 一款本地优先、隐私友好的加密资产看板。Android 原生，Compose + Glance。

## 项目愿景

CryptoPulse 不是又一个 CoinGecko / Binance 客户端。它的设计原则是：

1. **小组件优先** —— 90% 的人加完小组件就再也不开 App 了。我们把小组件做到 S 级。
2. **本地优先** —— 无登录、无账号、无云同步。自选币种存在你的设备上。
3. **极简美学** —— 不堆指标、不抄行情软件那套花里胡哨的配色。
4. **价格涟漪** —— Compose 自绘动效，让数字"活"起来。
5. **工程师友好** —— 全开源、可自部署、API 可换源。

## 技术栈

- **语言**：Kotlin 2.1 + Coroutines + Serialization
- **UI**：Jetpack Compose（Material 3）
- **小组件**：Jetpack Glance
- **网络**：Retrofit + OkHttp WebSocket
- **持久化**：DataStore Preferences
- **图片**：Coil
- **数据源**：CoinGecko 公共 API + Binance Public WebSocket

## 开发

### 环境

- Android Studio Ladybug 或更新
- JDK 17
- Gradle 8.11.1（首次同步会自动下载 wrapper）

### 启动

```bash
# 用 Android Studio 直接 Open 项目根目录
# 首次会自动 sync gradle wrapper
```

如果你装了独立 Gradle，可以先运行：

```bash
gradle wrapper
```

## 路线图

见 [ROADMAP.md](./ROADMAP.md)。

## 产品定位

见 [PRODUCT.md](./PRODUCT.md)。

## License

[MIT](./LICENSE)

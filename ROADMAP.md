# Roadmap

> 标准 MVP 计划：6 周完成 v1.0。每周末有一个**可演示**的小成果，避免烂尾。

---

## Week 0：脚手架 ✅

- [x] 项目骨架（Gradle、Compose、主题、launcher icon）
- [x] AI 协作配置（`.claude/` 规则与 skills）
- [x] README / ROADMAP / PRODUCT 文档
- [x] git 初始化
- [ ] **TODO**：在 GitHub 创建仓库后 push

---

## Week 1：自选币种列表（核心 UX）

**目标**：能看到一个币种价格列表，下拉刷新，实时显示涨跌幅。

- [x] 接入 CoinGecko REST API（`/coins/markets`）
- [x] 数据层：Retrofit + Repository + LoadResult 封装
- [x] UI 层：LazyColumn + 币种行 Composable（图标、symbol、价格、24h%）
- [x] 下拉刷新（`PullToRefreshBox`）
- [x] 暗色 / 浅色主题对照
- [x] 错误状态（无网络、超时、API 错误）
- [x] PriceFormatter 单测覆盖

**Demo**：装到手机上，能看到 BTC/ETH/SOL 等热门币的实时价格。

---

## Week 2：实时刷新 + 涟漪动效

**目标**：价格变动有视觉反馈，专属差异化点。

- [x] 接入 Binance Public WebSocket 组合流（`wss://stream.binance.com:9443/stream`）
- [x] WS 框架：callbackFlow 包装、connection 生命周期跟随 Flow collector
- [x] 指数退避重连（2/4/8/16/30s）
- [x] OkHttp pingInterval 心跳保活
- [x] 符号白名单（USDT 配对过滤）防止 Binance 拒绝 sub
- [x] 价格涟漪 `Modifier.priceFlash`：涨绿跌红、800ms 渐变、首次加载不触发
- [ ] 涟漪开关（用户偏好）→ 推到 Week 3 与 settings 一起做

**Demo**：列表里能看到价格实时跳动，动效优雅克制。

---

## Week 3：自选管理 + 搜索 + 设置

**目标**：用户能自定义关注列表 + 底部导航 + 偏好持久化。

- [x] DataStore Preferences：单一 `appPreferences` 承载 watchlist + 用户偏好
- [x] 币种搜索（symbol / name 本地过滤）
- [x] 添加 / 删除自选（star 图标 toggle）
- [x] 默认推荐列表（首次安装：BTC/ETH/SOL/BNB/XRP）
- [x] 底部导航：Markets / Watchlist / Settings
- [x] Settings 页面 + 涟漪动效开关
- [x] `MarketsStore` 应用单例：Markets/Watchlist 共享一份数据 + 一条 WS 连接
- [ ] 排序（拖拽 reorder）→ Week 6 polish

**Demo**：能在三个 Tab 间切换；自选币种持久化；价格涟漪可关。

---

## Week 4：Glance 小组件（杀手锏）

**目标**：3 种尺寸的小组件，比市面上任何 App 都精致。

- [x] 自适应 Glance Widget：small (1 币种) / medium (3) / large (5)，自动响应尺寸
- [x] `SizeMode.Responsive` + `LocalSize.current` 分支渲染
- [x] WorkManager 周期任务（15min）+ 添加时立即刷新
- [x] WidgetDataStore：单独 JSON 快照存于 appPreferences
- [x] 透明中转 Activity 处理点击（规避华为 / EMUI 后台启动限制）
- [x] `GlanceTheme` 自动跟随系统 light / dark
- [x] 自选数据驱动 widget；无自选时回退 top 5 by market cap
- [ ] Sparkline + Bitmap 渲染 → Week 5（配合 K 线）
- [ ] 深度链接到详情页 → Week 5（详情页未做）

**Demo**：能把小组件加到桌面，会自动按尺寸切换布局。

---

## Week 5：详情页 + K 线图（视觉中心）

**目标**：极简 K 线，不堆指标。

- [x] 详情页 + Navigation Compose 路由（`detail/{coinId}`）
- [x] CoinGecko `/coins/{id}/market_chart` 拉历史数据
- [x] Compose Canvas 自绘价格线 + 渐变填充（不用第三方库）
- [x] 周期切换：1D / 7D / 30D / 1Y（pill 风格选择器）
- [x] 单指按下显示十字线 + 顶部价格 / 时间 tooltip（替换 header）
- [x] 涨绿跌红基于首末点比较
- [x] 列表行可点击跳详情（CoinRow.onClick）
- [x] 详情页星标 toggle 同步 watchlist
- [x] 详情页期间底部 NavBar 自动隐藏
- [ ] 双指捏合缩放 → v2 再做
- [ ] Widget sparkline → Week 6 polish

---

## Week 6：打磨 + 上架

**目标**：v1.0 上 Play Store。

- [ ] App Icon 设计（Figma 或委托）
- [ ] Play Store 截图（5-8 张）
- [ ] Play Store 文案（中英双语）
- [ ] 隐私政策（本地优先无需上传）
- [ ] R8 优化检查
- [ ] 内测 → 公开测试 → 上架
- [ ] GitHub README 加 Play Store badge

**Demo**：v1.0 上线，可分享。

---

## v2.0 备选方向（不在 MVP 内）

- 价格提醒（本地通知）
- 模拟交易（学习状态机）
- 多看板（自选 / 热门 / 自定义分组）
- 多语言（en / es / ja）
- 颜色盲友好模式
- Wear OS 同步

---

## 节奏建议

- 每周末截一张 GIF / 短视频，发推记录进度（建公开账号）
- 周一规划周四中检查度，周日复盘
- 卡 2 天以上立刻砍范围，不为完美主义烂尾

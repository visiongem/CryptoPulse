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

## Week 3：自选管理 + 搜索

**目标**：用户能自定义关注列表。

- [ ] 币种搜索（按 symbol / name）
- [ ] 添加 / 删除自选
- [ ] 排序（拖拽 reorder）
- [ ] 本地存储（DataStore Preferences）
- [ ] 默认推荐列表（首次安装）

**Demo**：用户能完全控制看到哪些币。

---

## Week 4：Glance 小组件（杀手锏）

**目标**：3 种尺寸的小组件，比市面上任何 App 都精致。

- [ ] 小尺寸：1×1 单币种（默认 BTC）
- [ ] 中尺寸：2×2 多币种（前 4 个自选）
- [ ] 大尺寸：4×2 自选列表 + 24h 趋势
- [ ] 后台刷新（30s 间隔，前台更密）
- [ ] 点击小组件 deep link 到对应详情页
- [ ] 主题跟随 App（暗 / 亮）
- [ ] Bitmap 回收（应用本项目带走的经验）

**Demo**：能把小组件加到桌面，看起来比 Robinhood 还干净。

---

## Week 5：K 线图（视觉中心）

**目标**：极简 K 线，不堆指标。

- [ ] CoinGecko `/coins/{id}/market_chart` 拉历史数据
- [ ] Compose `Canvas` 自绘 K 线（不用第三方库）
- [ ] 周期切换：1H / 24H / 7D / 30D / 1Y
- [ ] 单指拖动十字线 + 该点价格 tooltip
- [ ] 双指捏合缩放（v2 再做）
- [ ] 颜色用 SignalPositive / SignalNegative

**Demo**：点开任一币种看到 K 线，手势顺滑。

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

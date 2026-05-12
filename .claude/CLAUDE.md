# CryptoPulse — AI 协作说明

## 项目背景

CryptoPulse 是一款本地优先的加密资产看板 Android App。

- 个人作品集项目，目标 6 周完成 v1.0 并上架 Play Store
- 详细路线图见 `ROADMAP.md`，产品定位见 `PRODUCT.md`
- 全开源（MIT），GitHub: `visiongem/CryptoPulse`

## 协作原则

- **回复使用中文**
- **不要过度工程化**：这是个人项目，避免引入复杂抽象（DDD / Clean Arch 完整版 / 复杂多模块）
- **优先生产可演示成果**：宁可粗糙但可运行，不要完美但停留在设计
- **AI 自由代笔代码**：但**架构决策必须先和用户对齐**
- **遇到不确定的产品方向**：先问，不要自己拍板

## 技术约束

- Kotlin 2.1 + JDK 17 + AGP 8.7
- UI：100% Compose（Material 3）；除非必要不引入 View
- 小组件：Glance，禁用 RemoteViews 手写
- 数据：DataStore Preferences；本期不引入 Room
- 网络：Retrofit + OkHttp WebSocket
- 不引入 Hilt：用 manual DI 即可（项目规模不需要）

## 代码风格

- 行宽 120
- 禁止匈牙利命名（`mFoo` / `s_bar`）
- 禁止全限定类名（必须 import）
- 包结构按 feature 分（`feature/markets/...`），不按 layer 分
- 公开 API 必须有 KDoc，私有方法默认不写注释
- 测试用 Kotlin DSL 风格（kotest 或 JUnit5 + AssertJ）

## 设计 Token

- 主色：`PulseCyan` (`#06B6D4`)
- 涨：`SignalPositive` (`#10B981`)
- 跌：`SignalNegative` (`#EF4444`)
- 中性：`SignalNeutral` (`#6B7280`)
- 详见 `app/src/main/java/io/github/visiongem/cryptopulse/ui/theme/Color.kt`

## 不要做

- 不要直接复制 CoinEx 项目的代码——参考思想可以，照抄不可以
- 不要引入 CoinEx 项目的内部规约（CoinExTheme、ButtonMhSpecs 等）——这是新项目，独立设计
- 不要生成假数据 mock 充作真实接入——必须接 CoinGecko / Binance 公共 API
- 不要在没明确需求时新增模块或包

## Git 约定

- 主分支：`main`
- 功能分支：`feat/xxx`、`fix/xxx`、`refactor/xxx`
- 单次 commit 单一职责
- commit message 用 Conventional Commits（`feat:` / `fix:` / `chore:` / `docs:`）
- 不需要 Co-Authored-By（个人项目）

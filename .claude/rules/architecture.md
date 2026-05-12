# 架构规范

## 总原则

- **单 module，按 feature 切包**
- **MVI 风格但不教条**：每个 feature 一个 ViewModel 持有 StateFlow，UI 通过 collectAsState 订阅
- **不引入 Hilt / Koin**：DI 用顶层单例 + Application 持有实例即可
- **测试金字塔**：ViewModel / Repository / Util 100% 单测覆盖，UI 测试只做关键路径

## 包结构

```
io.github.visiongem.cryptopulse/
├── CryptoPulseApp.kt            # Application + 顶层 DI 容器
├── MainActivity.kt              # 唯一 Activity
├── ui/
│   ├── theme/                   # 主题、颜色、字体
│   └── component/               # 跨 feature 通用 Composable
├── data/
│   ├── network/                 # Retrofit 接口 + WebSocket 客户端
│   ├── local/                   # DataStore + 本地存储
│   └── model/                   # API DTO + domain model
├── domain/
│   ├── repository/              # Repository 接口
│   └── usecase/                 # 复杂业务用 UseCase，简单的直接 Repository 暴露
└── feature/
    ├── markets/                 # 行情列表
    ├── detail/                  # 币种详情 + K 线
    ├── watchlist/               # 自选管理
    ├── settings/                # 设置
    └── widget/                  # Glance 小组件
```

## ViewModel 规范

```kotlin
class MarketsViewModel(
    private val repository: MarketsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MarketsUiState())
    val state: StateFlow<MarketsUiState> = _state.asStateFlow()

    fun onAction(action: MarketsAction) {
        when (action) {
            MarketsAction.Refresh -> refresh()
            is MarketsAction.AddToWatchlist -> addToWatchlist(action.coinId)
        }
    }
}

data class MarketsUiState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: String? = null,
)

sealed interface MarketsAction {
    data object Refresh : MarketsAction
    data class AddToWatchlist(val coinId: String) : MarketsAction
}
```

## Repository 规范

- 返回 `Flow<Result<T>>` 或 `suspend fun ... : Result<T>`
- 不抛业务异常，统一用 `Result` 包装
- WebSocket 暴露 `Flow`，订阅 / 取消订阅由 collector 生命周期控制（`collectAsState` 会自动管）

## 错误处理

```kotlin
sealed class AppError {
    data object NoNetwork : AppError()
    data object Timeout : AppError()
    data class Api(val code: Int, val message: String) : AppError()
    data class Unknown(val cause: Throwable) : AppError()
}
```

UI 层把 `AppError` 转成本地化字符串展示。

## 不要引入的东西（明确清单）

- ❌ Hilt / Dagger / Koin（用手动 DI）
- ❌ Room（用 DataStore；数据量真的需要才考虑）
- ❌ RxJava（Coroutines + Flow 足够）
- ❌ EventBus（用 Flow 替代）
- ❌ AAR 私有库（保持构建透明）
- ❌ 多 module（先单 module 跑起来，到 v2 再说）

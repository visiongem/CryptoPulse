# 代码风格

## Kotlin

- 行宽 **120**
- 4 空格缩进，**不用 tab**
- 字符串模板用 `$` 不用 `+` 拼接
- 不可空优于可空（`String` 优于 `String?`），可空必须显式 `?`
- `val` 优于 `var`
- 公开 API：`internal` 优于 `public`（除非真的需要跨 module）

## 命名

- **类**：UpperCamelCase
- **函数 / 变量**：lowerCamelCase
- **常量**：UPPER_SNAKE_CASE
- **Compose 函数**：UpperCamelCase（类似类）
- **预览函数**：在 Composable 名后加 `Preview`，private 修饰

## Compose

```kotlin
@Composable
fun CoinRow(
    coin: Coin,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        // ...
    }
}

@Preview
@Composable
private fun CoinRowPreview() {
    CryptoPulseTheme {
        CoinRow(coin = Coin.sample(), onClick = {})
    }
}
```

规则：
- 第一个参数永远是数据，最后一个永远是 `modifier: Modifier = Modifier`
- 回调命名 `onXxx`（`onClick`, `onRefresh`, `onCoinSelected`）
- 函数内部不创建 `Modifier`，由调用方传入
- 每个 `@Composable` MUST 有对应的 `Preview` 函数
- 复杂参数用 sealed class / data class，不用一堆 boolean 标志

## 资源命名

- **颜色**：`Color.kt` 里命名为业务语义（`PulseCyan`, `SignalPositive`），不用 `Blue500`
- **字符串**：`strings.xml` 命名 `feature_purpose`（`markets_empty_title`）
- **drawable**：`ic_xxx`（图标）/ `bg_xxx`（背景）/ `shape_xxx`（形状）

## 注释

- **默认不写注释**——好的命名是第一注释
- 注释只写 **WHY**，不写 WHAT
- 公开 API 写 KDoc（`@param`, `@return`, `@throws`）
- TODO 必须带 `// TODO(name): desc` 格式

## 测试

```kotlin
class PriceFormatterTest {

    @Test
    fun `formats price with thousand separator`() {
        val result = PriceFormatter.format("12345.678")
        assertThat(result).isEqualTo("12,345.67")
    }

    @Test
    fun `returns zero for null input`() {
        val result = PriceFormatter.format(null)
        assertThat(result).isEqualTo("0.00")
    }
}
```

规则：
- 测试名用 backticks 写完整的英文句子
- 一个测试方法只测一个行为
- 用 AssertJ 风格断言（不要 JUnit 的 `assertEquals`）
- mock 用 mockk，不用 mockito

## 不要做

- ❌ 单字符变量名（`a`, `b`, `i`, `j`）—— 循环也用语义名（`coin`, `index`）
- ❌ 神秘数字 —— 抽常量
- ❌ 多层嵌套 if/else —— 用 early return / sealed when
- ❌ 字符串拼接 —— 用模板或 string resource 占位符
- ❌ println 调试 —— 用 LogUtil（项目封装好后）

---
name: add-rule
description: 用户提出新的编码规则、约定、偏好时，记录到 .claude/rules/ 或 CLAUDE.md。触发词："加条规则"、"以后别这样写"、"记下这个规范"、"这个要写进规范"等。
---

# /add-rule — 添加规则

## 执行流程

1. **确认理解**：复述用户给的规则，确保理解准确。如果模糊或有歧义，先问清楚。

2. **判断归属**：

   | 归属 | 标准 |
   |------|------|
   | `.claude/CLAUDE.md` | 跨多个领域的项目级原则，少而精 |
   | `.claude/rules/architecture.md` | 模块结构、依赖方向、状态管理模式 |
   | `.claude/rules/code-style.md` | 命名、格式、Compose / Kotlin 写法 |
   | `.claude/rules/git.md` | commit / branch / PR 约定 |
   | 新建 `.claude/rules/{topic}.md` | 不属于以上任何一类的独立主题 |

3. **询问用户确认归属**（除非很明显）

4. **写入文件**：
   - 用 Markdown 列表 / 表格 / 小节
   - 保持已有文件风格一致
   - 加 **WHY**（为什么需要这条规则）
   - 给一两个 ✅ / ❌ 示例

5. **报告变更**：列出修改文件 + 新增章节

## 示例

用户："以后 Compose 函数的 modifier 参数必须放最后"

```markdown
## Compose 参数顺序

- ✅ `fun CoinRow(coin: Coin, onClick: () -> Unit, modifier: Modifier = Modifier)`
- ❌ `fun CoinRow(modifier: Modifier, coin: Coin, onClick: () -> Unit)`

**Why**：Compose 官方约定，方便调用方 trailing lambda 之外 chain modifier。
```

## 不要做

- ❌ 不询问就乱归类
- ❌ 把模糊的"用户偏好"提升为强制规则
- ❌ 和已有规则冲突（先指出冲突，问用户保留哪条）

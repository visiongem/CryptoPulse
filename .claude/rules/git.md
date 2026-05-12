# Git 工作流

## 分支

- `main` —— 稳定分支，永远可发布
- `feat/xxx` —— 功能开发
- `fix/xxx` —— bug 修复
- `refactor/xxx` —— 重构
- `chore/xxx` —— 配置 / 文档 / 依赖更新

## Commit 信息

遵循 Conventional Commits：

```
<type>: <subject>

[optional body]
```

- `feat:` 新功能
- `fix:` 修复
- `refactor:` 不影响行为的重构
- `perf:` 性能
- `docs:` 文档
- `style:` 格式（不影响代码逻辑）
- `test:` 测试
- `chore:` 构建 / 工具 / 依赖

示例：

```
feat: add coin markets list with pull-to-refresh
fix: prevent watchlist duplicate when network race
refactor: extract PriceFormatter to util module
```

## 节奏

- 单次 commit 单一职责
- 不堆"WIP" / "fix something"这种 commit
- 每完成一个 ROADMAP 子项就 commit
- 每完成一个 ROADMAP 大项就打 tag（`v0.2`, `v0.3`...）

## PR（即使个人项目也走 PR）

- 个人项目 + 单人开发，但仍建议走 PR
- 好处：CI 跑一遍、commit 历史干净、面试时简历可放 PR 链接
- PR 标题 = 主要 commit 标题
- PR description 写：what changed + why + screenshots（UI 改动必须）

## 不要做

- ❌ `git push --force` 到 main
- ❌ 合并未 review 的 PR 到 main
- ❌ 提交 `local.properties`、API key、签名 keystore
- ❌ commit message 写 "fix bug" 这种废话

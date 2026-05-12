---
name: commit
description: 基于当前 git diff 自动生成 Conventional Commits 风格的 commit message 并提交。当用户说"提交"、"commit"、"提交一下"、"帮我 commit"等时触发。
---

# /commit — 智能提交

## 执行流程

1. 运行以下命令了解当前状态（并行）：
   - `git status`（看暂存与未暂存变动）
   - `git diff --staged`（已暂存变动）
   - `git diff`（未暂存变动）
   - `git log --oneline -10`（最近 10 个 commit 风格参考）

2. 分析所有改动并按 Conventional Commits 起草 commit message：
   - 类型选择：`feat` / `fix` / `refactor` / `perf` / `docs` / `style` / `test` / `chore`
   - subject 单一职责、12 个字以内中文 OR 一句简短英文（**保持本仓库历史风格一致**）
   - 跨多个职责时，提示用户拆分

3. 如果有未暂存的相关文件，提示用户是否一并 add：
   - 跳过 `local.properties`、API key、签名 keystore 等敏感文件
   - 不要用 `git add -A` 或 `git add .`，按文件名加

4. 用 heredoc 提交：
```bash
git commit -m "$(cat <<'EOF'
feat: add coin markets list

支持下拉刷新、按 24h 涨跌幅排序
EOF
)"
```

5. 提交完成后 `git status` 确认成功

## 不要做

- ❌ 跳过 hooks（`--no-verify`）
- ❌ amend 已 push 的 commit
- ❌ 包含敏感文件
- ❌ 加 Co-Authored-By（个人项目）
- ❌ commit message 模糊（"fix something"、"update code"）

## 用户提示

提交完成后简短报告：
- commit hash 前 7 位
- subject 行
- 修改文件数 + 行数

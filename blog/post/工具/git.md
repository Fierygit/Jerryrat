---
title: git使用
date: 2019-09-28
author: Firefly
top: false
cover: true
toc: true
mathjax: false
categories: 
- 工具
tag: git
---

Git常用操作，一文打尽
![](./../images/git_all_01.png)

### 创建版本库
- git clone url 克隆远程版本库
- git init 初始化本地版本库

### 配置
- git config --global user.name 'chengcp' 配置global级别的用户名
- git config --global user.email '1326895569@qq.com' 配置global级别的邮箱
- git config --global -l 查看global级别的配置列表
- git config --global --unset user.name 删除用户名
- git config --global alias.last 'log -1 HEAD' 配置last别名，使用git last将显示最近的一次提交记录

### 添加和删除文件
- git add file1 file2 添加指定文件到暂存区
- git add dir 添加指定目录到暂存区
- git add . 添加当前目录的所有文件到暂存区
- git mv oldname newname 对一个已经追踪过的文件进行改名，同时加入暂存区
- git rm file1 file2 删除工作区文件，同时将这次删除放入暂存区
- git rm --cached file 停止追踪指定文件，但该文件会保留在工作区；tracked变成untracked

### 提交

- git commit file1 file 2 -m message 提交暂存区指定文件到本地仓库
- git commit -m message 提交暂存区所有文件到本地仓库
- git commit -a -m message 自动暂存所有已经追踪过的文件，且提交到本地仓库
git commit --amend -m message 使用一次新的提交，替代上次提交 

### 分支

- git branch 查看所有本地分支
- git branch –r 查看所有远程分支
- git branch –a 查看所有远程和本地分支
- git branch –v 查看本地所有分支最新一次提交信息
- git branch [branch] 新建分支
- git checkout –b [branch] 新建一个分支，并且切换过去
- git branch [branch] [commit] 基于某次提交，建立一个分支
- git branch --track [branch] [remote-branch] 建立一个分支，并且与远程分支建立追踪关系
- git branch --set-upstream [branch] [remote-branch] 在现有的本地分支和远程分支之间建立追踪关系
- git branch –m [old-branch] [new-branch] 重命名分支
- git merge [branch] 把指定分支合并到当前分支
- git chery-pick [commit] [commit] 选择提交，合并进当前分支
- git branch –d [branch] 删除本地分支
- git push origin –d [branch] 删除远程分支
- git checkout [branch] 切换分支
- git checkout - 切换到上一个最近使用过的分支

### 标签

- git tag 列出所有标签
- git show [tag] 查看指定标签信息
- git tag [tag] 给最近一次提交打一个标签
- git tag [tag] [commit] 在某次提交上打一个标签
- git tag –d [tag] 删除本地指定标签
- git push origin –d tag [tag] 删除远程的标签
- git push origin [tag] 推送指定标签
- git push origin --tags 推送所有标签

### 查看信息

- git status 查看文件状态
- git help [command] 获取帮助文档
- git [command] --help 获取帮助文档
- git log 查看当前分支的提交记录
- git log –all 查看所有分支的提交记录
- git log -5 --oneline --graph 查看最近5次提交记录，以单行、树状图形式显示
- git reflog 查看本地所有变更记录
- git diff 查看工作区和暂存区的差异
- git diff -- file 某个文件在工作区和暂存区的差异
- git diff HEAD 工作区和最新一次提交的差异
- git diff --cached 暂存区和HEAD的差异
- git diff branch_a branch_b – file 某文件在两个分支间的差异

### 远程操作

- git remote -v 查看所有远程仓库
- git ls-remote origin 查看远程仓库引用列表
- git remote show origin 查看远程仓库信息
- git fetch origin 拉取远程仓库最新提交
- git pull origin master 拉取远程master，并且合并到本地当前分支
- git remote add upstream url 添加一个新的远程仓库，命名为upstream
- git push origin master 推送到远程origin的master分支
- git push origin --all 推送所有分支到远程仓库
- git remote prune origin 删除远程仓库中不存在的分支

### 撤销

- git reset --soft HEAD 回滚到指定版本，保留工作区和暂存区
- git reset --mixed HEAD 回滚到指定版本，保留工作区，清空暂存区；--mixed是默认参数，即等同于git reset HEAD
- git reset --hard HEAD 回滚到指定版本，清空工作区和暂存区
- git reset HEAD -- file 清空暂存区中某个文件的修改
- git checkout -- file 检出暂存区的文件到工作目录
- git checkout . 检出暂存区的所有文件到工作目录
- git revert HEAD~ 2 回滚到2个祖先提交的版本，同时产生新的提交记录
- git revert --continue 冲突解决，且把修改提交到暂存区后执行回滚，生成一个新的提交
- git revert –abort 取消回滚，回到之前的状态

### 储藏

- git stash 将工作区和暂存区的变更保存到储藏堆栈中，同时工作区和暂存区恢复到HEAD一样
- git stash list 查看储藏列表
- git stash pop 应用最近的一次储藏，并且从储藏栈中移除该条记录
- git stash apply stash@{0} 应用最近的一次储藏，不移除记录；等同于 git stash apply
- git stash pop --index 应用最近一次储藏，--index表示暂存区的变更也会更新，否则只更新工作区变更
- git stash drop stash@{0} 移除储藏记录
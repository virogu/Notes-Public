
批量修改Git仓库已提交的记录中的提交作者邮箱信息

执行 git log 命令，查看提交历史记录，并记住需要修改作者邮箱的提交 ID。

执行以下命令，将需要修改的提交 ID 替换为实际的值：

```cmd
git filter-branch --env-filter '
OLD_EMAIL="原作者邮箱"
CORRECT_NAME="新作者名字"
CORRECT_EMAIL="新作者邮箱"
if [ "$GIT_COMMITTER_EMAIL" = "$OLD_EMAIL" ]
then
    export GIT_COMMITTER_NAME="$CORRECT_NAME"
    export GIT_COMMITTER_EMAIL="$CORRECT_EMAIL"
fi
if [ "$GIT_AUTHOR_EMAIL" = "$OLD_EMAIL" ]
then
    export GIT_AUTHOR_NAME="$CORRECT_NAME"
    export GIT_AUTHOR_EMAIL="$CORRECT_EMAIL"
fi
' --tag-name-filter cat -- --branches --tags
```

该命令会将 Git 仓库中所有分支和标签下的提交历史记录中的指定作者邮箱信息修改为新的作者名字和邮箱信息。

执行命令，强制推送修改后的提交历史记录到远程仓库上。
```
git push --force --tags origin 'refs/heads/*' 
```

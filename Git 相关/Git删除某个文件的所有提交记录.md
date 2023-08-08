Git删除某个文件的所有提交记录

```cmd
git filter-branch --index-filter 'git rm --cached --ignore-unmatch 文件路径' -- --all

git filter-branch --index-filter 'git rm --cached --ignore-unmatch 文件路径1 文件路径2 文件路径3' -- --all
```
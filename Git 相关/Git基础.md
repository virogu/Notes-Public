# Git 基础

Git 是一个开源的分布式版本控制系统，用于敏捷高效地处理任何或小或大的项目。

Git 采用了分布式版本库的方式，不必服务器端软件支持，最终可以使得任何代码的提交者都可以成为“中央代码库”。Git的根本思想和基本工作原理主要是在本地复制一个“代码库”，每次提交的代码均是推送到本地代码库中，节约了由于网络带宽所带来的限制，不至于出现提交一次代码要等待很久的情况。另一方面，一旦中央代码库的服务器出现“崩溃”，那么任何“本地库”均可以还原中央代码库。

# Git 安装配置

在使用Git前我们需要先安装 Git。Git 目前支持 Linux/Unix、Solaris、Mac和 Windows 平台上运行。

Git 各平台安装包下载地址为：http://git-scm.com/downloads

## Windows 平台上安装.

安装包下载地址：http://git-scm.com/download/win

下载完成根据引导安装即可，安装选项根据需求选择，大部分安装选项保持默认的即可

# Git 配置

Git 仓库中有个 config 文件，专门用来配置或读取相应的工作环境变量，目录是当前仓库下 .git\config 文件。

在 Windows 一般都是C:\Users\userName\ .gitconfig

## 用户信息

配置个人的用户名称和电子邮件地址：

```
$ git config --global user.name "name"
$ git config --global user.email test@mail.com
```

如果用了 **--global** 选项，那么更改的配置文件就是全局文件，以后你所有的项目都会默认使用这里配置的用户信息。

如果要在某个特定的项目中使用其他名字或者电邮，只要去掉 --global 选项重新配置即可，新的设定保存在当前项目的 .git/config 文件里。

##  查看配置信息

要检查已有的配置信息，可以使用 git config --list 命令：

```
$ git config --list
```

# Git 工作流程

一般工作流程如下：

- 克隆 Git 资源作为工作目录。
- 在克隆的资源上添加或修改文件。
- 如果其他人修改了，你可以更新资源。
- 在提交前查看修改。
- 提交修改。
- 在修改完成后，如果发现错误，可以撤回提交并再次修改并提交。

下图展示了 Git 的工作流程：

![img](https://www.runoob.com/wp-content/uploads/2015/02/git-process.png)

# Git 创建仓库

## git init

Git 使用 **git init** 命令来初始化一个 Git 仓库，Git 的很多命令都需要在 Git 的仓库中运行，所以 **git init** 是使用 Git 的第一个命令。

在执行完成 **git init** 命令后，Git 仓库会生成一个 .git 目录，该目录包含了资源的所有元数据，其他的项目目录保持不变。

使用当前目录作为Git仓库，我们只需使它初始化。

```
git init
```

该命令执行完后会在当前目录生成一个 .git 目录。

使用我们指定目录作为Git仓库。

```
git init newrepo
```

如果当前目录下有几个文件想要纳入版本控制，需要先用 git add 命令告诉 Git 开始对这些文件进行跟踪，然后提交：

```
$ git add *.java
$ git add README
$ git commit -m '初始化项目版本'
```

以上命令将目录下以 .java结尾及 README 文件提交到仓库中。

## git clone

我们使用 **git clone** 从现有 Git 仓库中拷贝项目。

克隆仓库的命令格式为：

```
git clone <repo>
```

如果我们需要克隆到指定的目录，可以使用以下命令格式：

```
git clone <repo> <directory>
```

比如，要克隆 MPU_MVS_25A1 项目：

```
$ git clone git@codeup.aliyun.com:5ee35787f0e06f96cfd22c79/MVS/MPU_MVS_25A1.git
```

执行该命令后，会在当前目录下创建一个名为 MPU_MVS_25A1 的目录，其中包含一个 .git 的目录，用于保存下载下来的所有版本记录。

如果要自己定义要新建的项目目录名称，可以在上面的命令末尾指定新的名字：

```
$ git clone git@codeup.aliyun.com:5ee35787f0e06f96cfd22c79/MVS/MPU_MVS_25A1.git MPU_MVS_25A1_RENAME
```

# Git 基本操作

Git 常用的是以下 6 个命令：**git clone**、**git push**、**git add** 、**git commit**、**git checkout**、**git pull**。

![img](https://www.runoob.com/wp-content/uploads/2015/02/git-command.jpg)

**说明：**

- workspace：工作区
- staging area：暂存区/缓存区
- local repository：版本库或本地仓库
- remote repository：远程仓库

一个简单的操作步骤：

```
$ git init    
$ git add .    
$ git commit  
```

- git init - 初始化仓库。
- git add . - 添加文件到暂存区。
- git commit - 将暂存区内容添加到仓库中

## 提交与修改

Git 的工作就是创建和保存你的项目的快照及与之后的快照进行对比。

下表列出了有关创建与提交你的项目的快照的命令：

| 命令         | 说明                                     |
| :----------- | :--------------------------------------- |
| `git add`    | 添加文件到仓库                           |
| `git status` | 查看仓库当前的状态，显示有变更的文件。   |
| `git diff`   | 比较文件的不同，即暂存区和工作区的差异。 |
| `git commit` | 提交暂存区到本地仓库。                   |
| `git reset`  | 回退版本。                               |
| `git rm`     | 删除工作区文件。                         |
| `git mv`     | 移动或重命名工作区文件。                 |

## 提交日志

| 命令               | 说明                                 |
| :----------------- | :----------------------------------- |
| `git log`          | 查看历史提交记录                     |
| `git blame <file>` | 以列表形式查看指定文件的历史修改记录 |

## 远程操作

| 命令         | 说明               |
| :----------- | :----------------- |
| `git remote` | 远程仓库操作       |
| `git fetch`  | 从远程获取代码库   |
| `git pull`   | 下载远程代码并合并 |
| `git push`   | 上传远程代码并合并 |

# Git 分支管理

几乎每一种版本控制系统都以某种形式支持分支。使用分支意味着你可以从开发主线上分离开来，然后在不影响主线的同时继续工作。

创建分支命令：

```
git branch (branchname)
```

切换分支命令:

```
git checkout (branchname)
```

当你切换分支的时候，Git 会用该分支的最后提交的快照替换你的工作目录的内容， 所以多个分支不需要多个目录。

合并分支命令:

```
git merge 
```

你可以多次合并到统一分支， 也可以选择在合并之后直接删除被并入的分支。

开始前我们先创建一个测试目录：

```
$ mkdir gitdemo
$ cd gitdemo/
$ git init
Initialized empty Git repository...
$ touch README
$ git add README
$ git commit -m '第一次版本提交'
[master (root-commit) 3b58100] 第一次版本提交
 1 file changed, 0 insertions(+), 0 deletions(-)
 create mode 100644 README
```

------

## Git 分支管理

### 列出分支

列出分支基本命令：

```
git branch
```

没有参数时，**git branch** 会列出你在本地的分支。

```
$ git branch
* master
```

此例的意思就是，我们有一个叫做 **master** 的分支，并且该分支是当前分支。

当你执行 **git init** 的时候，默认情况下 Git 就会为你创建 **master** 分支。

如果我们要手动创建一个分支。执行 **git branch (branchname)** 即可。

```
$ git branch testing
$ git branch
* master
  testing
```

现在我们可以看到，有了一个新分支 **testing**。

当你以此方式在上次提交更新之后创建了新分支，如果后来又有更新提交， 然后又切换到了 **testing** 分支，Git 将还原你的工作目录到你创建分支时候的样子。

接下来演示如何切换分支，我们用 git checkout (branch) 切换到我们要修改的分支。

```
$ ls
README
$ echo 'runoob.com' > test.txt
$ git add .
$ git commit -m 'add test.txt'
[master 3e92c19] add test.txt
 1 file changed, 1 insertion(+)
 create mode 100644 test.txt
$ ls
README        test.txt
$ git checkout testing
Switched to branch 'testing'
$ ls
README
```

当我们切换到 **testing** 分支的时候，我们添加的新文件 test.txt 被移除了。切换回 **master** 分支的时候，它们又重新出现了。

```
$ git checkout master
Switched to branch 'master'
$ ls
README        test.txt
```

我们也可以使用 git checkout -b (branchname) 命令来创建新分支并立即切换到该分支下，从而在该分支中操作。

```
$ git checkout -b newtest
Switched to a new branch 'newtest'
$ git rm test.txt 
rm 'test.txt'
$ ls
README
$ touch newText.txt
$ git add .
$ git commit -am 'removed test.txt、add newText.txt'
[newtest c1501a2] removed test.txt、add newText.txt
 2 files changed, 1 deletion(-)
 create mode 100644 newText.txt
 delete mode 100644 test.txt
$ ls
README        newText.txt
$ git checkout master
Switched to branch 'master'
$ ls
README        test.txt
```

如你所见，我们创建了一个分支，在该分支的上移除了一些文件 test.txt，并添加了 newText.txt 文件，然后切换回我们的主分支，删除的 test.txt 文件又回来了，且新增加的 newText.txt不存在主分支中。

使用分支将工作切分开来，从而让我们能够在不同开发环境中做事，并来回切换。

### 删除分支

删除分支命令：

```
git branch -d (branchname)
```

例如我们要删除 testing 分支：

```
$ git branch
* master
  testing
$ git branch -d testing
Deleted branch testing (was 85fc7e7).
$ git branch
* master
```

### 分支合并

某分支的独立内容，你希望将它合并回到你的主分支。 你可以使用以 `git merge `命令将任何分支合并到当前分支中去：

```
$ git branch
* master
  newtest
$ ls
README        test.txt
$ git merge newtest
Updating 3e92c19..c1501a2
Fast-forward
 newText.txt  | 0
 test.txt   | 1 -
 2 files changed, 1 deletion(-)
 create mode 100644 newText.txt 
 delete mode 100644 test.txt
$ ls
README        newText.txt
```

以上实例中我们将 newtest 分支合并到主分支去，test.txt 文件被删除。

合并完后就可以删除分支:

```
$ git branch -d newtest
Deleted branch newtest (was c1501a2).
```

删除后， 就只剩下 master 分支了：

```
$ git branch
* master
```

### 合并冲突

合并并不仅仅是简单的文件添加、移除的操作，Git 也会合并文件的修改。

执行合并操作时，两个分支中的文件都有修改时，git合并时也会将文件合并，但是如果两个分支都修改了同一个文件的同一部分，那么合并时就很有可能会发生冲突，因为它无法判断哪一个修改是需要的。

比如master分支中 test.txt 文件第一行文本是“11111”，test分支中test.txt 文件第一行文本是“test”，这时在test分支上将master分支合并过来便会冲突。

```
$ git merge master
CONFLICT (add/add): Merge conflict in test.txt
Auto-merging test.txt
Automatic merge failed; fix conflicts and then commit the result.
```

这时候我们就需要自己合并冲突的文件了，打开冲突的文件：

```
<<<<<<< HEAD
test
=======
11111
>>>>>>> master
```

<<<<<<<  到 ======= 之间的部分是当前的修改，=======  到 >>>>>>>之间的部分是传入的修改，此时就需要我们自己判断需要哪一个修改，修改完成，删除这三段标记，然后保存文件。

比如我们只保留master分支传进来的修改，则把文件内容改成这样:

```
11111
```

保存文件后，可以用 `git add` 来告诉Git文件冲突已经解决，然后再提交：

```
git add test.text
git commit
```

也可以使用其他工具来处理合并冲突，比如电脑里如果装了VisualStudioCode的话，则可以用该程序打开冲突的文件，冲突的地方会高亮显示，而且可以快速选择合并修改的方式（使用当前修改、使用传入的修改、保留双方修改）

# Git 查看提交历史

Git 提交历史一般常用两个命令：

- **git log** - 查看历史提交记录。
- **git blame <file>** - 以列表形式查看指定文件的历史修改记录。
- ### git log

- 在使用 Git 提交了若干更新之后，又或者克隆了某个项目，想回顾下提交历史，我们可以使用 **git log** 命令查看。

- 针对我们前一章节的操作，使用 **git log** 命令列出历史提交记录如下：

- ```
  $ git log
  ```

- 我们可以用 --oneline 选项来查看历史记录的简洁的版本。

- ```
  $ git log --oneline
  $ git log --oneline
  ```

- 你也可以用 **--reverse** 参数来逆向显示所有日志。

- ```
  $ git log --reverse --oneline
  ```

- ### git blame

- 如果要查看指定文件的修改记录可以使用 git blame 命令，格式如下：

- ```
  git blame <file>
  ```

- git blame 命令是以列表形式显示修改记录，如下实例：

- ```
  $ git blame README
  ```

# Git 标签

如果你达到一个重要的阶段，并希望永远记住那个特别的提交快照，你可以使用 git tag 给它打上标签。

比如说，我们想为我们的 项目发布一个"1.0"版本。 我们可以用 git tag -a v1.0 命令给最新一次提交打上 "v1.0"的标签。

-a 选项意为"创建一个带注解的标签"。 不用 -a 选项也可以执行的，但它不会记录这标签是啥时候打的，谁打的，也不会让你添加个标签的注解。

```
$ git tag -a v1.0 -m '发布1.0版本'
```

如果我们忘了给某个提交打标签，又将它发布了，我们可以给它追加标签。

例如，假设我们之前发布了提交 0456798，但是那时候忘了给它打标签。 我们现在也可以：

```
$ git tag -a v0.9 0456798 -m '发布0.9版本'
```

如果我们要查看所有标签可以使用以下命令：

```
$ git tag
```



# Git GUI

Git安装时会自带一个GUI管理软件，但是使用不是很方便，可以使用其他Git管理软件来代替，能够很大的提升工作效率

SourceTree 是一个免费的Git图形化管理软件，有Windows和Mac OS的版本，下载地址：[sourcetree ](https://www.sourcetreeapp.com/)




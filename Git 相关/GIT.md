#### 版本控制的作用

  - 备份					
    - 避免万一机器挂了,磁盘坏了等原因导致代码丢失.
  - 多人协作
    - 提交 (push/commit),拉取 (pull/update)
    - 分支 (branch)
    - 重置 (revert)
    - 日志 (log)
    - ...

#### Git 简史

  https://git-scm.com/book/zh/v1/起步-Git-简史

  同生活中的许多伟大事件一样，Git 诞生于一个极富纷争大举创新的年代。Linux 内核开源项目有着为数众广的参与者。绝大多数的 Linux 内核维护工作都花在了提交补丁和保存归档的繁琐事务上（1991－2002年间）。到 2002 年，整个项目组开始启用分布式版本控制系统 BitKeeper 来管理和维护代码。

  到了 2005 年，开发 BitKeeper 的商业公司同 Linux 内核开源社区的合作关系结束，他们收回了免费使用 BitKeeper 的权力。这就迫使 Linux 开源社区（特别是 Linux 的缔造者 Linus Torvalds ）不得不吸取教训，只有开发一套属于自己的版本控制系统才不至于重蹈覆辙。他们对新的系统制订了若干目标：

  - 速度
  - 简单的设计
  - 对非线性开发模式的强力支持（允许上千个并行开发的分支）
  - 完全分布式
  - 有能力高效管理类似 Linux 内核一样的超大规模项目（速度和数据量）

  自诞生于 2005 年以来，Git 日臻成熟完善，在高度易用的同时，仍然保留着初期设定的目标。它的速度飞快，极其适合管理大项目，它还有着令人难以置信的非线性分支管理系统（见第三章），可以应付各种复杂的项目开发需求。
  
#### GIT 基本
- git 是分布式去中心化的 ,意味着"服务器端"的文档结构,管理方法跟客户端完全一致(其实并没有服务器这一说)

- git init  `初始化一个 git 仓库(创建.git 目录)`

- .gitignore  `类似svn ignore.更灵活,不同的工程有不同的配置`

  ```
  # 此为注释 – 将被 Git 忽略
  # 忽略所有 .a 结尾的文件
  *.a
  # 但 lib.a 除外
  !lib.a
  # 仅仅忽略项目根目录下的 TODO 文件，不包括 subdir/TODO
  /TODO
  # 忽略 build/ 目录下的所有文件
  build/
  # 会忽略 doc/notes.txt 但不包括 doc/server/arch.txt
  doc/*.txt
  # 忽略 doc/ 目录下所有扩展名为 txt 的文件
  doc/**/*.txt
  ```

- git add   `跟踪文件,并将其放到暂存`

  - git add *   `暂存当前目录下所有文件`
  - git add *.c `暂存所有.c 文件`
  - git add README  `暂存README 文件`

- git commit    `提交暂存`

  - git commit -a   `提交所有,包括未暂存但是已跟踪的.`

- git clone   `类似 svn checkout,用来 clone 一个仓库`

- git status    `类似svn status 查看当前工作目录的状态`

- ```
  On branch dev_ftd_ftr
  Changes not staged for commit:
    (use "git add <file>..." to update what will be committed)
    (use "git checkout -- <file>..." to discard changes in working directory)
  
          modified:   app/build.gradle
          modified:   app/src/main/java/com/tsinglink/android/mpu/fragment/VideoSettingFragment.java
          modified:   app/src/main/res/layout/fragment_video_setting.xml
          modified:   app/src/main/res/values-en/strings.xml
          modified:   app/src/main/res/values/strings.xml
  
  Untracked files:
    (use "git add <file>..." to include in what will be committed)
  
          app/TSP210_D_LA1_1H/
  
  no changes added to commit (use "git add" and/or "git commit -a")
  
  ```
#### git分支
- git 项目结构
    
    如下图所示,当前项目有两个分支:`master`分支和 `v1.0`分支
    
    有个 `HEAD` 指针始终指着当前工作目录所在的分支.
    
    Git 的 “master” 分支并不是一个特殊分支。 它就跟其它分支完全没有区别。 之所以几乎每一个仓库都有 master 分支，是因为 git init 命令默认创建它，并且大多数人都懒得去改动它。
    
    ![image](https://git-scm.com/book/en/v2/images/branch-and-history.png)
- git branch `branch_name`   `创建一个分支`
    ```
    git branch testing
    ```
    ![image](https://git-scm.com/book/en/v2/images/head-to-master.png)
    
    创建分支后,从当前最新的 HEAD 分支处两条支流,但是 HEAD还是指向 master.
- git checkout testing    `切换到某个分支`
    
    ![image](https://git-scm.com/book/en/v2/images/head-to-testing.png)
- 分支前移

    再做一次提交后,HEAD 随着 testing 都向前移动了.
    
    ![image](https://git-scm.com/book/en/v2/images/advance-testing.png)
    
- 再切到 master  `git checkout master`

    ![image](https://git-scm.com/book/en/v2/images/checkout-master.png)
    
- 再做修改并commit

    ![image](https://git-scm.com/book/en/v2/images/advance-master.png)
- 合并分支  

    `git merge`

    - 快进合并(fast-forward)
    
    ![image](https://git-scm.com/book/en/v2/images/basic-branching-4.png)
    
    ![image](https://git-scm.com/book/en/v2/images/basic-branching-5.png)
    
    - git branch -d `branch_name`  [删除分支]
    ```
    git branch -d hotfix
    Deleted branch hotfix (3a0874c).
    ```
    
    ![image](https://git-scm.com/book/en/v2/images/basic-branching-6.png)
    
    - 找到共同祖先再合并
    
    ![image](https://git-scm.com/book/en/v2/images/basic-merging-2.png)
    
    - 遇到冲突
    ```
    git merge iss53
    Auto-merging index.html
    CONFLICT (content): Merge conflict in index.html
    Automatic merge failed; fix conflicts and then commit the result.
    ```
    - 冲突
    ```
    <<<<<<< HEAD
    first file change from master
    =======
    first file change from dev_fix_bug1
    >>>>>>> dev_fix_bug1
    ```

    这表示 HEAD 所指示的版本（也就是你的 master 分支所在的位置）在这个区段的上半部分（======= 的上半部分），而 dev_fix_bug1 分支所指示的版本在 ======= 的下半部分。 
    这里可以选择使用上半部分还是下半部分,或者重新来写这一行,只要 <<<<<<< , ======= , 和 >>>>>>> 这些行被完全删除后,就没有冲突了。 
    

    
- git branch `查看所有分支`
    ```
    // 查看所有分支的最后一次提交记录
    git branch -v
      iss53   93b412c fix javascript issue
    * master  7a98805 Merge branch 'iss53'
      testing 782fd34 add scott to the author list in the readmes
    ```
#### 远程分支和本地分支
    
多个人同时开发时,会产生不同的分支.本地的称为本地分支,其他人提交过的分支,成为远程分支.远程分支以**指针** `origin/[分支名]`来表示.

`f4265`是本地从服务器 clone 时的提交点.
![image](https://git-scm.com/book/en/v2/images/remote-branches-1.png)

在此之后,本地和远程各提交了两次,形成如下情况:

![image](https://git-scm.com/book/en/v2/images/remote-branches-3.png)

拉取远程仓库
git fetch
```
见上图
```

拉取远程仓库并与本地合并
```
                     A---B---C master on origin
                    /
               D---E---F---G master
                   ^
                   origin/master in your repository
                   
                   
                    A---B---C origin/master
                    /         \
               D---E---F---G---H master
```

推送本地分支到远程
```
$ git push origin serverfix
Counting objects: 24, done.
Delta compression using up to 8 threads.
Compressing objects: 100% (15/15), done.
Writing objects: 100% (24/24), 1.91 KiB | 0 bytes/s, done.
Total 24 (delta 2), reused 0 (delta 0)
To https://github.com/schacon/simplegit
 * [new branch]      serverfix -> serverfix
```
删除远程分支

```
$ git push origin --delete serverfix
To https://github.com/schacon/simplegit
 - [deleted]         serverfix
```
 
#### 标签

Git 可以给历史中的某一个提交打上标签，以示重要。 比较有代表性的是人们会使用这个功能来标记发布结点（v1.0 等等）。 

1. 列出所有标签
```
git tag
```
2. 给当前提交打上轻量级标签
```
git tag v1.0
```
3. 附注标签
```
git tag -a v1.4 -m "提交 v1.4版本.修改记录如下:xxxxx"
```
4. 给历史提交打标签
```
git tag -a v1.2 9fceb02
```
    
#### 分支应用    
> 由于 Git 的分支实质上仅是包含所指对象校验和（长度为 40 的 SHA-1 值字符串）的文件，所以它的创建和销毁都异常高效。 创建一个新分支就相当于往一个文件中写入 41 个字节（40 个字符和 1 个换行符），如此的简单能不快吗？

> 这与过去大多数版本控制系统形成了鲜明的对比，它们在创建分支时，将所有的项目文件都复制一遍，并保存到一个特定的目录。 完成这样繁琐的过程通常需要好几秒钟，有时甚至需要好几分钟。所需时间的长短，完全取决于项目的规模。而在 Git 中，任何规模的项目都能在瞬间创建新分支。 同时，由于每次提交都会记录父对象，所以寻找恰当的合并基础（译注：即共同祖先）也是同样的简单和高效。 这些高效的特性使得 Git 鼓励开发人员频繁地创建和使用分支。


比如我们安卓开发过程中,使用SVN 分支的时候,要拷贝一份工作副本到本地,首先拷贝的过程就很慢,还浪费磁盘空间,拖累电脑速度.

Android studio 在打开一个新的工程时,需要建立文件索引,会很慢,还会生成大量临时文件,这些临时文件都上 G的.由于每个分支都是不同的副本,加起来体积就很大了,磁盘很快被塞满.另外很多的分支还会导致工程目录太多,不好管理.

另外svn 的分支合并也不太友好.涉及到多个文件夹之间切来切去的.

第三方工程 GIT 分支参考
    
    
- ffmpeg    29 个分支

    https://github.com/FFmpeg/FFmpeg

- 微软的vscode 366 个分支

    https://github.com/microsoft/vscode
        

分支开发模式
    

1. 根据开发进度分支

    一个模块开发之前,先创建一个分支.开发,测试完成后,合并到主分支.
    
    每一次发布,都打上一个标签, 当线上项目有 bug 需要紧急修复,在标签基础上创建临时分支来修补这个 bug.修补完成再合并到当前分支和主分支.最后若有必要,删除临时分支

2. 根据产品分支
    
    主分支是公共项目,或者第一个产品项目;其他分支是在主分支基础上后来衍生出来的其他产品项目
    ```
    git branch
      210b
      210c
      210d
      210e
    * master
    ```
    每个产品分支都可以使用第一条开发进度来衍生分支


  
#### 服务器上的 Git - 协议

远程仓库跟本地仓库没有本质区别.服务器上的 git 结构与本地也没有什么不同.

服务器的架设要考虑的实际上是用何种协议来将 git 仓库 与本地仓库保持同步,以及对应的用户权限.

git 支持 4 中种协议来实现同步.
- 本地协议 **本地用**
```
// 从一个已有仓库 clone
git clone ../gittest    
Cloning into 'gittest'...
done.

```

```
// 将本地仓库推送到 git 项目
git remote add local_proj /opt/git/project.git
```
- HTTP 协议 **配置 HTTP 服务**

```
$ git clone https://example.com/gitproject.git
```
- SSH 协议  **服务器端最简单了**

```
git clone ssh://user@server/project.git
```
- GIT 协议
```
一般不用,略...
```

搭建方式:

https://git-scm.com/book/zh/v2/%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%B8%8A%E7%9A%84-Git-%E5%8D%8F%E8%AE%AE

建议:

大型项目使用 ssh 协议;

小型项目使用第三方托管.

托管服务:
github  
oschina     国内的 github
阿里云      
腾讯云      

https://blog.csdn.net/lrcoop/article/details/88599487

#### GIT CUI
https://git-scm.com/downloads/guis


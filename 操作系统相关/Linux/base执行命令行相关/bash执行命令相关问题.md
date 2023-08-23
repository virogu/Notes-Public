# sh 执行问题
## bash: ./xxx.sh: /bin/bash^M: bad interpreter: No such file or directory

解决方案：

* 方法一
```
vim xxx.sh 

进入 xxx.sh后
在底部模式下执行
:set fileformat=unix

后执行
:x
或者
:wq

保存修改
然后就可以执行./***.sh运行脚本了。
```

* 方法二：

  直接执行 `sed -i "s/\r//" xxx.sh` 来转化， 然后就可以执行 `./***.sh` 运行脚本了。

* 方法三：

  直接执行 `dos2unix xxx.sh` 来转化， 然后就可以执行 `./xxx.sh` 运行脚本了.

  如执行报错`bash: dos2unix: command not found`，请使用`busybox dos2unix ***.sh`继续操作。

## bash: ./XXX: No such file or directory/ 没有那个文件或目录
64-bit上运行32-bit软件时，会出现这种问题

需要安装32位支持库
```sh
sudo apt-get install ia32-libs
```
安装这个库可能会提示找不到，安装不了
```sh
没有可用的软件包 ia32-libs，但是它被其它的软件包引用了。
这可能意味着这个缺失的软件包可能已被废弃，
或者只能在其他发布源中找到
然而下列软件包会取代它：
  lib32z1

E: 软件包 ia32-libs 没有可安装候选
```
根据错误提示安装下面这个这个库
```sh
sudo apt-get install lib32z1
```
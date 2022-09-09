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

  直接执行`sed -i "s/\r//" xxx.sh`来转化， 然后就可以执行`./***.sh`运行脚本了。

* 方法三：

  直接执行`dos2unix xxx.sh`来转化， 然后就可以执行`./xxx.sh`运行脚本了.

  如执行报错`bash: dos2unix: command not found`，请使用`busybox dos2unix ***.sh`继续操作。
成功安装了Linux Mint 19操作系统，但是部分界面还是英文的。
由于在Linux Mint 19里去除了Ubuntu的语言支持小工具，使用Cinnamon自身的一个新开发语言配置工具来替换，但是这个工具尚未完善，明明在Linux Mint 19系统中的Firefox等软件还是英文的界面，可却显示中文已经完整安装。所以，我们需要手动来安装所需要的中文语言包，以下在Linux Mint 19系统终端中运行相关指令即可完成。


1. 安装中文语言包
```
$ sudo apt-get install language-pack-zh-hans  language-pack-gnome-zh-hans
```
2. 安装firefox语言包
```
$ sudo apt-get install firefox-locale-zh-hans
```
3. 安装libreoffice语言包
```
$ sudo apt-get install libreoffice-l10n-zh-cn
```
4. 安装thunderbird语言包
```
$ sudo apt-get install thunderbird-locale-zh-hans
```


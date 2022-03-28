
先得添加ubuntu-wine/ppa这个官方的源地址
指令是：
```
sudo add-apt-repository ppa:ubuntu-wine/ppa
```

当然，你如果直接开始安装也是可以的，但是不能获取到最新的wine哦！建议以上的操作。

再更新安装包
安装包的更新指令是：
```
sudo apt-get update
```  

安装wine1.8
```
sudo apt-get install wine1.8
```

第一步，在终端中使用如下命令，添加软件仓库并自动刷新缓存：
```
wget -O- https://deepin-wine.i-m.dev/setup.sh | sh
```

软件仓库添加完毕，会提示使用 apt 命令即可安装微信、QQ 了，与安装 Linux 原生软件一样

第二步，在终端中使用 apt 命令安装微信、QQ 等软件，以下以微信为例。安装其他软件只需使用相应的软件名称即可。
```
sudo apt-get install com.qq.weixin.deepin
```
安装后需要注销重登录才能显示应用图标。

常用应用的软件包名如下
|应用   |	包名
|----   |----|
|TIM    |com.qq.office.deepin
|QQ     |com.qq.im.deepin
|微信   |com.qq.weixin.deepin
|钉钉   |com.dingtalk.deepin

完整列表见 https://deepin-wine.i-m.dev/


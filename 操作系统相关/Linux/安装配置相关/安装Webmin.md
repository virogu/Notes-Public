
[原文链接](https://www.cnblogs.com/a5idc/p/13962022.html)

在本教程中，我们将向您展示如何在香港服务器Linux Mint 20系统上安装Webmin。Webmin是用于管理VPS的免费控制面板。Webmin是基于Web的界面，用于管理VPS Web托管服务器。借助Webmin，您可以设置用户帐户，apache，DNS和文件共享以及其他操作。Webmin非常适合对Unix或Linux命令行不太了解的初学者。

步骤1.在运行以下教程之前，重要的是通过apt在终端中运行以下命令来确保系统是最新的：
```
sudo apt update
sudo apt install gnupg2
```

步骤2.在Linux Mint 20上安装Webmin。
现在，我们在Linux Mint系统上添加新的Webmin存储库：
```
sudo nano /etc/apt/sources.list
```
然后，将此行添加到文件底部以添加新的存储库：
```
deb http://download.webmin.com/download/repository sarge contrib
```
启用存储库后，使用以下命令安装Webmin：
```
wget -q -O- http://www.webmin.com/jcameron-key.asc | sudo apt-key add
sudo apt update
sudo apt install webmin
```
此过程将需要一些时间，具体取决于您的互联网速度。我建议您有耐心，不要强行关闭安装，否则将破坏安装。

步骤3.配置防火墙。
默认情况下，Webmin侦听所有网络接口上端口10000上的连接。如果您的服务器运行UFW防火墙，则需要打开Webmin端口：
```
sudo ufw allow 10000/tcp
```

步骤4.访问Webmin界面。
最后，将Webmin安装在Linux Mint系统上，启动Web浏览器，然后输入服务器的域名或公共IP地址，然后带上Webmin端口10000：
你的域名或者ip:10000
至此，您已经成功安装了Webmin。
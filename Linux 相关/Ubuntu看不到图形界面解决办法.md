## ubuntu 看不到图形界面桌面的解决办法

1、使用Ctrl + Alt + F1组合键进入字符命令行界面

2、试试 restartx

3、如果第2条解决不了，再试试 sudo service lightdm restart 重启 lightdm

4、如果第3条解决不了，再试试 sudo dpkg-reconfigre lightdm 选择 lightdm

5、如果第4条解决不了，就只能重装 unity 了

sudo apt-get update

sudo apt-get install --reinstall ubuntu-desktop

sudo apt-get install unity


# 删除linux mint自带openjdk
先运行

`sudo update-alternatives --display java`

查看java的路径

然后运行

`sudo update-alternatives --remove "java" "/usr/lib/jvm/jdk/bin/java"`


查看javac的路径

`sudo update-alternatives --display javac`


然后运行

`sudo update-alternatives --remove "javac" "/usr/lib/jvm/jdk/bin/javac"`

查看javaws的路径

`sudo update-alternatives --display javaws`

然后运行

`sudo update-alternatives --remove "javaws" "/usr/lib/jvm/jdk/bin/javaws"`

然后运行
```
java -version
javac -version
which javaws
```
查看这些是不是都没有了


再查看安装的软件包
```
sudo dpkg --list | grep -i jdk
```

删除jdk

`sudo apt-get purge openjdk*`

删除其他的包

`sudo apt-get purge icedtea-* openjdk-*`

`cd /usr/lib/jvm`

`sudo rm -rf jdk`

再次确认是否被删除

`sudo dpkg --list | grep -i jdk`

# 配置新的JDK

`gedit /etc/profile`

最后面添加这些

```
export JAVA_HOME=/home/lb/.jdks/azul-1.8.0_345
export PATH=$PATH:$JAVA_HOME/bin
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```

`source /etc/profile` 使配置生效

`java -version`  查看JDK版本是否生效
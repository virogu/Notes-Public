# linux下卸载之前的qt4

- 打开终端，进入安装Qt的目录
- 运行命令 
    
    `./MaintenanceTool`
​​​​​​​
- 如果没有这个的话，则执行：

    `sudo apt autoremove '.*qt4.*-dev'`

- 还不行的话：
    
    `rm /usr/lib/x86_64-linux-gnu/libQt*`

# 安装Qt5

以安装qt5.9.9为例

1. 从以下网址中下载qt安装包

    [Qt5.12.12下载](https://iso.mirrors.ustc.edu.cn/qtproject/archive/qt/5.12/5.12.12/qt-opensource-linux-x64-5.12.12.run)

2. 打开终端，输入以下命令赋予安装包权限

    `chmod -R 777 qtopensource-linux-x64-5.12.12.run`

3. 输入以下命令开始进行安装

    `./qt-opensource-linux-x64-5.12.12.run`

4. 然后进入到安装页面，并且跟着提示一步一步走就好了（现在安装还要登录账户）

5. 安装完成后不要马上打开qt软件，而是按照如下步骤配置环境变量

   - 在终端输入以下命令
        ```
        cd
        ls -a 
        gedit ./bashrc
        ```
   - 然后在`.bashrc`文件中添加如下内容
        ```
        #其中下面第一行代码中的路劲是qt安装位置所对应的路径
        #32位系统
        export QTDIR=/opt/Qt5.12.12/5.12.12
        export PATH=$QTDIR/gcc/bin:$PATH
        export LD_LIBRARY_PATH=$QTDIR/gcc/lib:$LD_LIBRARY_PATH

        #64位
        export QTDIR=/opt/Qt5.12.12/5.12.12
        export PATH=$QTDIR/gcc_64/bin:$PATH
        export LD_LIBRARY_PATH=$QTDIR/gcc_64/lib:$LD_LIBRARY_PATH
        ```
   - 保存退出，执行以下命令让环境变量生效
        ```
        source ~/.bashrc
        ```
6. 输入以下命令，验证qt是否安装成功
    ```
    qmake -version
    ```
    如果出现qt信息则证明安装成功
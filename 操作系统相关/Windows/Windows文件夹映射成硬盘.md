## Windows文件夹映射成硬盘
- 方法一，使用命令
  
    映射
    将`D:\files`文件夹映射为`E:`盘
    ```cmd
    subst e: d:\files
    ```

    取消映射
    ```cmd
    subst d: /d
    ```
    虚拟盘符可以用h~z任意一个做盘符,不能用已有的盘符的名称,包括光驱盘符，否则会报错。
    
    注意：这种映射重启电脑后会失效

- 方法二，使用共享文件夹映射网络驱动器
  
    [映射网络驱动器](https://blog.csdn.net/bandaoyu/article/details/122746715)
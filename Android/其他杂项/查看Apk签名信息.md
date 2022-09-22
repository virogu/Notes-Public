
# 查看apk签名信息

## 直接用Android-SDK中的工具查看
```
java -jar .\apksigner.jar verify --verbose --print-certs xxx.apk
```

其中 `apksigner.jar` 工具包位置在 `Android-SDK` 目录下面，`Android-SDK\build-tools\xx.x.x\lib`


## 使用JDK中的工具查看

转自：https://blog.csdn.net/findsafety/article/details/25365997

假定安装了JDK，如果想查HelloWorld.apk所使用的签名的fingerprint，可以这样做：

1. 查找apk里的rsa文件

    （Windows）
    ```cmd
    jar tf HelloWorld.apk | findstr RSA
    ```

    （Linux）
    ```cmd
    jar tf HelloWorld.apk | grep RSA
    ```
    输出结果：
    ```cmd
    META-INF/CERT.RSA
    ```

2. 从apk中解压rsa文件
    ```cmd
    jar xf HelloWorld.apk META-INF/CERT.RSA
    ```

3. 获取签名的fingerprints
    ```cmd
    keytool -printcert -file META-INF/CERT.RSA
    ```

    输出结果
    ```cmd
    ...
    Certificate fingerprints:
    MD5: BC:6D:BD:6E:49:69:2A:57:A8:B8:28:89:04:3B:93:A8
    SHA1: 0D:DF:76:F4:85:96:DF:17:C2:68:1D:3D:FF:9B:0F:D2:A1:CF:14:60
    Signature algorithm name: SHA1withRSA
    Version: 3
    ...
    ```

4. 清理工作，删除rsa文件

    （Windows）
    ```cmd
    rmdir /S /Q META-INF
    ```
    
    （Linux）
    ```cmd
    rm -rf META-INF
    ```

如果你想知道两个apk是不是用的同一个签名，那比一下它们签名的MD5码（或SHA1码）是不是一样就行了。
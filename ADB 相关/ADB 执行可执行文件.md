### 1. 推送文件到 /data/local/tmp
例：
```
adb push E:/Test/libXXX.out.so /data/local/tmp
```

### 2. 设置文件权限
例：
```
adb shell
cd /data/local/tmp
chmod 777 libXXX.out.so
```

### 3. 设置工作目录为当前目录：
```
export LD_LIBRARY_PATH=.
```

### 4. 执行可执行文件
```
例：执行 libXXX.out.so
./libXXX.out.so
```



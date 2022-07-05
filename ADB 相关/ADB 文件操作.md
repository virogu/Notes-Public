
删除文件
```cmd
adb shell rm -rf /dir1/test1/test
```

批量删除符合条件的文件
```cmd
adb shell rm -rf /dir1/test1/test*
```

批量删除符合条件的文件（不区分大小写）
```cmd
 adb shell 'find /dir1/test1 -maxdepth 1 -iname "TesT*" -exec rm -rf {} \;'
```

CPU温度
```cmd
adb shell cat /sys/class/thermal/thermal_zone7/temp
```

强制停止程序
```cmd
adb shell am force-stop com.xxxx.android.mirror
```

启动程序
```cmd
adb shell am start com.xxxx.xxxx/com.xxxx.PreviewActivity
```

卸载程序
```cmd
adb uninstall com.xxx.xx
```

```cmd
adb forward tcp:10101 tcp:10101
adb reverse tcp:9537 tcp:9537
```

查看CPU
```cmd
adb shell getprop ro.board.platform
top -m 20 -s cpu -d 5
```

获取处理器信息
```cmd
adb shell getprop ro.board.platform
```

查看相机参数
执行这个adb命令会打印所有默认参数和当前使用参数
```cmd
adb shell dumpsys media.camera
```

查看某一项参数,使用过滤命令grep(linux), windows下可使用findstr来过滤
```cmd
adb shell dumpsys media.camera |grep picture-size
```

将结果保存到文件中
```cmd
adb shell dumpsys media.camera > F:\Data\桌面\camera.txt
```

模拟开关机广播
```cmd
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
```

设置桌面
```cmd
adb shell "cmd package set-home-activity com.xxxx/com.xxx.MainActivity" 
```

点击HOME键
```cmd
adb shell "input keyevent 3"
```

模拟点击屏幕
```cmd
adb shell "input keyboard text 'a text'"
```

模拟输入文字
```cmd
adb shell "input keyevent 3"
```

设置默认输入法
```cmd
adb shell settings put secure default_input_method com.sohu.inputmethod.sogou/.SougouIME

adb shell settings put secure default_input_method com.android.inputmethod.latin/.LatinIME
```


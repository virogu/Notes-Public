# 获取设备音频流信息
```cmd
adb shell dumpsys audio
```
# 获取当前设备各类音量大小
## 获取设备的电话音量 - STREAM_VOICE_CALL
```cmd
adb shell media volume --show --stream 0 --get
```
## 获取设备的系统音量 - STREAM_SYSTEM
```cmd
adb shell media volume --show --stream 1 --get
```
## 获取设备的铃音音量 - STREAM_RING
```cmd
adb shell media volume --show --stream 2 --get
```
## 获取设备的音乐音量（多媒体音量 ）- STREAM_MUSIC
```cmd
adb shell media volume --show --stream 3 --get
```
## 获取设备的闹钟音量- STREAM_ALARM
```cmd
adb shell media volume --show --stream 4 --get
```

# 设置音量大小
示例：设置多媒体音量大小
```cmd
adb shell media volume --show --stream 3 --set 10

"""
输出：
    [v] will control stream=3 (STREAM_MUSIC)
    [v] will set volume to index=10
    [v] Connecting to AudioService
"""
# 注：设置系统音量为0（stream=1）后，设备会进入静音模式，之后再修改其他模式音量会失败
```


```
service.adb.tcp.port=5555

adb tcpip 5555

adb shell su -c setprop service.adb.tcp.port 5555


// 方式一
adb root
adb remount
adb pull /system/build.prop
修改 build.prop 文件，添加 service.adb.tcp.port=5555，保存
adb push build.prop /system/build.prop
adb shell chmod 0644 /system/build.prop


//方式二
adb shell
// su
// mount -o remount rw /system
echo service.adb.tcp.port=5555 >> /system/build.prop
```
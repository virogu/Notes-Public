# Android目录文件简析
[TOC]

## 根目录
### data
data分区
#### app
用户安装的软件都在这里面,一个包名一个文件夹
##### oat/arm/base.art,base.odex,base.vdex
用户运行应用，而这随后就会触发 ART 加载 .dex 文件。
如果有 .oat 文件（即 .dex 文件的 AOT 二进制文件），则 ART 会直接使用该文件，软件速度会明显提升。删除这个文件夹一般不会影响软件功能，但是软件速度会明显变慢。
有Speed编译方式和Everything编译
#### dalvik-cache
里面有arm和arm64文件夹，存放软件编译后的文件，这些文件删除后会重新生成。
#### data
软件数据目录
#### media
##### 0
手机内部存储分区
#### system
##### locksettings.db
锁屏密码文件，删除此文件即可删除手机锁屏密码
### dev
手机各分区文件
### etc
#### host 
```
127.0.0.1 localhost
::         1ip6-localhost
```
可以修改此文件达到去广告或限制手机访问某些域名的目的
比如加上`127.0.0.1  baidu.com` ，手机就访问不了百度了
### misc
#### wifi
##### WifiConfigStore.xml
这个文件存放手机连接的WiFi信息，打开可以查看手机连接过的所有WiFi密码信息
### sbin
### sdcard
实质上是一个快捷方式，指向`data/media/0`
### storage
`storage/emulated/0` 也是 `data/media/0` 手机内部存储目录
### system
#### app
系统级非核心软件，删除里面的软件系统不一定会崩溃
#### data-app
不是必有的文件夹，有些系统会预装很多软件，即使卸载后，再恢复出厂设置就又会出现，这些预装软件就是放在这里面的，系统第一次初始化时会把这些软件安装到data分区
#### priv-app
安卓4.4增加的新分区，主要是存放系统的系统级核心应用，比如电话，短信，设置，SystemUI等，这些应用需要系统级权限，而又不能被用户卸载掉，删除里面的软件后系统会崩溃
### xbin
如果使用普通方式 root，里面会有`su`文件，可以通过此方式判断设备有没有被 root，但是如果是用的`magisk`进行root不会有这个文件，`magisk`可以隐藏 root（现在应该有别的方式去判断是否有root权限，Magisk应该也有别的方式去隐藏root）
### media
系统媒体文件夹
#### audio
#### alarm
系统预设闹铃铃声
#### notification
系统预设通知铃声
#### ringtones
系统预设电话铃声
#### ui
系统各种UI提示音
比如拍照快门声，充电音效，锁屏音效等
#### lockscreen，wallpager
系统预设壁纸
#### bootanimation.zip
开机动画，里面是包含一个`desc.txt`文件和若干个`part`文件夹
`part` 文件夹里面是动画的每一帧
`desc` 文件用来描述动画属性
### build.prop
系统配置属性文件
正确的修改可以自定义系统某些特性。
下面是里面部分内容所代表的意思
```
ro.boot.selinux=permissive
permissive模式

#音量阶数
ro.config.media_vol_steps = 30

#增加虚拟键
qemu.hw.mainkeys=0

#谷歌相机
persist.camera.HAL3.enabled=1

#增加虚拟键
qemu.hw.mainkeys=0

#渲染GPU的UI
debug.sf.hw=1

#禁用开机动画
debug.sf.nobootanimation=1
```
### vendor
安卓8新加的分区
有`vendor`分区不一定支持`PT（ProjectTreble）`，支持`PT`的一定有`vendor`分区
这里面存放底层芯片硬件驱动等

## Xposed
`Xposed` 是一款开源框架，可以在不修改APK的情况下影响程序运行(修改系统)的框架服务，基于它可以制作出许多功能强大的模块，且在功能不冲突的情况下同时运作。
`Xposed` 具有比较高的可定制化程度。用户可定制手机的外观、状态等几乎所有东西， 
`Xposed`的主要作用是hook应用方法，动态劫持方法的运行逻辑。
需要用 `Xposed管理器` 配合相关 `XP模块` 来使用。
目前`Xposed` 已经停止维护，在高版本安卓上面已经不能使用了，取而代之的有`Edxposed`、`LSPosed`等
`Xp模块`也是一个apk，编写时引用的`XposedBridgeApi.jar`，`Mainfest.xml`文件加入以下标签
```xml
<meta-data
        android:name="xposedmodule"
        android:value="true"/>
    <meta-data
        android:name="xposeddescription"
        android:value="这是一个xposed应用"/>
    <meta-data
        android:name="xposedminversion"
        android:value="54"/>
```
然后编写hook工具类，重写`handleLoadPakage`方法 
```java
public class XposedHookUtil implements IXposedHookLoadPackage {
String class_name = "com.hdc.xposeddemo.MainActivity";
// 对指定包名,指定方法进行hook，将其修改成自己的方法
@Override
public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
    Class clazz = loadPackageParam.classLoader.loadClass(class_name);
    XposedHelpers.findAndHookMethod(clazz, "getTTAd", new XC_MethodReplacement() {
        @Override
        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
            xxxx;
        }
    });
}
}
```
常用的Xposed模块：QX，微X，应用变量等

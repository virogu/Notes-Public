https://glumes.com/post/webrtc/webrtc-android-setup/


#### 安装工具

下载工具
```
// 安装一些基础的软件依赖
sudo apt-get update
sudo apt-get install -y openssl vim git gcc g++ curl python build-essential inetutils-ping net-tools sudo lsb-release libxml2
```

下载depot_tools

```
git clone https://chromium.googlesource.com/chromium/tools/depot_tools.git
```
配置环境
```
// 配置环境，打开.bashrc文件
gedit ~/.bashrc
// 在这文件添加如下内容
# depot_tools_path
export DEPOT_TOOLS_PATH=/root/work/webrtc/depot_tools
export PATH=${PATH}:${DEPOT_TOOLS_PATH}
// 使环境生效
source ~/.bashrc
// 查看是否设置成功
echo $PATH
fetch --help
```
建立源码路径，下载好源码（漫长的过程）后期更新只需要执行 gclient sync 即可
```
mkdir webrtc
cd webrtc
fetch --nohooks webrtc_android
gclient sync
```

#### 安装依赖

下载完 WebRTC 后需要安装相关的依赖，进入到 WebRTC 源码的 `src` 目录中，执行如下命令：

```cpp
cd src
./build/install-build-deps.sh --no-chromeos-fonts
./build/install-build-deps-android.sh --no-chromeos-fonts
```

执行shell脚本时报错：: 没有那个文件或目录
是因为该文件在windows系统上打开过，关闭后其中的空格符号和Linux的不同，导致这个报错，我们可以通过sed命令与正则的配合将文件中的空格符号替换成linux的空格：

```
sed -i 's/\r$//' xxx.sh
```

####  编译

设置编译参数，生成ninja文件

```csharp
# source ./build/android/envsetup.sh

gn gen out/build --args='target_os="android" target_cpu="arm64" is_debug=false treat_warnings_as_errors=false'
# out/build ： 编译生成文件的目录，随意指定
# target_os ： 编译目标平台 android ios 等
# target_cpu ： CPU架构平台 arm arm64 x86 x64等
# is_debug : Release模式或者Debug模式
```

清理目录

```
gn clean out/build
```

编译

```csharp
#全量编译
ninja -C out/build
    
ninja -C out/build AppRTCMobile

#编译组合
a)编译arm debug版本
    gn gen out/Debug_arm --args='target_os="android" target_cpu="arm" treat_warnings_as_errors=false'
    ninja -C out/Debug_arm
b)编译arm64 debug版本
    gn gen out/Debug_arm64 --args='target_os="android" target_cpu="arm64" treat_warnings_as_errors=false'
    ninja -C out/Debug_arm64
c)编译arm release版本
    gn gen out/Release_arm --args='target_os="android" target_cpu="arm" is_debug=false treat_warnings_as_errors=false'
    ninja -C out/Release_arm
d)编译arm64 release版本
    gn gen out/Release_arm64 --args='target_os="android" target_cpu="arm64" is_debug=false treat_warnings_as_errors=false'
    ninja -C out/Release_arm64
```

生成aar

```bash
cd webrtc/src
python tools_webrtc/android/build_aar.py --output "libwebrtc.aar" --arch "armeabi-v7a" "arm64-v8a" --build-dir out/Release  --extra-gn-args 'is_debug=false is_component_build=false is_clang=true rtc_include_tests=false rtc_use_h264=true rtc_enable_protobuf=false use_rtti=true use_custom_libcxx=false treat_warnings_as_errors=false' 

//只编译 arm64
python tools_webrtc/android/build_aar.py --output "libwebrtc.aar" --arch "arm64-v8a" --build-dir out/Release  --extra-gn-args 'is_debug=false is_component_build=false is_clang=true rtc_include_tests=false rtc_use_h264=true rtc_enable_protobuf=false use_rtti=true use_custom_libcxx=false treat_warnings_as_errors=false'


# 成功后你会在src目录下看到libwebrtc.aar文件，里面就是Android开发需要用到的SDK了。
# out/Release目录是编译目录，第一编译会全量编译速度很慢（预计30~40分钟），以后就增量编译很快（预计10s内）。
```

生成so文件，生成so文件首次会全量编译，后续增量编译速度非常快。如果以后只改C层代码不生成Java或者Object-C的API，这种方法非常适合测试。

```csharp
ninja -C out/Release sdk/android:libjingle_peerconnection_so
```

#### 编译mediasoupclient

```
$ cd /root/work/libmediasoupclient

$ cmake . -Bbuild \
  -DLIBWEBRTC_INCLUDE_PATH:PATH=/root/work/webrtc/webrtc_android/src \
  -DLIBWEBRTC_BINARY_PATH:PATH=/root/work/webrtc/webrtc_android/src/out/build/obj

$ make -C build/
```













安装包时可能会因为依赖其他包而安装失败，可以使用aptitude安装

```
// 安装aptitude
sudo apt-get install aptitude
// 安装所需要的包
sudo aptitude install xxxxx
```

或者 [https://launchpad.net/ubuntu](https://launchpad.net/ubuntu) 搜索需要的deb包，然后到本地下载，然后dpkg -i xxx.deb
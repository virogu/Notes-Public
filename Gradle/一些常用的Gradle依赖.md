```Groovy
//kotlin安卓扩展
apply plugin: 'kotlin-android-extensions'

//Retrifot(网络请求)
implementation 'com.squareup.retrofit2:retrofit:2.0.2'
implementation 'com.squareup.retrofit2:converter-gson:2.0.2'
implementation 'com.squareup.okhttp3:okhttp:3.12.1'

// CameraX
def camerax_version = "1.0.0-alpha05"
// CameraX view
def camerax_view_version = "1.0.0-alpha02"
// CameraX 扩展 library
def camerax_ext_version = "1.0.0-alpha02"
implementation "androidx.camera:camera-core:$camerax_version"
//如果你要使用Camera2的扩展功能
implementation "androidx.camera:camera-camera2:$camerax_version"
// 如果你要使用 CameraX View
implementation "androidx.camera:camera-view:$camerax_view_version"
// 如果你要使用 CameraX 的 扩展功能
implementation "androidx.camera:camera-extensions:$camerax_ext_version"
//申请权限
implementation 'com.tbruyelle.rxpermissions2:rxpermissions:0.9.5'

//add at the end of android{} block
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
}

//Collection KTX
implementation "androidx.collection:collection-ktx:1.1.0"

//Permissionx
implementation 'com.permissionx.guolindev:permissionx:1.3.0'
implementation 'com.permissionx.guolindev:permission-support:1.3.0'

//协程
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1"

```

```Groovy
defaultConfig {
   ......
   // 先在默认配置中定义两个变量main_activity_key和oem_main_activity_key，并赋初始值
   manifestPlaceholders += [main_activity_key: "android.intent.action.MAIN",
                            oem_main_activity_key: "android.intent.action.NO_MAIN"]

  //不能单独写两个manifestPlaceholders，必须是一个数组，不然编译通不过

   ......
}
```

```Groovy
productFlavors {
    OemA {
        buildConfigField "int", "oem_type", "0"
    }
    OemB {
        buildConfigField "int", "oem_type", "0"
    }
}
```

```Groovy
// 遍历OEM版本，在目标客户中根据需要替换main_activity_key和oem_main_activity_key的值
productFlavors.all {flavor ->
    if (flavor.name.contains("OemA")) {
        flavor.manifestPlaceholders = [main_activity_key:
        "androidintent.action.NO_MAIN",
        oem_main_activity_key: "android.intent.action.MAIN"]
    } else if (flavor.name.contains("OemB")) {
        flavor.manifestPlaceholders = [main_activity_key: "android.intent.action.MAIN",
   oem_main_activity_key: "android.intent.action.NO_MAIN"]
    }
}
```

然后在Manifest中修改两个Activity的action属性

```xml
<activity
    android:name=".ui.activity.OemAActivity"
    android:excludeFromRecents="true"
    android:launchMode="singleInstance"
    android:screenOrientation="landscape"
    android:theme="@android:style/Theme.NoTitleBar.Fullscreen">
    <intent-filter>
        <action android:name="${oem_main_activity_key}"/> // 修改属性
        <category android:name="android.intent.category.LAUNCHER"/>
    </intent-filter>
</activity>

<activity
    android:name=".ui.activity.OemBActivity"
    android:excludeFromRecents="true"
    android:launchMode="singleInstance"
    android:screenOrientation="landscape"
    android:theme="@android:style/Theme.NoTitleBar.Fullscreen">
    <intent-filter>
        <action android:name="${main_activity_key}"/> // 修改属性
        <category android:name="android.intent.category.LAUNCHER"/>
    </intent-filter>
</activity>
```

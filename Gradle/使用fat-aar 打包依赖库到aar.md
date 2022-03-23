https://github.com/kezong/fat-aar-android

项目根目录 `build.gradle` 添加

```Groovy
buildscript {
    repositories {
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath 'com.kezong:fat-aar:1.3.1'
    }
}
```

`module` 的 `build.gradle` 添加：

```Groovy
apply plugin: 'com.kezong.fat-aar'
```

使用：
需要打包进aar的库使用 `embed` ，而不是 `implementation`

```Groovy
embed(name: 'abcdef', ext: 'aar')
embed('com.abc.abc:abc:1.2.3')
```

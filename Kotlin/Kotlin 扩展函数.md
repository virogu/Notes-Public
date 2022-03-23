[TOC]

###  **内置扩展函数**
#### IO操作扩展函数

#####  复制文件

复制文件A到文件B
```kotlin
val originFile = File("$TEST_PATH\\copytest1.txt")
val dstFile = File("$TEST_PATH\\copytest2.txt")
```
常规写法

```kotlin
val inputStream = FileInputStream(originFile)
val os = FileOutputStream(dstFile)
try {
    val buffer = ByteArray(1024)
    var len = inputStream.read(buffer)
    while (len != -1) {
        os.write(buffer, 0, len)
        len = inputStream.read(buffer)
    }
} finally {
    os.flush()
    os.close()
    inputStream.close()
}
```

`use` 结合扩展函数`InputStream.copyTo()`写法

```kotlin
val inputStream = FileInputStream(originFile)
val os = FileOutputStream(dstFile)
inputStream.use {
    os.use { output ->
        it.copyTo(output)
    }
}
```

使用 `File.copyTo()`扩展函数直接复制
```kotlin
try {
    originFile.copyTo(dstFile)
} catch (e: Throwable) {
    e.printStackTrace()
}
```

##### 遍历文件
遍历文件夹下所有文件包括子目录，子文件等

`File.walkBottomUp()` 
获取从下到上的顺序访问此目录及其所有内容的顺序。使用深度优先搜索，并在访问所有文件之前访问目录

`File.walkTopDown()`
获取从上到下的顺序访问此目录及其所有内容的顺序。使用深度优先搜索，并在访问所有文件之前访问目录

```
val path = File("E:\\Note\\MarkDown")
val files = path.walkBottomUp()
println("walkBottomUp")
files.forEach {
    println(it.path)
}
```

##### 删除文件

`File.deleteRecursively()`

删除文件夹下所有文件，包括子目录、子文件

```
val path = File("$TEST_PATH\\testDelete - 副本\\")
val result = path.deleteRecursively()
```
函数返回 true 表示全部删除完成，返回 false 表示至少有一个文件没有删除成功



###  **自定义扩展函数**
自定义一个List交换两个元素位置的函数
```
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 > this.size - 1 || index2 > this.size - 1) {
        throw IllegalArgumentException("index is more than list's size")
    }
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

@org.junit.jupiter.api.Test
fun listSwap() {
    val list = mutableListOf(1, 2, 3, 4, 5)
    println("origin: $list")
    list.swap(2, 3)
    println("after swap: ${list}")
}
```
测试执行结果：
```
origin: [1, 2, 3, 4, 5]
after swap: [1, 2, 4, 3, 5]
```

扩展函数在不修改某个类源码的情况下,动态地添加新的函数.className.
扩展函数不能访问原有类的私有属性

###  **运算符重载**

同一运算符在不同的环境所表现的效果不同，如 ”+“ 在两个 Int 值之间表示两者的数值相加，在两个字符串之间表示，将字符串拼接，同时kotlin允许我们将任意两个类型的对象进行”+“运算，或者其他运算符操作。

语法结构：如下，其中`operator` 为运算符重载的关键字
```
operator fun plus(a: A): A {
    //相关逻辑
}
```

常见的语法糖表达式和实际调用函数对照表：

|表达式|函数名|
|  ---- | ----  |
|a * b	|a.times(b)|
|a / b	|a.div(b)|
|a % b	|a.rem(b)|
|a + b	|a.plus(b)|
|a - b	|a.minus(b)|
|a++	|a.inc()|
|a--	|a.dec()|
|!a		|a.not()|
|a == b	|a.equals(b)|
|”a > b“、”a < b“、”a >= b“、”a >= b“	|a.compareTo(b)|
|a..b	|a.rangeTo(b)|
|a[b]	|a.get(b)|
|a[b] = c|a.set(b, c)|
|a in b	|b.contains(a)|

#### 扩展函数和运算符重载结合

例：重写 String 的  * （即 times) 操作
```
operator fun String.times(int: Int): String {
    return StringBuilder().also { s ->
        repeat(int) {
            s.append(this)
        }
    }.toString()
}
```
使用
```
val s = "hello " * 5
println(s)
```
结果
```
hello hello hello hello hello 
```


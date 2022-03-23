[TOC]

## **Kotlin标准函数**

### **let函数**

let函数会将调用它的对象作为参数传递到Lambda表达式中。

例如有如下方法

```kotlin
fun doStudy(study: Study){ //Study接口中只有readBooks()和doHomeWork()两个方法
    if (study != null){
        study.readBooks()
    }
    if (study != null){
        study.doHomeWork()
    }
}

```

这个时候结合?.操作符合let函数就可以对代码进行优化了，如下所示

```kotlin
 fun doStudy(study: Study){
    //?.操作符表示对象不为空时正常调用相应的方法，对象为空时就什么都不做
    study?.let {stu ->
        stu.readBooks() //因为let函数可以正常调用，所以对象一定不为空
        stu.doHomeWork()
    }
}          
```

然后利用Lambda语言的特性：当Lambda表达式的参数列表中只有一个参数时，可以不用声明参数名，直接用it关键字代替即可。那么代码就可以进一步简化

```kotlin
fun doStudy(study: Study){
    study?.let {
        it.readBooks()
        it.doHomeWork()
    }
}

```

注意： let函数是可以处理全局变量判空的问题的，而if判断语句则无法做到这一点，比如我们将doStudy()函数中的参数变成一个全局变量，使用let函数仍然可以正常工作，但使用if判空语句就会提示错误，代码如下所示：

```kotlin
var study: Study? = null
fun doStudy() {
    if (study != null) {
        study.doHomeWork() //这里会报错，下面一行也是
        study.readBooks()
        //
    }
}
```

调用study的方法会报错是因为全局变量的值随时都可能被其他线程所修改，即使做了判空处理仍然无法保证if语句中的study对象没有空指针的风险。



### **with函数**

with函数 接收两个参数：第一个参数可以是一个任意类型的对象，第二个参数是一个Lambda表达式。with函数会在Lambda表达式中提供第一个参数对象的上下文，并使用Lambda表达式的最后一行作为返回值返回。它可以在连续调用同一个对象的多个方法时让代码变得更加精简。

举个例子： 比如有一个水果列表，现在我们想吃完所有水果，并将结果答应出来，就可以这样写

```kotlin
val list = listOf("Apple", "Banana", "Orange", "Pear")
val builder = StringBuilder()
builder.append("Start eating fruits.\n")
for (fruit in list){ 
    builder.append(fruit).append("\n")
}
builder.append("Ate all fruits")
val result = builder.toString()
println(result)

```

上面代码，连续调用了多次builder对象的方法，这时候就可以考虑使用with函数了来让代码变得更加精简了。如下所示

```kotlin
val list = listOf("Apple", "Banana", "Orange", "Pear")
val builder = StringBuilder()
val result = with(StringBuilder()){
    append("Start eating fruits.\n")
    for (fruit in list){
        append(fruit).append("\n")
    }
    append("Ate all fruits.")
    toString()
}
println(result)
```

这段代码其实也不难理解，首先我们给with函数第一个参数传入一个StringBuilder对象，那么接下来第二个参数也就是Lambda表达式的上下文就会是这个StringBuilder对象，于是我们在Lambda表达式中就不用使用builder.append()的方式，而是直接使用append()的方式来调用，Lambda的最后一行会作为with函数的返回值返回，最终将结果打印出来，两段代码运行结果是一样的



### **run函数**

run函数的用法和使用场景其实和with函数斯非常类似的，只是稍微做了些语法改动而已。首先run函数是不能直接调用的，而是一定要调用某个对象的run函数才行；其次run函数只接收一个Lambda参数，并且会在Lambda表达式中提供调用对象的上下文。其他方面和with函数是一样的，也会使用Lambda表达式中的最后一行代码作为返回值返回。

现在可以用run函数来改一下吃水果的这段代码，如下所示：

```kotlin
val list = listOf("Apple", "Banana", "Orange", "Pear")
val builder = StringBuilder()
val result = builder.run{
    append("Start eating fruits.\n")
    for (fruit in list){
        append(fruit).append("\n")
    }
    append("Ate all fruits.")
    toString()
}
println(result)

```

总体来说变化非常小，只是将调用with函数并传入StringBuilder对象改成了调用StringBuilder对象的run方法，其他没有任何区别。两段代码运行结果都一样。



### **apply函数**

apply函数和run函数也是极其类似的。都要在某个对象上调用，并且只接收一个Lambda参数，也会在Lambda表达式中提供调用对象的上下文，但是apply函数无法指定返回值，而是会返回调用对象本身。

那么现在用apply函数来改写吃水果这段代码，如下所示：

```kotlin
val list = listOf("Apple", "Banana", "Orange", "Pear")
val builder = StringBuilder()
val result = builder.apply{
    append("Start eating fruits.\n")
    for (fruit in list){
        append(fruit).append("\n")
    }
    append("Ate all fruits.")
}
println(result.toString())
              
```

注意这里的代码变化，因为apply函数无法指定返回值，只能返回调用对象本身，因此这里的result实际上是一个StringBuilder对象。所以我们在最后打印的时候还要调用它的toString()方法才行。这段代码运行结果和前面两段仍然完全相同。

### **also函数**

also函数和apply函数用法基本一致的。要在某个对象上调用，并且返回调用对象本身。只不过also函数在lambda中要用一个名称（默认为 it），在 apply中，不必添加其他前缀来访问其成员，或者使用 this引用 .

```kotlin
am.also {
	//使用 it 来调用对象
	it.xxx()
}
am.apply {
	
//使用 this 来调用对象 或者 直接调用对象
	this.xxx()
	xxx()
}
```

小总结：
```
let、run		返回函数体最后一行
apply、also	返回调用对象本身
let、also	函数体调用时默认用 it
run、apply	函数体调用时默认用 this
with		调用时需要传一个对象，返回函数体最后一行
```

### **use函数**

使用`use`函数的对象要实现`Closeable`方法，并且无论接下来的操作是否引发异常都会自动正确关闭它，比如`InputStream`就可以使用`use`函数

常规写法

```kotlin
var input:InputStream? = null
try{
	input= FileInputStream(File("aaaaa"))
	//对 input 进行操作
	// ......
}finall{
	//最后需要调用close（）
	input?.close()
}
```

使用use函数写法

```kotlin
val input = FileInputStream(File("aaaaa"))
input?.use{
	//对 input 进行操作
	//不需要手动close
}
```
`use` 函数可以极大地简化文件操作的代码

### **repeat**
重复执行指定次数

```
repeat(5){ 
	println("count:$it") 
}
```
等价于
```
for (i in 0..4) { 
	println("count:$i") 
}
```
或者
```
for (i in 0 until 5) { 
	println("count:$i") 
}
```
或者
```
(0..4).forEach { 
 println("count:$i") 
}
```
### **takeIf  和 takeUnless **

`takeIf` 传递一个函数参数，如果函数结果为true，返回T对象，否则返回null
例如，判断文件是否存在，不存在则返回

```
 var file = File("filePath")
 if (file.exists()) {
  //do something
 } else {
  return
 }
```
使用`takeIf`:
```
var file = File("filePath").takeIf { it.exists() }?:return false
//do something
```
`takeUnless` 传递一个函数参数，如果函数结果为false，返回T对象，否则返回null






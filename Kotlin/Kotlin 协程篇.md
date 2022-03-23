[TOC]

### CoroutineScope 作用域

指定协程执行的范围

`CoroutineScope` 应该在生命周期明确的实体上实现（或用作字段），这些实体负责启动子协程。例如在Activity中使用协程：

```Kotlin
 class MyActivity : AppCompatActivity(), CoroutineScope by MainScope() {
 	override fun onDestroy() {
        cancel() // destroy的时候cancel()
    }
 
 	fun showSomeData() = launch { // <- extension on current activity, 	
 		// 主线程
 		// 也可以切换到其他线程
		draw(data) // draw in the main thread
	}
 }
```
Lifecycle的扩展中已经定义了一个协程作用域，这个作用范围是默认在主线程执行的，生命周期结束会执行`cancel()`，所以我们可以直接使用这个作用域就行了:
```kotlin
val LifecycleOwner.lifecycleScope: LifecycleCoroutineScope
    get() = lifecycle.coroutineScope
```
在Activity中使用协程时就可以用 `lifecycleScope` 了
```kotlin
lifecycleScope.launch {
	// do something in main thread
	launch(Dispatchers.IO) { 
    	// do something in io thread
    }
}
```
在 `Fragment `中也是使用 `lifecycleScope` 作用域，在 `ViewModel `中用 `viewModelScope `

#### GlobalScope

全局的 `CoroutineScope`，没有绑定到任何Job。
`GlobalScope` 用于启动在整个应用程序生命周期内运行且不会过早取消的顶级协程.
应用程序代码通常应使用应用程序定义的`[CoroutineScope]`。不建议在`[GlobalScope]`实例上使用`[async][CoroutineScope.async]`或`[launch] [CoroutineScope.launch]`。



### suspendCoroutine 

挂起函数

```kotlin
suspend inline fun <T> suspendCoroutine(crossinline block: (Continuation<T>) -> Unit): T
```
获取暂停函数中的当前继续实例，并暂停当前正在运行的协程。也就是阻塞的等待函数返回结果，这个函数类似`RxJava` 中 `Single<T> `的`blockGet()`操作。

例：
```kotlin
lifecycleScope.launch(Dispatchers.IO) {
    Timber.i("start")
    val result = suspendCoroutineGetTest()
    Timber.i("result: $result")
    Timber.i("end:")
}

private suspend fun suspendCoroutineGetTest(): String? = suspendCoroutine {
    runBlocking(Dispatchers.IO) {
        delay(3000L)
    }
    it.resume("hhhhh")
}
```
运行结果：
```
15:53:43.858 I/MainActivity$onCreate: start
15:53:46.865 I/MainActivity$onCreate: result: hhhhh
15:53:46.865 I/MainActivity$onCreate: end:
```
使用`resumeWithException`可以让它抛出异常，调用该方法的地方就可以捕获到该异常，例：
```kotlin
lifecycleScope.launch(Dispatchers.IO) {
    Timber.i("start")
    try {
        val result = suspendCoroutineGetTest()
        Timber.i("result: $result")
    } catch (e: Throwable) {
        Timber.e(e)
    }
    Timber.i("end:")
}

private suspend fun suspendCoroutineGetTest(): String? = suspendCoroutine {
    runBlocking(Dispatchers.IO) {
        delay(3000L)
    }
    it.resumeWithException(IllegalStateException("test"))
}
```
运行结果
```
2021-02-03 16:01:50.964 I/MainActivity$onCreate: start
2021-02-03 16:01:53.971 E/MainActivity$onCreate: java.lang.IllegalStateException: test
        ......
2021-02-03 16:01:53.971 I/MainActivity$onCreate: end:
```
使用这个函数我们就可以将异步的一些操作改能同步的，或者将一些回调特别多的操作改成同步的。
比如进行相机拍照操作就可以这样处理：

```kotlin
// 点击拍照按钮
bt_snap.setOnClickListener {
	bt_snap.isEnabled = false
    lifecycleScope.launch(Dispatchers.IO) {
        val byteArray = doTakePicture(mCamera)
        // ....
		launch(Dispatchers.Main) {
			bt_snap.isEnabled = true
		}  
    }
}

private suspend fun doTakePicture(camera: Camera?): ByteArray? = suspendCoroutine {
    if (camera == null) {
        it.resume(null)
        return@suspendCoroutine
    }
    camera.autoFocus { _, c ->
        if (c == null) {
            it.resume(null)
            return@autoFocus
        }
        c.takePicture (null, null, { data, c1 ->
            if (c1 == null) {
                it.resume(null)
                return@takePicture
            }
            try {
                c1.startPreview()
            } finally {
                it.resume(data)
                return@takePicture
            }
        })
    }
}

```

在此函数中，`resume`和`resumeWithException`可以在运行暂停功能的同一堆栈中同步使用，也可以稍后在同一线程或不同执行线程中异步使用。随后调用任何恢复函数将产生一个`java.lang.IllegalStateException: Already resumed`的异常。另外，如果此函数中使用了`resumeWithException`，在调用它的地方也要进行异常捕获处理。

### suspendCancellableCoroutine

可取消的挂起函数

上面讲的`suspendCoroutine `挂起函数执行开始之后是不可取消的，比如：
```kotlin
lifecycleScope.launch(Dispatchers.Main) {
    val job = launch(Dispatchers.IO) {
    	Timber.i("start")
   		//这里三秒后才会返回结果
        val result = suspendCoroutineGetTest()
        Timber.i("result: $result")
        Timber.i("end:")
	}
	// 1秒后取消
	launch(Dispatchers.IO) {
    	delay(1000L)
    	Timber.i("cancel job")
    	job.cancel()
	}
}

private suspend fun suspendCoroutineGetTest(): String? = suspendCoroutine {
    runBlocking(Dispatchers.IO) {
        delay(3000L)
    }
    Timber.i("suspendCoroutine return ")
    it.resume("hhhhh")
}
```
预期结果是1秒后 job 被取消，suspendCoroutineGetTest 将不再返回结果，而实际结果是：
```
00.789 I/MainActivity$onCreate$1$job: start
01.796 I/MainActivity$onCreate: cancel job
03.804 I/MainActivity: suspendCoroutine return 
03.804 I/MainActivity$onCreate$1$job: result: hhhhh
03.804 I/MainActivity$onCreate$1$job: end:
```
job 调用了cancel方法之后，挂起函数还在执行，并且整个job还在正常运行，这显然不符合我们预期要求。
如果要让job能够取消就需要使用`suspendCancellableCoroutine`

```kotlin
private suspend fun suspendCoroutineGetTest(): String? = suspendCancellableCoroutine {
    runBlocking(Dispatchers.IO) {
        delay(3000L)
    }
    Timber.i("suspendCoroutine return ")
    it.resume("hhhhh")
}
```
这时候再运行：
```
52.797 I/MainActivity$onCreate$1$job: start
53.806 I/MainActivity$onCreate: cancel job
55.806 I/MainActivity: suspendCoroutine return 
55.810 E/MainActivity$onCreate$1$job: kotlinx.coroutines.JobCancellationException: Job was cancelled;
```
三秒后挂起函数返回结果时便抛出了异常，job也不会继续执行。

上面讲到挂起函数调用`resume`之后再次`resume`就会抛出异常，但是如果是使用 `suspendCancellableCoroutine` 就可以通过 `isActive` 来判断是否需要再 `resume` 了:

```kotlin
Timber.i("suspendCoroutine return ")
it.resume("hhhhh")
if(it.isActive) {
    it.resume("hhhhh")
}
if(it.isActive) {
	//这里再次调用resume也不会再执行了，因为上面已经resume了，isActive也已经变成了false
    it.resume("hhhhh")
}
```
所以我们在使用挂起函数时最好使用 `suspendCancellableCoroutine `而不是 `suspendCoroutine `。
另外，这个挂起函数中有三个标志：
`isActive`		函数如果没有完成或取消则返回 true
`isCompleted`	由于任何原因完成此继续操作时，都返回 true 。被取消的继续也被视为完成。
`isCancelled`	如果调用了job的`cancel()`方法，之后则返回 true

一般情况下使用`isActive`就可以满足需求了。



### Flow

#### 什么是Flow

Flow 库是在 Kotlin Coroutines 1.3.2 发布之后新增的库，也叫做异步流

类似 RxJava 的 Observable 、 Flowable 等。用生产者和消费者来做对比，简单来讲就可以一边生产一边消费的作用。

#### 使用Flow

先看一个简单的使用例子

```kotlin
lifecycleScope.launch {
    launch {
    	//验证Flow没有阻塞线程
        repeat(3){
            delay(1000)
            Timber.i("I'm not blocked: $it")
        }
    }
    flow{
    	repeat(3){
        	delay(1000)
        	emit(it)
    	}
	}.collect {
        Timber.i("collect: $it")
    }
}
```
运行结果
```
00.480 I: collect : 0
00.481 I: I'm not blocked : 0
01.484 I: collect : 1
01.484 I: I'm not blocked : 1
02.486 I: collect : 2
02.487 I: I'm not blocked : 2
```

其中Flow的接口，只有一个`collect`函数。和RXjava相比的话，可以理解为 `collect()` 对应`subscribe()`，而 `emit()` 对应`onNext()`。

#### 切换线程
默认情况没有切换线程的话flow的生产者和消费者是同步非阻塞的：
```kotlin
lifecycleScope.launch {
    flow{
        repeat(3){
            delay(1000)
            Timber.i("emit on: ${Thread.currentThread().name}")
            emit(it)
        }
    }.collect {
        Timber.i("collect on ${Thread.currentThread().name} : $it")
    }
}
```
输出结果：
```
I: emit on: main
I: collect on main : 0
I: emit on: main
I: collect on main : 1
I: emit on: main
I: collect on main : 2
```
切换线程，相比Rxjava中使用 observeOn、subscribeOn 来切换线程，flow 会更加简单。只需使用 flowOn，如下：
```kotlin
lifecycleScope.launch {
    flow{
        repeat(3){
            delay(1000)
            Timber.i("emit on: ${Thread.currentThread().name}")
            emit(it)
        }
    }.flowOn(Dispatchers.IO).collect {
        Timber.i("collect on ${Thread.currentThread().name} : $it")
    }
}
```
输出结果：
```
I: emit on: DefaultDispatcher-worker-1
I: collect on main : 0
I: emit on: DefaultDispatcher-worker-2
I: collect on main : 1
I: emit on: DefaultDispatcher-worker-1
I: collect on main : 2
```

flow builder是运行在 `flowOn()` 指定的线程中， `clollect() ` 所在线程则取决于整个 flow 处于哪个

CoroutineScope 下。



#### Flow 什么时候开始执行

Flow 是一种类似于序列的冷流 ， flow 构建器中的代码直到流被收集的时候才运行。

例：先初始化一个flow，三秒后再collect

```kotlin
Timber.i("start")
val f = flow{
    repeat(3){
        delay(1000)
        Timber.i("emit on: ${Thread.currentThread().name}")
        emit(it)
    }
}.flowOn(Dispatchers.IO)
delay(3000)
f.collect {
    Timber.i("collect on ${Thread.currentThread().name} : $it")
}
```
运行结果
```
46.180 I: start
50.211 I: emit on: DefaultDispatcher-worker-1
50.212 I: collect on main : 0
	......
```

#### Flow 怎么取消

Flow是需要写在 `lanuch` 中的，所以取消的话只需要调用`launch`的`cancel()`把`launch`取消就行了
```kotlin
val d = launch {
    flow{
        repeat(3){
            delay(1000)
            Timber.i("emit on: ${Thread.currentThread().name}")
            emit(it)
        }
    }.flowOn(Dispatchers.IO).collect {
        Timber.i("collect on ${Thread.currentThread().name} : $it")
    }
}
delay(1000)
d.cancel()
```
这样一秒后这个flow操作就会被取消


#### Flow 可以用在哪里
用处一：后台进行下载操作，主线程更新下载进度：
```kotlin
doDownload().flowOn(Dispatchers.IO).collect {
    Timber.i("下载进度：$it")
}

private fun doDownload() = flow {
    repeat(100){
        delay(10)
        emit(it)
    }
}
```
运行结果：
```
I: 下载进度：0
I: 下载进度：1
I: 下载进度：2
I: 下载进度：3
	......
I: 下载进度：98
I: 下载进度：99
```

用法二：后台耗时获取数据，后台一边获取，主线程一边处理。比如获取大量设备列表，后台可以分批进行获取，获取一批便emit一批。
```kotlin
lifecycleScope.launch {
    Timber.i("start")
    var count = 0
    getDevices().flowOn(Dispatchers.IO).buffer().collect {
        count++
        Timber.i("获取到 ${it.size} 个设备，开始更新列表")
    }
    Timber.i("end, count: $count")
}

private fun getDevices() = flow {
    val list: MutableList<Int> = ArrayList()
    repeat(1000) { i ->
        list.add(i)
    }
    repeat(100) {
        delay(100)
        //Timber.i("emit: $it")
        emit(list)
    }
}
```
上面是模拟一次获取1000个设备，获取100次

```
I: start
I: 获取到 1000 个设备，开始更新列表
I: 获取到 1000 个设备，开始更新列表
......
I: end, count: 100
```

#### Flow 其他操作符

例子：

```kotlin
foo().collect {
    println(it)
}

repeat(10) {
    delay(100)
    emit(it)
}
```
结果：
```
0
1
...
9
```

##### map 

使用map我们可以将最终结果映射为其他类型，融合了Rxjava的map与flatMap的功能
代码如下所示：
```kotlin
foo().map { 
    "转换成了String : $it"
}.collect {
    println(it)
}
```
结果：
```
转换成了String : 0
转换成了String : 1
...
转换成了String : 9
```
##### filter

通过 filter 可以对结果添加过滤条件，如下所示，仅打印出大于7的值
```kotlin
foo().filter { 
    it > 7
}.collect {
    println(it)
}
```
结果：
```
8
9
```

##### transform

transform可以自定义转换逻辑，除了可以实现filter和map的功能，还可以实现其他功能
```kotlin
foo().transform {
    if(it > 7){
        emit("transform to string :$it")
        emit("emit second :$it")
    }
}.collect {
    println(it)
}
```
运行结果：
```
transform to string :8
emit second :8
transform to string :9
emit second :9
```

#####  末端操作符
上面常用的`collect`就是一个末端操作符，除此之外还有`toList`、`reduce`、`fold`、`asLiveData`等操作符
`toList` 就如字面意思，把所有结果转成List数组
`reduce` 可以把所有结果累加起来：

```kotlin
val d = foo().reduce { accumulator, value ->
    accumulator + value
}
println(d)
```
结果:
```
45
```
`asLiveData` 将Flow转成LiveData，通过LiveData来监听 emit 的数据：

```kotlin
val liveData = getDevices().flowOn(Dispatchers.IO).buffer().asLiveData()
liveData.observe(this@MainActivity, {
    count++
    Timber.i("count: $count")
})
```
运行结果：
```
I: start
I: count: 1
I: count: 2
I: count: 3
... ...
I: count: 100
```

##### 协程背压

Kotlin流程设计中的所有函数都标有suspend修饰符，具有在不阻塞线程的情况下挂起调用程序执行的强大功能。因此，当流的收集器不堪重负时，它可以简单地挂起发射器，并在准备好接受更多元素时稍后将其恢复。

######  buffer 操作符

例如，发射者需要100毫秒处理并产生一个数据，而收集者需要200毫秒来处理这个数据，这时候发射者发就需要等收集者处理完上个数据再去花费100毫秒处理产生一个新数据，这样一个周期就是300毫秒，而使用` buffer`操作符就可以并发运行流中发射元素的代码以及收集的代码， 当发射者发射数据时，收集者还没处理完，会将这个数据先缓存下来，收集者处理完上一个数据之后立马就可以处理下一个数据。这样除了第一个周期是300毫秒，接下来的每个周期都是200毫秒，会节省很多时间。
默认可以无限制添加数据，但是超出默认缓存区域大小时，会 `suspend` 暂停。
`buffer()`  函数体可以传两个参数来自行定义缓存机制

```
buffer(capacity: Int = BUFFERED, onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND)
```
前面的获取设备列表的演示就加上了buffer。

```kotlin
println("start")
var count = 0
getDevices().flowOn(Dispatchers.IO).conflate().collect {
    count++
    delay(200)
    println("获取到 ${it.size} 个设备，开始更新列表")
}
println("end, count: $count")
```

######  conflate 操作符

用了 `conflate` 操作符，当发射者发射数据时，收集者还在处理上一个数据时则会跳过这个值，上面例子中`buffer()` 改成 `conflate()` 接口如下：

```kotlin
println("start")
var count = 0
getDevices().flowOn(Dispatchers.IO).conflate().collect {
    count++
    delay(200)
    println("获取到 ${it.size} 个设备，开始更新列表")
}
println("end, count: $count")
```
输出结果：
```
... ...
获取到 1000 个设备，开始更新列表
end, count: 11
```
只处理了11个

###### collectLatest()操作符

只处理最新的数据，这看上去似乎与 conflate 没有区别，其实区别大了：它并不会直接用新数据覆盖老数据，而是每一个都会被处理，只不过如果前一个还没被处理完后一个就来了的话，处理前一个数据的逻辑就会被取消。

```kotlin
var count = 0
getDevices().flowOn(Dispatchers.IO).collectLatest {
    delay(500)
    count++
    println("获取到 ${it.size} 个设备，开始更新列表")
}
println("end, count: $count")
```
处理的时候延时500毫秒，而发送间隔只有100毫秒，所以每次发送时都会把前面的处理取消掉，因此只有最后一个结果能被处理
```
start
获取到 1000 个设备，开始更新列表
end, count: 1
```

##### 组合多个流

###### Zip

`zip` 操作符可以组合两个流中的相关值：
```kotlin
val nums = (1..6).onEach {
    delay(10)
}.asFlow() // 数字 1..3
val strs = flowOf("one", "two", "three").onEach {
    delay(50)
} // 字符串
nums.zip(strs) { a, b ->
    "$a -> $b" 
}.collect { 
    println(it) 
}
```
输出结果
```
1 -> one
2 -> two
3 -> three
```

###### Combine
当流表示一个变量或操作的最新值时，可能需要执行计算，这依赖于相应流的最新值，并且每当上游流产生值的时候都需要重新计算。
也就是说当两个流中任何一个流产生了新的流的时候，并且这两个流都已经有值发射了，就将这两个流当前最新的值组合在一起。
```kotlin
val nums = (1..6).asFlow().onEach {
    delay(10)
} 
val strs = flowOf("one", "two", "three").onEach {
    delay(50)
}
nums.combine(strs) { a, b ->
    "$a -> $b"
}.collect {
    println(it)
}
```
输出结果
```
4 -> one
5 -> one
6 -> one
6 -> two
6 -> three
```



## Paging局部修改数据
```kotlin
//转成LiveData
private val _pagerData: MutableLiveData<PagingData<Item>> =
        repository.pagingData.cachedIn(viewModelScope).asLiveData().let {
            it as MutableLiveData<PagingData<Item>>
        }

val pagerData: LiveData<PagingData<Item>> get() = _pagerData

fun mod(
    old: Item,
    new: Item,
) {
    _pagerData.value?.map {
        if (it.id == old.id) {
            new
        } else {
            it
        }
    }.also {
        _pagerData.value = it
    }
}
```
[这里还有一个不修改数据源实现的示例](https://sourcediving.com/crud-operations-with-the-new-android-paging-v3-5bf55110aa4d)

# getRefreshKey示例
```kotlin
override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
    return state.anchorPosition?.let {
        state.closestPageToPosition(it)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
    }
}
```


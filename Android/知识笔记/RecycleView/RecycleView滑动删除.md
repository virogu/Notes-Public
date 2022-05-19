## RecycleView实现滑动删除或拖动排序
```Kotlin
ItemTouchHelper(object : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return (viewHolder as? MyAdapter.MyHolder)?.data?.let {
            //dragFlags 拖拽的方式
            //swipeFlags 滑动触发的方式
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        } ?: makeMovementFlags(0, 0)
    }
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        (viewHolder as? MyAdapter.MyHolder)?.data?.let {
            //删除某一项的操作
            //model.onViewEvent(ItemViewEvents.Remove(it.id))
        }
    }
}).attachToRecyclerView(recyclerView)
```
[仿QQ侧滑删除示例](https://github.com/BeauteousJade/SlideDeleteDemo)
```kotlin
class AFragment : Fragment() {

    private lateinit var tracker: SelectionTracker<Long>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //......
        adapter = MyAdapter()

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        tracker = SelectionTracker.Builder<Long>(
            "appt_unreviewed_selection",
            recyclerView,
            MyAdapter.KeyProvider(recyclerView),
            MyAdapter.DetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(MyAdapter.SelectionPredicate(recyclerView)).build()
        adapter.setSelectionTracker(tracker)
        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionRefresh() {
                super.onSelectionRefresh()
                adapter.refreshSelection()
            }

            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val size = tracker.selection.size()
            }
        })
        //......
    }

    
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        tracker.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }
    
    internal class MyAdapter(
        workerDispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) : PagingDataAdapter<Item, MyAdapter.MyHolder>(
        diffCallback = COMPARATOR,
        workerDispatcher = workerDispatcher
    ) {
        //......
        override fun onBindViewHolder(holder: VisitAppointHolder, position: Int) {
            holder.bindView(position, getData(position), PAYLOAD_ALL)
        }

        internal inner class MyHolder(
            itemView: View,
            val binding: ItemBinding
        ) : RecyclerView.ViewHolder(itemView) {

            private val details = ItemDetails()

            fun bindView(
                position: Int,
                data: Item?,
                payload: Set<*>
            ) {
                //val index = if (data == null) -position else position
                val key = if (data == null) -position.toLong() else position.toLong()
                details.setIndex(position)
                details.setKey(key)
                binding.tvNumber.tag = key
                // .....
            }

        }

        class ItemDetails : ItemDetailsLookup.ItemDetails<Long>() {

            private var mIndex: Int = RecyclerView.NO_POSITION
            private var mKey: Long = -1

            override fun getPosition(): Int {
                return mIndex
            }

            fun setIndex(index: Int) {
                this.mIndex = index
            }

            fun setKey(key: Long) {
                this.mKey = key
            }

            override fun getSelectionKey(): Long {
                return mKey
            }

            override fun inSelectionHotspot(e: MotionEvent): Boolean {
                return false
            }

            override fun inDragRegion(e: MotionEvent): Boolean {
                return mKey >= 0
            }
        }

        class DetailsLookup(
            private val recyclerView: RecyclerView
        ) : ItemDetailsLookup<Long>() {
            override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
                val view = recyclerView.findChildViewUnder(e.x, e.y)
                val viewHolder = view?.let {
                    try {
                        recyclerView.getChildViewHolder(it)
                    } catch (e: Throwable) {
                        null
                    }
                }
                if (viewHolder is MyAdapter.MyHolder) {
                    return viewHolder.getItemDetails()
                }
                return null
            }
        }



        class KeyProvider(
            private val recyclerView: RecyclerView
        ) : ItemKeyProvider<Long?>(SCOPE_MAPPED) {

            override fun getKey(position: Int): Long? {
                if (position < 0) return null
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                if (viewHolder is MyAdapter.MyHolder) {
                    return viewHolder.getItemDetails().selectionKey
                }
                return null
            }

            override fun getPosition(@NonNull key: Long): Int {
                return abs(key).toInt()
            }

        }

        class SelectionPredicate(
            private val recyclerView: RecyclerView
        ) : SelectionTracker.SelectionPredicate<Long>() {
            override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {
                if (!nextState) return true
                return key >= 0
            }

            override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean {
                if (!nextState) return true
                if (position < 0) return false
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                if (viewHolder is MyAdapter.MyHolder) {
                    return canSetStateForKey(viewHolder.getItemDetails().selectionKey, nextState)
                }
                return false
            }

            override fun canSelectMultiple(): Boolean = true

        }
    }

}

```
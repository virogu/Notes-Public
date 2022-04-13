import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import java.util.*
import kotlin.math.floor
import kotlin.math.pow

/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */
class EchelonLayoutManager : LinearLayoutManager {
    private var mItemViewWidth = 0
    private var mItemViewHeight = 0
    private var mItemCount = 0
    private var mScrollOffset = Int.MAX_VALUE
    private val mScale = 0.9f

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0 || state.isPreLayout) return
        removeAndRecycleAllViews(recycler)
        mItemViewWidth = horizontalSpace
        mItemViewHeight = (verticalSpace * 0.8).toInt()
        mItemCount = itemCount
        mScrollOffset = mItemViewHeight.coerceAtLeast(mScrollOffset).coerceAtMost(mItemCount * mItemViewHeight)
        layoutChild(recycler)
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        val pendingScrollOffset = mScrollOffset + dy
        mScrollOffset = mItemViewHeight.coerceAtLeast(mScrollOffset + dy).coerceAtMost(mItemCount * mItemViewHeight)
        layoutChild(recycler)
        return mScrollOffset - pendingScrollOffset + dy
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    private fun layoutChild(recycler: Recycler) {
        if (itemCount == 0) return
        var bottomItemPosition = floor(mScrollOffset / mItemViewHeight.toDouble()).toInt()
        var remainSpace = verticalSpace - mItemViewHeight
        val bottomItemVisibleHeight = mScrollOffset % mItemViewHeight
        val offsetPercentRelativeToItemView = bottomItemVisibleHeight * 1.0f / mItemViewHeight
        val layoutInfos: ArrayList<ItemViewInfo> = ArrayList<ItemViewInfo>()
        run {
            var i = bottomItemPosition - 1
            var j = 1
            while (i >= 0) {
                val maxOffset = (verticalSpace - mItemViewHeight) / 2 * 0.8.pow(j.toDouble())
                val start = (remainSpace - offsetPercentRelativeToItemView * maxOffset).toInt()
                val scaleXY = (mScale.toDouble().pow(j - 1.toDouble()) * (1 - offsetPercentRelativeToItemView * (1 - mScale))).toFloat()
                val layoutPercent = start * 1.0f / verticalSpace
                val info = ItemViewInfo(start, scaleXY, offsetPercentRelativeToItemView, layoutPercent)
                layoutInfos.add(0, info)
                remainSpace = (remainSpace - maxOffset).toInt()
                if (remainSpace <= 0) {
                    info.top = (remainSpace + maxOffset).toInt()
                    info.positionOffset = 0f
                    info.layoutPercent = (info.top / verticalSpace).toFloat()
                    info.scaleXY = mScale.toDouble().pow(j - 1.toDouble()).toFloat()
                    break
                }
                i--
                j++
            }
        }
        if (bottomItemPosition < mItemCount) {
            val start = verticalSpace - bottomItemVisibleHeight
            layoutInfos.add(ItemViewInfo(start, 1.0f, bottomItemVisibleHeight * 1.0f / mItemViewHeight, start * 1.0f / verticalSpace)
                    .setIsBottom())
        } else {
            bottomItemPosition -= 1 //99
        }
        val layoutCount = layoutInfos.size
        val startPos = bottomItemPosition - (layoutCount - 1)
        val endPos = bottomItemPosition
        val childCount = childCount
        for (i in childCount - 1 downTo 0) {
            val childView = getChildAt(i)
            val pos = getPosition(childView!!)
            if (pos > endPos || pos < startPos) {
                removeAndRecycleView(childView, recycler)
            }
        }
        detachAndScrapAttachedViews(recycler)
        for (i in 0 until layoutCount) {
            val view = recycler.getViewForPosition(startPos + i)
            val layoutInfo: ItemViewInfo = layoutInfos[i]
            addView(view)
            measureChildWithExactlySize(view)
            val left = (horizontalSpace - mItemViewWidth) / 2
            layoutDecoratedWithMargins(view, left, layoutInfo.top, left + mItemViewWidth, layoutInfo.top + mItemViewHeight)
            view.pivotX = view.width / 2.toFloat()
            view.pivotY = 0f
            view.scaleX = layoutInfo.scaleXY
            view.scaleY = layoutInfo.scaleXY
        }
    }

    /**
     * 测量itemview的确切大小
     */
    private fun measureChildWithExactlySize(child: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(mItemViewWidth, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(mItemViewHeight, View.MeasureSpec.EXACTLY)
        child.measure(widthSpec, heightSpec)
    }

    /**
     * 获取RecyclerView的显示高度
     */
    private val verticalSpace: Int
        get() = height - paddingTop - paddingBottom

    /**
     * 获取RecyclerView的显示宽度
     */
    private val horizontalSpace: Int
        get() = width - paddingLeft - paddingRight
}

class ItemViewInfo(
        var top: Int,
        var scaleXY: Float,
        var positionOffset: Float,
        var layoutPercent: Float
) {
    private var mIsBottom = false
    fun setIsBottom(): ItemViewInfo {
        mIsBottom = true
        return this
    }

}

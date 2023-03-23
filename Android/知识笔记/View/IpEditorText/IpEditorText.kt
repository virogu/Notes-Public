import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import lonbon.hospital.ui.theme.R

class IpEditorText(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {

    private data class EditorView(
        val text: EditText,
        val suffixTextView: TextView? = null
    )

    private lateinit var boxBackground: MaterialShapeDrawable
    private lateinit var shapeAppearanceModel: ShapeAppearanceModel

    private var editorViews: List<EditorView> = emptyList()
    private var itemDisableIndex: Set<Int> = emptySet()

    private var itemWidth: Int
    private var itemHeight: Int

    private var textSize: Float
    private var textColor: Int
    private var textDisableColor: Int

    private var mBackgroundColor: Int
    private var boxStrokeColor: Int
    private var boxStrokeWidth: Float
    private var boxCornerRadius: Float
    private var boxCornerRadiusTopStart: Float
    private var boxCornerRadiusTopEnd: Float
    private var boxCornerRadiusBottomStart: Float
    private var boxCornerRadiusBottomEnd: Float

    private var ips: SparseArray<String> = SparseArray()
    private var ipViewCount = 4

    private var suffix = "."

    private var ipText: String? = null

    init {
        context.obtainStyledAttributes(
            attrs, R.styleable.IpEditorText
        ).also {
            suffix = it.getString(R.styleable.IpEditorText_splitText) ?: suffix
            ipViewCount = it.getInt(R.styleable.IpEditorText_itemCount, 4)
            ipViewCount = ipViewCount.coerceAtLeast(1).coerceAtMost(4)
            itemWidth = it.getDimensionPixelSize(R.styleable.IpEditorText_itemWidth, -2)
            itemHeight = it.getDimensionPixelSize(R.styleable.IpEditorText_itemHeight, -2)

            textSize = it.getDimension(
                R.styleable.IpEditorText_textSize,
                context.resources.getDimension(R.dimen.sp_22)
            )

            textColor = it.getColor(R.styleable.IpEditorText_textColor, Color.DKGRAY)
            textDisableColor =
                it.getColor(R.styleable.IpEditorText_textDisableColor, Color.LTGRAY)

            mBackgroundColor =
                it.getColor(R.styleable.IpEditorText_boxBackgroundColor, Color.TRANSPARENT)
            boxStrokeColor =
                it.getColor(R.styleable.IpEditorText_boxStrokeColor, Color.TRANSPARENT)
            boxStrokeWidth = it.getDimension(R.styleable.IpEditorText_boxStrokeWidth, 0f)

            boxCornerRadius = it.getDimension(R.styleable.IpEditorText_boxCornerRadius, 0f)
            boxCornerRadiusTopStart =
                it.getDimension(R.styleable.IpEditorText_boxCornerRadiusTopStart, -1f)
            boxCornerRadiusTopEnd =
                it.getDimension(R.styleable.IpEditorText_boxCornerRadiusTopEnd, -1f)
            boxCornerRadiusBottomStart =
                it.getDimension(R.styleable.IpEditorText_boxCornerRadiusBottomStart, -1f)
            boxCornerRadiusBottomEnd =
                it.getDimension(R.styleable.IpEditorText_boxCornerRadiusBottomEnd, -1f)

            ipText = it.getString(R.styleable.IpEditorText_ipText)
            itemDisableIndex = it.getString(
                R.styleable.IpEditorText_itemDisableIndex
            )?.split(",")?.mapNotNull { s ->
                s.toIntOrNull()
            }?.toSet().orEmpty()

            it.recycle()
        }
        initView()
    }

    private fun initView() {
        createLayouts()
        updateLayoutsParams()
        updateBackground()
        updateIp()
    }

    private fun createLayouts() {
        removeAllViews()
        editorViews = List(ipViewCount) { index ->
            val textView = AppCompatEditText(context).apply {
                layoutParams = LayoutParams(itemWidth, itemHeight, 1f).apply {
                    gravity = Gravity.CENTER
                    setPadding(0, 0, 0, 0)
                }
                setPadding(0, 0, 0, 0)
                gravity = Gravity.CENTER
                background = null
                inputType = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
                isSingleLine = true
                maxLines = 3
                keyListener = DigitsKeyListener.getInstance("0123456789")
                addListener(index)
            }.also(::addView)

            val suffixTextView = if (index < ipViewCount - 1) {
                AppCompatTextView(context).apply {
                    layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, itemHeight).apply {
                        setPadding(0, 0, 0, 0)
                        gravity = Gravity.CENTER
                    }
                    gravity = Gravity.CENTER
                    setPadding(0, 0, 0, 0)
                    text = suffix
                }.also(::addView)
            } else {
                null
            }
            EditorView(textView, suffixTextView)
        }
    }

    private fun updateLayoutsParams() {
        updateViewEnable()
        updateTextStyle()
    }

    private fun updateViewEnable() {
        editorViews.forEachIndexed { i, editorView ->
            editorView.text.isEnabled = !itemDisableIndex.contains(i)
        }
    }

    private fun updateTextStyle() {
        editorViews.forEachIndexed { _, editorView ->
            val text = editorView.text
            val suffixTextView = editorView.suffixTextView

            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled),
                ),
                intArrayOf(textColor, textDisableColor)
            )
            suffixTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            suffixTextView?.setTextColor(colorStateList)

            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            text.setTextColor(colorStateList)
        }
    }

    private fun updateBackground() {
        shapeAppearanceModel = ShapeAppearanceModel().toBuilder().apply {
            setAllCornerSizes(boxCornerRadius)
            boxCornerRadiusTopStart.takeIf { f -> f > 0 }?.also(::setTopLeftCornerSize)
            boxCornerRadiusTopEnd.takeIf { f -> f > 0 }?.also(::setTopRightCornerSize)
            boxCornerRadiusBottomStart.takeIf { f -> f > 0 }?.also(::setBottomLeftCornerSize)
            boxCornerRadiusBottomEnd.takeIf { f -> f > 0 }?.also(::setBottomRightCornerSize)
        }.build()

        boxBackground = MaterialShapeDrawable(shapeAppearanceModel).also { d ->
            d.strokeWidth = boxStrokeWidth
            d.strokeColor = ColorStateList.valueOf(boxStrokeColor)
            d.fillColor = ColorStateList.valueOf(mBackgroundColor)
        }
        this.background = boxBackground
    }

    private fun EditText.addListener(index: Int) {
        setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (this.text.isNullOrEmpty()) {
                    toPreEditor(index)
                }
            }
            return@setOnKeyListener false
        }
        addTextChangedListener(object : TextWatcher {
            private var ip: String = ""

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                char: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (char.isNullOrEmpty()) {
                    ip = ""
                    return
                }
                if (char.length > 2) {
                    val s = char.toString().toIntOrNull() ?: Int.MAX_VALUE
                    ip = if (s > 255) {
                        "255"
                    } else {
                        "$s"
                    }
                    toNextEditor(index)
                } else {
                    ip = char.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                removeTextChangedListener(this)
                ips[index] = ip
                setText(ip)
                setSelection(ip.length)
                addTextChangedListener(this)
            }

        })
        val next = toNextEditor(index)
        imeOptions = if (next != null) {
            EditorInfo.IME_ACTION_NEXT or EditorInfo.IME_FLAG_NO_FULLSCREEN
        } else {
            EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN
        }
    }

    private fun toPreEditor(index: Int): EditText? {
        return editorViews.getOrNull(index - 1)?.let { prev ->
            val editText = prev.text
            if (editText.isEnabled) {
                editText.requestFocus()
                editText.setSelection(editText.text.length)
                editText
            } else {
                toPreEditor(index - 1)
            }
        }
    }

    private fun toNextEditor(index: Int): EditText? {
        return editorViews.getOrNull(index + 1)?.let { next ->
            val editText = next.text
            if (editText.isEnabled) {
                editText.requestFocus()
                editText.setSelection(editText.text.length)
                editText
            } else {
                toNextEditor(index + 1)
            }
        }
    }

    fun setItemCount(
        @androidx.annotation.IntRange(from = 1, to = 4)
        count: Int
    ) {
        this.ipViewCount = count.coerceAtLeast(1).coerceAtMost(4)
        initView()
    }

    fun setDisableItems(vararg index: Int) {
        this.itemDisableIndex = index.toSet()
        updateViewEnable()
    }

    fun setSuffix(suffix: String) {
        this.ipText = this.ipText?.replace(this.suffix, suffix)
        this.suffix = suffix
        editorViews.forEachIndexed { _, v ->
            v.suffixTextView?.text = suffix
        }
    }

    fun getIp(): String {
        val s = run {
            List(editorViews.size) { index ->
                val ip = ips.get(index, "")
                if (ip.isNullOrEmpty()) {
                    return@run ""
                }
                ip
            }.joinToString(suffix)
        }
        this.ipText = s
        return s
    }

    fun setIp(ip: String?) {
        this.ipText = ip
        updateIp()
    }

    private fun updateIp() {
        val ip = ipText
        if (ip.isNullOrEmpty()) {
            editorViews.forEach {
                it.text.text.clear()
            }
            return
        }
        val temp = ip.split(suffix)
        if (temp.isEmpty() || temp.size < editorViews.size) {
            editorViews.forEach {
                it.text.text.clear()
            }
            return
        }
        editorViews.forEachIndexed { index, editorView ->
            val text = editorView.text
            val s = temp[index].let {
                val i = it.toIntOrNull() ?: 0
                if (i < 0) {
                    "0"
                } else if (i > 255) {
                    "255"
                } else {
                    it
                }
            }
            ips[index] = s
            text.setText(s)
        }
    }

}
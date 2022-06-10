class AnimeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x55FFFFFF
    }

    private val topPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xAAFFFFFF.toInt()
    }

    private var topLineOffsetX = 0f
    private val topLineLength get() = width / 4f

    private val animation = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 2000L
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            val value = it.animatedValue as Float
            topLineOffsetX = (width * .75f) * value
            postInvalidate()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animation.start()
    }

    override fun onDetachedFromWindow() {
        animation.cancel()
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.drawRoundRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                height / 2f,
                height / 2f,
                backgroundPaint
            )
            it.drawRoundRect(
                topLineOffsetX,
                0f,
                topLineOffsetX + topLineLength,
                height.toFloat(),
                height / 2f,
                height / 2f,
                topPaint
            )
        }
    }
}

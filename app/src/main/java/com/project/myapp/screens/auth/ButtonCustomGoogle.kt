package com.project.myapp.screens.auth

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.project.myapp.R
import java.lang.Integer.max

class ButtonCustomGoogle @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int = R.attr.googleButtonStyle,
    defStyleRes: Int = 0,
) : View(context, attributeSet, defStyleAttr, defStyleRes) {
    // attr variables
    private var buttonText: String = DEFAULT_TEXT
    private var textColor: Int = DEFAULT_TEXT_COLOR
    private var backgroundColor: Int = DEFAULT_BACKGROUND_COLOR
    private var cornerRadius: Float = DEFAULT_CORNER_RADIUS
    private var textAllCaps: Boolean = DEFAULT_TEXT_ALL_CAPS
    private var textSize = DEFAULT_TEXT_SIZE
    private var textSpacing = DEFAULT_TEXT_SPACING
    private var iconDrawable: Drawable? = null
    private var heightPercent = 0F
    private var widthPercent = 0F

    // paint initialization
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rect = RectF()

    init {
        initAttributes(attributeSet, defStyleAttr, defStyleRes)
        isClickable = true
        isFocusable = true
        setupPaint()
    }

    private fun initAttributes(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ButtonCustomGoogle, defStyleAttr, defStyleRes)
        try {
            buttonText = ta.getString(R.styleable.ButtonCustomGoogle_googleButtonText) ?: DEFAULT_TEXT
            textColor = ta.getColor(R.styleable.ButtonCustomGoogle_googleTextColor, DEFAULT_TEXT_COLOR)
            backgroundColor = ta.getColor(R.styleable.ButtonCustomGoogle_googleBackgroundColor, DEFAULT_BACKGROUND_COLOR)
            cornerRadius = ta.getDimension(R.styleable.ButtonCustomGoogle_googleCornerRadius, DEFAULT_CORNER_RADIUS)
            textAllCaps = ta.getBoolean(R.styleable.ButtonCustomGoogle_googleTextAllCaps, DEFAULT_TEXT_ALL_CAPS)
            textSize = ta.getDimension(R.styleable.ButtonCustomGoogle_googleTextSize, DEFAULT_TEXT_SIZE)
            textSpacing = ta.getFloat(R.styleable.ButtonCustomGoogle_googleLetterSpacing, DEFAULT_TEXT_SPACING)
            iconDrawable = ta.getDrawable(R.styleable.ButtonCustomGoogle_googleIconDrawable)
                ?: ContextCompat.getDrawable(context, R.drawable.auth_android_google_icon)
            heightPercent = ta.getFloat(R.styleable.ButtonCustomGoogle_heightPercent, 0f)
            widthPercent = ta.getFloat(R.styleable.ButtonCustomGoogle_widthPercent, 0f)
        } finally {
            ta.recycle()
        }
    }

    private fun setupPaint() {
        // background settings
        backgroundPaint.color = backgroundColor
        backgroundPaint.style = Paint.Style.FILL

        // text settings
        val typeface = ResourcesCompat.getFont(context, R.font.open_sans_semibold)
        textPaint.typeface = typeface
        textPaint.letterSpacing = textSpacing
        textPaint.color = textColor
        textPaint.textSize = textSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // draws the background
        drawBackground(canvas)

        // calculates metrics (text, drawable, positions), stores them in a data class
        val metrics = calculateContentMetrics()

        // draws icon
        drawIcon(canvas, metrics)

        // draws text
        drawText(canvas, metrics)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, backgroundPaint)
    }

    private fun calculateContentMetrics(): ContentMetrics {
        // prepares text
        val displayText = if (textAllCaps) buttonText.uppercase() else buttonText
        val textWidth = textPaint.measureText(displayText)
        val fontMetrics = textPaint.fontMetrics
        val textHeight = fontMetrics.descent + fontMetrics.ascent
        val centreLine = height / 2f - textHeight / 2

        // prepares drawable size
        val drawableWidth = iconDrawable?.intrinsicWidth ?: 0
        val drawableHeight = iconDrawable?.intrinsicHeight ?: 0

        // total content width
        val totalContentWidth = drawableWidth + textWidth

        // Start horizontal point for all content
        val startX = (width - totalContentWidth) / 2f

        // Start horizontal point for text
        val textStartX = startX + drawableWidth

        return ContentMetrics(
            displayText,
            textWidth,
            drawableWidth,
            drawableHeight,
            totalContentWidth,
            startX,
            textStartX,
            centreLine,
        )
    }

    private fun drawIcon(canvas: Canvas, m: ContentMetrics) {
        iconDrawable?.let { drawable ->
            val top = (height - m.drawableHeight) / 2
            val left = m.startX.toInt()
            val right = left + m.drawableWidth
            val bottom = top + m.drawableHeight
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
    }

    private fun drawText(canvas: Canvas, m: ContentMetrics) {
        canvas.drawText(m.displayText, m.textStartX, m.baseline, textPaint)
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
    ) {
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels
        val minHeight = resources.getDimensionPixelSize(R.dimen.height_min_bt_google)
        val maxHeight = resources.getDimensionPixelSize(R.dimen.height_max_bt_google)

        val desiredWidth = (screenWidth * widthPercent).toInt()

        val calculatedHeight = (screenHeight * heightPercent).toInt()
        val desiredHeight = if (calculatedHeight <= maxHeight) max(calculatedHeight, minHeight) else maxHeight

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            performClick()
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        Toast.makeText(context, "Google Button was clicked", Toast.LENGTH_SHORT).show()
        return true
    }

    private data class ContentMetrics(
        val displayText: String,
        val textWidth: Float,
        val drawableWidth: Int,
        val drawableHeight: Int,
        val totalContentWidth: Float,
        val startX: Float,
        val textStartX: Float,
        val baseline: Float,
    )

    companion object {
        private const val DEFAULT_CORNER_RADIUS = 24f
        private const val DEFAULT_TEXT = "Google"
        private const val DEFAULT_TEXT_ALL_CAPS = true
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_BACKGROUND_COLOR = Color.WHITE
        private const val DEFAULT_TEXT_SIZE = 34f
        private const val DEFAULT_TEXT_SPACING = 0.15F
    }
}

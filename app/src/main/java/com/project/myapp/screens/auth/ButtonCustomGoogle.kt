package com.project.myapp.screens.auth

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
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
    private var customFont: Typeface? = null
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
    private var textMetrics: TextMetrics? = null
    private var displayText = ""

    init {
        initAttributes(attributeSet, defStyleAttr, defStyleRes)
        displayText = if (textAllCaps) buttonText.uppercase() else buttonText
        isClickable = true
        isFocusable = true
        setupPaint()
    }

    private fun initAttributes(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonCustomGoogle, defStyleAttr, defStyleRes)
        try {
            customFont = loadTypeface(context, typedArray)
            buttonText = typedArray.getString(R.styleable.ButtonCustomGoogle_googleButtonText) ?: DEFAULT_TEXT
            textColor = typedArray.getColor(R.styleable.ButtonCustomGoogle_googleTextColor, DEFAULT_TEXT_COLOR)
            backgroundColor = typedArray.getColor(R.styleable.ButtonCustomGoogle_googleBackgroundColor, DEFAULT_BACKGROUND_COLOR)
            cornerRadius = typedArray.getDimension(R.styleable.ButtonCustomGoogle_googleCornerRadius, DEFAULT_CORNER_RADIUS)
            textAllCaps = typedArray.getBoolean(R.styleable.ButtonCustomGoogle_googleTextAllCaps, DEFAULT_TEXT_ALL_CAPS)
            textSize = typedArray.getDimension(R.styleable.ButtonCustomGoogle_googleTextSize, DEFAULT_TEXT_SIZE)
            textSpacing = typedArray.getFloat(R.styleable.ButtonCustomGoogle_googleLetterSpacing, DEFAULT_TEXT_SPACING)
            iconDrawable = typedArray.getDrawable(R.styleable.ButtonCustomGoogle_googleIconDrawable)
                ?: ContextCompat.getDrawable(context, R.drawable.auth_android_google_icon)
            heightPercent = typedArray.getFloat(R.styleable.ButtonCustomGoogle_heightPercent, 0f)
            widthPercent = typedArray.getFloat(R.styleable.ButtonCustomGoogle_widthPercent, 0f)
        } finally {
            typedArray.recycle()
        }
    }

    private fun loadTypeface(context: Context, typedArray: TypedArray): Typeface? {
        val fontResId = typedArray.getResourceId(R.styleable.ButtonCustomGoogle_customFont, 0)

        return when {
            fontResId != 0 -> {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.resources.getFont(fontResId)
                    } else {
                        ResourcesCompat.getFont(context, fontResId)
                    }
                } catch (e: Exception) {
                    Typeface.DEFAULT
                }
            }
            else -> Typeface.DEFAULT
        }
    }

    private fun setupPaint() {
        // background settings
        backgroundPaint.color = backgroundColor
        backgroundPaint.style = Paint.Style.FILL

        // text settings
        textPaint.typeface = customFont
        textPaint.letterSpacing = textSpacing
        textPaint.color = textColor
        textPaint.textSize = textSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawIcon(canvas)
        drawText(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, backgroundPaint)
    }

    private fun calculateContentMetricsSetBounds(width:Int, height:Int ) {
        // prepares text settings
        val textWidth = textPaint.measureText(displayText)
        val fontMetrics = textPaint.fontMetrics
        val textHeight = fontMetrics.descent + fontMetrics.ascent
        val centreLine = height / 2f - textHeight / 2

        // prepares drawable size
        val drawableWidth = iconDrawable?.intrinsicWidth ?: DEFAULT_DRAWABLE_SIZE
        val drawableHeight = iconDrawable?.intrinsicHeight ?: DEFAULT_DRAWABLE_SIZE
        val totalContentWidth = drawableWidth + textWidth

        val startX = (width - totalContentWidth) / 2f
        val textStartX = startX + drawableWidth

        val iconLeftBound = startX.toInt()
        val iconTopBound = (height - drawableHeight) / 2
        val iconRightBound = iconLeftBound + drawableWidth
        val iconBottomBound = iconTopBound + drawableHeight

        iconDrawable?.setBounds(iconLeftBound, iconTopBound, iconRightBound, iconBottomBound)

        textMetrics =
            TextMetrics(
                textStartX,
                centreLine,
            )
    }

    private fun drawIcon(canvas: Canvas) {
        iconDrawable?.draw(canvas)
    }

    private fun drawText(canvas: Canvas) {
        textMetrics?.let {
            canvas.drawText(displayText, it.textStartX, it.baselineText, textPaint)
        }
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
        if (w <= 0 || h <= 0) return
        if (w == oldw && h == oldh) return
        rect.set(0f, 0f, w.toFloat(), h.toFloat())
        calculateContentMetricsSetBounds(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()
        Toast.makeText(context, "Google Button was clicked", Toast.LENGTH_SHORT).show()
        return true
    }

    private data class TextMetrics(
        val textStartX: Float,
        val baselineText: Float,
    )

    companion object {
        private const val DEFAULT_CORNER_RADIUS = 24f
        private const val DEFAULT_TEXT = "Google"
        private const val DEFAULT_TEXT_ALL_CAPS = true
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_BACKGROUND_COLOR = Color.WHITE
        private const val DEFAULT_TEXT_SIZE = 34f
        private const val DEFAULT_TEXT_SPACING = 0.15F
        private const val DEFAULT_DRAWABLE_SIZE = 0
    }
}

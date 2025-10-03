package com.project.myapp.screens.contacts

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator(
    private val spacing: Int,
    private val radius: Float,
    private val strokeColor: Int,
    private val strokeWidth: Float,
) : RecyclerView.ItemDecoration() {
    private val paint =
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            this.strokeWidth = this@ItemDecorator.strokeWidth
            color = this@ItemDecorator.strokeColor
        }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.set(spacing, spacing, spacing, spacing / 2)
        } else {
            outRect.set(spacing, spacing / 2, spacing, spacing / 2)
        }
    }

    override fun onDraw(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val left = child.left.toFloat()
            val top = child.top.toFloat()
            val right = child.right.toFloat()
            val bottom = child.bottom.toFloat()

            val rectF = RectF(left, top, right, bottom)
            canvas.drawRoundRect(rectF, radius, radius, paint)
        }
    }
}

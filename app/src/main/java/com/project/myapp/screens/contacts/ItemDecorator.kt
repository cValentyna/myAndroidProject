package com.project.myapp.screens.contacts

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator(
    private val spacing: Int,
) : RecyclerView.ItemDecoration() {
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
}

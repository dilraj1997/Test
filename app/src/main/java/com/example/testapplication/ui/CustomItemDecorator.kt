package com.example.testapplication.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(
    private val left: Int,
    private val top: Int,
    private val bottom: Int,
    private val right: Int
) : RecyclerView.ItemDecoration() {

    constructor(margin: Int) : this(margin, margin, margin, margin)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position >= 0) {
            outRect.left = left
            outRect.top = top / 2
            outRect.bottom = bottom / 2
            outRect.right = right
        }
    }
}

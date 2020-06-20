package com.mycars.baseui.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MediaSpaceDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        val totalSpanCount = getTotalSpanCount(parent)
        val spanSize = getItemSpanSize(parent, position)
        if (totalSpanCount == spanSize) {
            return
        }

        outRect.top = if (isInTheFirstRow(position, totalSpanCount)) 0 else spacing
        outRect.left = if (isFirstInRow(position, totalSpanCount)) 0 else spacing / 2
        outRect.right = if (isLastInRow(position, totalSpanCount)) 0 else spacing / 2
        outRect.bottom = 0 // don't need
    }

    private fun isInTheFirstRow(position: Int, spanCount: Int): Boolean {
        return position < spanCount
    }

    private fun isFirstInRow(position: Int, spanCount: Int): Boolean {
        return position % spanCount == 0
    }

    private fun isLastInRow(position: Int, spanCount: Int): Boolean {
        return isFirstInRow(position + 1, spanCount)
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return (layoutManager as? GridLayoutManager)?.spanCount ?: 1
    }

    private fun getItemSpanSize(parent: RecyclerView, position: Int): Int {
        val layoutManager = parent.layoutManager
        return (layoutManager as? GridLayoutManager)?.spanSizeLookup?.getSpanSize(position) ?: 1
    }
}

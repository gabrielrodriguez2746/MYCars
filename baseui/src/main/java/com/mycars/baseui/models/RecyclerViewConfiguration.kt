package com.mycars.baseui.models

import androidx.recyclerview.widget.RecyclerView

data class RecyclerViewConfiguration(
    val layoutManager: RecyclerView.LayoutManager,
    val isNestedScroll: Boolean = false,
    val hasFixedSize: Boolean = true,
    val adapter: RecyclerView.Adapter<*>,
    val decorator: RecyclerView.ItemDecoration? = null
)
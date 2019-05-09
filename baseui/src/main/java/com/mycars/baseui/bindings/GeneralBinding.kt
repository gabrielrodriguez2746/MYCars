package com.mycars.baseui.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mycars.baseui.models.RecyclerViewConfiguration

@BindingAdapter("configuration")
fun RecyclerView.configure(configuration: RecyclerViewConfiguration) {
    layoutManager = configuration.layoutManager
    adapter = configuration.adapter
    isNestedScrollingEnabled = configuration.isNestedScroll
    setHasFixedSize(configuration.hasFixedSize)
}
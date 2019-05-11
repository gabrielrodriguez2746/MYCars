package com.mycars.carsui.binding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("image")
fun ImageView.setImage(@DrawableRes image: Int) {
    setImageResource(image)
}
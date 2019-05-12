package com.mycars.baseui.helpers

import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat

fun View.addRippleForeground() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val outValue = TypedValue()
        context.theme.resolveAttribute(
            android.R.attr.selectableItemBackgroundBorderless, outValue, true)
        foreground = ContextCompat.getDrawable(context, outValue.resourceId)
    }
}
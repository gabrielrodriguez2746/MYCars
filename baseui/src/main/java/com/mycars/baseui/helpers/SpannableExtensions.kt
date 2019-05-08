package com.mycars.baseui.helpers

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan

fun SpannableString.applySpans(indexes: List<Int>,
                               colorSpan: ForegroundColorSpan? = null,
                               styleSpan: StyleSpan? = null,
                               strikeThroughSpan: StrikethroughSpan? = null,
                               sizeSpan: RelativeSizeSpan? = null): SpannableString {
    if (indexes.size > 1) {
        var index = 0
        while (index + 1 < indexes.size) {
            colorSpan?.let { this.setSpan(ForegroundColorSpan(it.foregroundColor), indexes[index], indexes[index + 1], 0) }
            styleSpan?.let { this.setSpan(StyleSpan(it.style), indexes[index], indexes[index + 1], 0) }
            strikeThroughSpan?.let { this.setSpan(StrikethroughSpan(), indexes[index], indexes[index + 1], 0) }
            sizeSpan?.let { this.setSpan(RelativeSizeSpan(it.sizeChange), indexes[index], indexes[index + 1], 0) }
            index += 2
        }
    }
    return this
}
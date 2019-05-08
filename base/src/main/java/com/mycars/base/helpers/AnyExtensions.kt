package com.mycars.base.helpers

import android.util.Log

inline fun <T> tryOrDefault(f: () -> T, defaultValue: T): T {
    return try {
        f()
    } catch (e: Exception) {
        Log.d("TRY_OR_DEFAULT", e.localizedMessage)
        defaultValue
    }
}

inline fun tryOrPrintException(f: () -> Unit) {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
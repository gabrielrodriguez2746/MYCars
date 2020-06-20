package com.mycars.base.helpers

import android.util.Log

@Suppress("TooGenericExceptionCaught")
inline fun <T> tryOrDefault(f: () -> T, defaultValue: T): T {
    return try {
        f()
    } catch (e: Exception) {
        Log.d("TRY_OR_DEFAULT", e.localizedMessage)
        defaultValue
    }
}

@Suppress("TooGenericExceptionCaught")
inline fun tryOrPrintException(f: () -> Unit) {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

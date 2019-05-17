package com.mycars.base.helpers

import retrofit2.CallAdapter

inline fun <reified T : CallAdapter<Any, Any>> CallAdapter<*, *>.asCallAdapterOfType(): CallAdapter<Any, Any>? {
    return if (this is T) this else null
}

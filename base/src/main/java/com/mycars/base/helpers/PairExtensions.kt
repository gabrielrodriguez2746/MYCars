package com.mycars.base.helpers

@Suppress("UNCHECKED_CAST")
fun <T, U, R> Pair<T?, U?>.biLet(body: (T, U) -> R): R? {
    if (first != null && second != null) {
        return body(first as T, second as U)
    }
    return null
}

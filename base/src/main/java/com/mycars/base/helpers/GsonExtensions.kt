package com.mycars.base.helpers

import com.google.gson.JsonObject

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
inline fun <reified R> JsonObject.getGenericOrDefault(key: String, default: R): R {
    return tryOrDefault({
        get(key)?.let { keyElement ->
            when {
                keyElement.isJsonNull -> default
                keyElement.isJsonPrimitive -> {
                    val primitiveElement = keyElement.asJsonPrimitive
                    when {
                        primitiveElement.isBoolean -> primitiveElement.asBoolean
                        primitiveElement.isNumber -> primitiveElement.asNumber
                        primitiveElement.isString -> primitiveElement.asString
                        else -> primitiveElement
                    }
                }
                else -> keyElement
            } as R
        } ?: throw ClassCastException()
    }, default)
}
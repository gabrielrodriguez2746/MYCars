package com.mycars.base.config

data class IsDebug(private val isDebug: Boolean) {
    operator fun invoke() = isDebug

    companion object {
        fun Boolean.toIsDebug(): IsDebug = IsDebug(this)
    }
}
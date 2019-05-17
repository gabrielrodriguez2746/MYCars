package com.mycars.base.intents

import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

abstract class ActivityClassIntent {

    abstract fun getClassIntent(): @JvmSuppressWildcards KClass<out AppCompatActivity>
}

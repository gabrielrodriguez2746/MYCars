package com.mycars.intent

import androidx.appcompat.app.AppCompatActivity
import com.mycars.activities.MainActivity
import com.mycars.base.intents.ActivityClassIntent
import javax.inject.Inject
import kotlin.reflect.KClass

class MainActivityClass @Inject constructor() : ActivityClassIntent() {
    override fun getClassIntent(): @JvmSuppressWildcards KClass<out AppCompatActivity> = MainActivity::class
}

package com.mycars.providers

import android.content.Context
import androidx.core.content.ContextCompat
import com.mycars.base.providers.ResourceProvider
import javax.inject.Inject

class AppResourceProvider @Inject constructor(private val context: Context) : ResourceProvider {

    override fun getString(id: Int): String = context.getString(id)

    @Suppress("SpreadOperator")
    override fun getString(id: Int, vararg args: Any?): String = context.getString(id, *args)

    override fun getStringArray(id: Int): Array<String> = context.resources.getStringArray(id)

    override fun getColor(id: Int): Int = ContextCompat.getColor(context, id)
}

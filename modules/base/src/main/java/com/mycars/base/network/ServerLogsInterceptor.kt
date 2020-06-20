package com.mycars.base.network

import android.util.Log
import com.mycars.base.config.BaseConfiguration
import com.mycars.base.helpers.applyLoggingInterceptorLogs
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class ServerLogsInterceptor @Inject constructor(baseConfiguration: BaseConfiguration) :
    Interceptor by HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.e("SERVER", message)
        }
    }).applyLoggingInterceptorLogs(
        baseConfiguration.areAppLogsEnable()
    )

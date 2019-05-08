package com.mycars.config

import com.mycars.BuildConfig
import com.mycars.base.config.BaseConfiguration
import javax.inject.Inject

class AppConfiguration @Inject constructor() : BaseConfiguration {

    override fun areAppLogsEnable(): Boolean = BuildConfig.DEBUG
}
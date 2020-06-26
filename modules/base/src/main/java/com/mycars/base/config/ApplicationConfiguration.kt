package com.mycars.base.config

import javax.inject.Inject

class ApplicationConfiguration @Inject constructor(
    private val isDebug: IsDebug
) : BaseConfiguration {
    override fun areAppLogsEnable(): Boolean = isDebug()
}

package com.mycars.base.helpers

import io.kotlintest.shouldBe
import io.mockk.spyk
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InterceptorExtensionTest {

    @Nested
    inner class `Http logging interceptor` {
        private val interceptor: HttpLoggingInterceptor = spyk()

        @Test
        fun `logs enabled`() {
            interceptor.applyLoggingInterceptorLogs(true)
            interceptor.level shouldBe BODY
        }

        @Test
        fun `logs disabled`() {
            interceptor.applyLoggingInterceptorLogs(false)
            interceptor.level shouldBe NONE
        }
    }

}
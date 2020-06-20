package com.mycars.base.helpers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.kotlintest.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GsonExtensionTest {

    @Nested
    inner class `get generic or default` {

        lateinit var jsonObject: JsonObject
        private val defaultKey = "key"

        @BeforeEach
        fun beforeEach() {
            jsonObject = JsonObject()
        }

        @Test
        fun `not key`() {
            val defaultValue = "hello World"
            jsonObject.getGenericOrDefault("key", defaultValue) shouldBe defaultValue
        }

        @Test
        fun `wrong generic key value`() {
            val defaultValue = "hello World"
            jsonObject.add(defaultKey, JsonObject())
            jsonObject.getGenericOrDefault(defaultKey, defaultValue) shouldBe defaultValue
        }

        @Nested
        inner class `get key Value` {

            @Test
            fun `from String`() {
                val expectedValue = "hello World"
                val value = JsonPrimitive(expectedValue)
                val default = "Not here"
                jsonObject.add(defaultKey, value)
                jsonObject.getGenericOrDefault(defaultKey, default) shouldBe expectedValue
            }

            @Test
            fun `from Boolean`() {
                val expectedValue = true
                val value = JsonPrimitive(expectedValue)
                val default = false
                jsonObject.add(defaultKey, value)
                jsonObject.getGenericOrDefault(defaultKey, default) shouldBe expectedValue
            }

            @Test
            fun `from Number Int`() {
                val expectedValue = 50
                val value = JsonPrimitive(expectedValue)
                val default = -1
                jsonObject.add(defaultKey, value)
                jsonObject.getGenericOrDefault(defaultKey, default) shouldBe expectedValue
            }

            @Test
            fun `from Number Double`() {
                val expectedValue = 50.0
                val value = JsonPrimitive(expectedValue)
                val default = -1.0
                jsonObject.add(defaultKey, value)
                jsonObject.getGenericOrDefault(defaultKey, default) shouldBe expectedValue
            }

            @Test
            fun `from JsonObject`() {
                val value = JsonObject()
                val default = JsonObject()
                jsonObject.add(defaultKey, value)
                jsonObject.getGenericOrDefault(defaultKey, default) shouldBe value
            }

            @Test
            fun `from JsonArray`() {
                val value = JsonArray()
                val default = JsonArray()
                jsonObject.add(defaultKey, value)
                jsonObject.getGenericOrDefault(defaultKey, default) shouldBe value
            }
        }
    }
}

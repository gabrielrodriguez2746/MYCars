package com.mycars.base.helpers

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PairExtensionTest {

  @Nested
  inner class `bi Let` {

    @Test
    fun `one null parameter`() {
      val a: Any? = null
      val b = Any()
      val body = { _: Any, _: Any -> Unit }
      (a to b).biLet(body) shouldBe null
    }

    @Test
    fun `two null parameters`() {
      val a: Any? = null
      val b: Any? = null
      val body = { _: Any, _: Any -> Unit }
      (a to b).biLet(body) shouldBe null
    }

    @Test
    fun `two not null parameters`() {
      val a = Any()
      val b = Any()
      val body = { _: Any, _: Any -> Unit }
      (a to b).biLet(body) shouldBe body(a, b)
    }
  }
}
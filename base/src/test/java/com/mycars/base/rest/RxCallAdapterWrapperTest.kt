package com.mycars.base.rest

import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import java.io.IOException

class RxCallAdapterWrapperTest {

    private val genericError: String = "Generic error"
    private val networkError: String = "Network Error"
    private val baseAdapter: CallAdapter<Any, Any> = mockk()

    lateinit var adapter: RxCallAdapterWrapper

    @BeforeEach
    fun beforeEach() {
        adapter = spyk(RxCallAdapterWrapper(genericError, networkError, baseAdapter))
    }

    @Nested
    inner class `as retrofit exception` {

        @Test
        fun `http exception`() {
            val input = mockk<HttpException>()
            adapter.asRetrofitException(input).message shouldBe genericError
        }

        @Test
        fun `io exception`() {
            val input = mockk<IOException>()
            adapter.asRetrofitException(input).message shouldBe networkError
        }

        @Test
        fun `any exception`() {
            val input = Throwable()
            adapter.asRetrofitException(input) shouldBe input
        }

    }

    @Nested
    inner class `adapt call` {

        private val call = mockk<Call<Any>>()
        private val expectedError = Throwable()

        @BeforeEach
        fun beforeEach() {
            clearMocks(baseAdapter)
        }

        @Test
        fun `from Completable`() {
            every { adapter.asRetrofitException(any()) } returns expectedError
            every { baseAdapter.adapt(any()) } returns Completable.error(expectedError)

            with(adapter.adapt(call)) {
                shouldBeInstanceOf<Completable>()
                (this as Completable).test().assertError(expectedError)
            }

        }

        @Test
        fun `from Single`() {
            every { adapter.asRetrofitException(any()) } returns expectedError
            every { baseAdapter.adapt(any()) } returns Single.error<Any>(expectedError)

            with(adapter.adapt(call)) {
                shouldBeInstanceOf<Single<*>>()
                (this as Single<*>).test().assertError(expectedError)
            }

        }

        @Test
        fun `from Observable`() {
            every { adapter.asRetrofitException(any()) } returns expectedError
            every { baseAdapter.adapt(any()) } returns Observable.error<Any>(expectedError)

            with(adapter.adapt(call)) {
                shouldBeInstanceOf<Observable<*>>()
                (this as Observable<*>).test().assertError(expectedError)
            }
        }

        @Test
        fun `from Flowable`() {
            every { adapter.asRetrofitException(any()) } returns expectedError
            every { baseAdapter.adapt(any()) } returns Flowable.error<Any>(expectedError)

            with(adapter.adapt(call)) {
                shouldBeInstanceOf<Flowable<*>>()
                (this as Flowable<*>).test().assertError(expectedError)
            }
        }

        @Test
        fun `from Maybe`() {
            every { adapter.asRetrofitException(any()) } returns expectedError
            every { baseAdapter.adapt(any()) } returns Maybe.error<Any>(expectedError)

            with(adapter.adapt(call)) {
                shouldBeInstanceOf<Maybe<*>>()
                (this as Maybe<*>).test().assertError(expectedError)
            }
        }

        @Test
        fun `from unknown`() {
            every { adapter.asRetrofitException(any()) } returns expectedError
            every { baseAdapter.adapt(any()) } returns Any()

            shouldThrow<RuntimeException> {
                adapter.adapt(call)
            }

        }
    }

}
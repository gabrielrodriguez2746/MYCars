package com.mycars.base.rest

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type

class RxCallAdapterWrapper constructor(
    private val genericError: String,
    private val networkError: String,
    private val baseAdapter: CallAdapter<Any, Any>
) : CallAdapter<Any, Any> {

    override fun responseType(): Type {
        return baseAdapter.responseType()
    }

    @Suppress("TooGenericExceptionThrown")
    override fun adapt(call: Call<Any>): Any {

        return when (val adaptedCall = baseAdapter.adapt(call)) {
            is Completable -> adaptedCall.onErrorResumeNext { throwable ->
                Completable.error(asRetrofitException(throwable))
            }
            is Single<*> -> adaptedCall.onErrorResumeNext { throwable ->
                Single.error(asRetrofitException(throwable))
            }
            is Observable<*> -> adaptedCall.onErrorResumeNext { throwable: Throwable ->
                Observable.error(asRetrofitException(throwable))
            }
            is Flowable<*> -> adaptedCall.onErrorResumeNext { throwable: Throwable ->
                Flowable.error(asRetrofitException(throwable))
            }
            is Maybe<*> -> adaptedCall.onErrorResumeNext { throwable: Throwable ->
                Maybe.error(asRetrofitException(throwable))
            }
            else -> throw RuntimeException("Observable Type not supported")
        }
    }

    internal fun asRetrofitException(throwable: Throwable): Throwable {
        return when (throwable) {
            is HttpException -> Throwable(genericError)
            is IOException -> Throwable(networkError)
            else -> throwable
        }
    }
}

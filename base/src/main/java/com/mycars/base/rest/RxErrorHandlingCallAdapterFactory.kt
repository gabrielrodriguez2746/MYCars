package com.mycars.base.rest

import com.mycars.base.helpers.asCallAdapterOfType
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory(
    private val genericError: String,
    private val networkError: String,
    private val factory: CallAdapter.Factory
) : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        return factory.get(returnType, annotations, retrofit)?.asCallAdapterOfType<CallAdapter<Any, Any>>()
            ?.let {
                RxCallAdapterWrapper(genericError, networkError, it)
            }
    }
}

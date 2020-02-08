package com.prateek.myapplicationshekharkhandeparkar.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/*
* NullOnEmptyConverterFactory class
* for handling null values
* */
class NullOnEmptyConverterFactory private constructor() : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation>?,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val delegate = retrofit.nextResponseBodyConverter<Any>(this, type!!, annotations!!)
        return Converter<ResponseBody, Any> { body ->
            if (body.contentLength() == 0L) {
                null
            } else delegate.convert(body)
        }
    }

    companion object {

        fun create(): Converter.Factory {
            return NullOnEmptyConverterFactory()
        }
    }
}
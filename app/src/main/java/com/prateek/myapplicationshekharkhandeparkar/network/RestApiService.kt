package com.prateek.myapplicationshekharkhandeparkar.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.prateek.myapplicationshekharkhandeparkar.model.Country
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

/*
* RestApiService class
* retrofit interface for creating server connection
* */
interface RestApiService {

    @GET(countriesEndPoint) // get method for the api without any params
    fun fetchAsync(): Deferred<List<Country>>

    companion object {

        const val baseURL: String = "https://restcountries.eu/"    // base url of the api
        const val countriesEndPoint: String = "rest/v2/all"    // end point for the api call

        fun createService(mContext: Context): RestApiService {              // create instance of retrofit service
            val okHttpClient = OkHttpClient().newBuilder()      // okhttpclient for connection
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(NetworkConnectionInterceptor(mContext))
                .build()

            return Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                .addConverterFactory(NullOnEmptyConverterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(RestApiService::class.java)
        }
    }
}
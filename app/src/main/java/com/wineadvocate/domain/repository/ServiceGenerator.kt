package com.wineadvocate.repository

import android.util.Log
import com.google.gson.GsonBuilder
import com.wineadvocate.bradixapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *  Created by Christian on Thursday Mar, 2019
 */

object ServiceGenerator {

    private var apiHttpClient: OkHttpClient.Builder? = null
    private var apiRetrofit: Retrofit? = null

    // new implementation
    private val gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .create()

    private val apiBuilder = Retrofit.Builder()
        .baseUrl(BuildConfig.AUTH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())


    fun <S> createAPIService(serviceClass: Class<S>): S {

        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d("OkHttp", message) })

        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        // here I checked if Okhttpclient is null, create new. otherwise, use existing.
        if (apiHttpClient == null) {
            apiHttpClient = OkHttpClient.Builder()
        }

        apiHttpClient!!
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)


        // here I checked if retrofit is null, create new. otherwise, use existing.
        val client = apiHttpClient!!.build()
        if (apiRetrofit == null) {
            apiRetrofit = apiBuilder.client(client).build()
        }

        return apiRetrofit!!.create(serviceClass)
    }

}
package com.daou.api


import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

object RequestToServer {

    private const val BASE_URL = "https://nstaging.daouoffice.com/"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }
}
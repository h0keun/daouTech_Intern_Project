package com.daou

import com.daou.api.RequestInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 왜 object로 했는지 알고 넘어가기
object RequestToServer {
    private const val BASE_URL = "https://nstaging.daouoffice.com/"

    private var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: RequestInterface = retrofit.create(
        RequestInterface::class.java
    )
}
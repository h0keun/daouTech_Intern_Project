package com.daou.api

import com.daou.data.LoginRequestData
import com.daou.data.LoginResponseData
import com.daou.data.LoginResponseResult
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @POST("/api/login")
    suspend fun requestLogin(@Body body : LoginRequestData): Response<LoginResponseData>

    @GET("/api/alive")
    suspend fun requestLoginResult(): Response<LoginResponseResult>
}

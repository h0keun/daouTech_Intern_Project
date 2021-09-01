package com.daou.api

import com.daou.data.remote.LoginRequest
import com.daou.data.remote.LoginResponse
import com.daou.data.remote.SessionResponse
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @POST("/api/login")
    suspend fun requestLogin(@Body body : LoginRequest): Response<LoginResponse>

    @GET("/api/alive")
    suspend fun getSessionAlive(): Response<SessionResponse>
}

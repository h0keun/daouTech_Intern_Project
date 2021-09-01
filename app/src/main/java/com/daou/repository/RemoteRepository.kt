package com.daou.repository

import com.daou.api.RequestToServer
import com.daou.data.remote.LoginRequest
import com.daou.data.remote.LoginResponse
import com.daou.data.remote.SessionResponse
import retrofit2.Response
import retrofit2.http.Body

class RemoteRepository {

    suspend fun requestLogin(@Body body : LoginRequest): Response<LoginResponse> {
        return RequestToServer.service.requestLogin(body)
    }

    suspend fun requestSessionAlive(): Response<SessionResponse>{
        return RequestToServer.service.getSessionAlive()
    }

}
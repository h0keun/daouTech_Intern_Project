package com.daou.repository

import com.daou.api.RequestToServer
import com.daou.data.LoginRequestData
import com.daou.data.LoginResponseData
import com.daou.data.LoginResponseResult
import retrofit2.Response
import retrofit2.http.Body

class Repository {

    suspend fun requestLogin(@Body body : LoginRequestData): Response<LoginResponseData> {
        return RequestToServer.service.requestLogin(body)
    }

    suspend fun requestLoginResult(): Response<LoginResponseResult>{
        return RequestToServer.service.requestLoginResult()
    }

}
package com.daou.api

import com.daou.model.RequestLogin
import com.daou.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {

    @POST("/api/login")
    fun requestLogin(@Body body: RequestLogin) : Call<ResponseLogin>
}

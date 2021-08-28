package com.daou.viewmodel

import androidx.lifecycle.*
import com.daou.data.LoginRequestData
import com.daou.data.LoginResponseData
import com.daou.data.LoginResponseResult
import com.daou.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Body

class LoginViewModel(private val repository: Repository) : ViewModel() {

    val putLoginResponse: SingleLiveEvent<Response<LoginResponseData>> by lazy {
        SingleLiveEvent()
    }

    val getLoginResponse: SingleLiveEvent<Response<LoginResponseResult>> by lazy {
        SingleLiveEvent()
    }

    fun requestLogin(@Body body: LoginRequestData) {
        viewModelScope.launch {
            val response = repository.requestLogin(body)
            putLoginResponse.value = response
        }
    }

    fun requestLoginResult() {
        viewModelScope.launch {
            val responseResult = repository.requestLoginResult()
            getLoginResponse.value = responseResult
        }
    }
}






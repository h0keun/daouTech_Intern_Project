package com.daou.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.daou.data.remote.LoginRequest
import com.daou.repository.RemoteRepository
import kotlinx.coroutines.launch
import retrofit2.http.Body
import androidx.lifecycle.MutableLiveData

class LoginViewModel(private val repository: RemoteRepository) : ViewModel() {
    val TAG = "LoginViewModel"
    val idInput = SingleLiveEvent<String>()
    val passwordInput = SingleLiveEvent<String>()

    val emptyLoginData = SingleLiveEvent<Any>()
    val failedLogin = SingleLiveEvent<Any>()
    val errorMessage = SingleLiveEvent<Any>()
    val successLogin = SingleLiveEvent<Any>()
    var progress = MutableLiveData<Int>()

    init {
        progress.value = 8
    }

    fun clickButton(id: String?, password: String?) {
        if (id.isNullOrBlank() || password.isNullOrBlank()) {
            progress.value = 8
            emptyLoginData.call()
        } else {
            progress.value = 0
            requestLogin(
                LoginRequest(
                    username = id.toString(),
                    password = password.toString()
                )
            )
        }
    }

    private fun requestLogin(@Body body: LoginRequest) {
        viewModelScope.launch {
            try {
                if (repository.requestLogin(body).body()?.code.toString() == "200" &&
                    repository.requestLogin(body).body()?.message.toString() == "OK" &&
                    repository.requestLogin(body).body()?.goChecksum.toString() == "true"
                ) {
                    requestLoginResult()
                } else {
                    progress.value = 8
                    failedLogin.call()
                }
            } catch (error: Throwable) {
                progress.value = 8
                errorMessage.call()
            }
        }
    }

    private fun requestLoginResult() {
        viewModelScope.launch {
            try {
                if (repository.requestSessionAlive().body()?.code.toString() == "200" &&
                    repository.requestSessionAlive().body()?.goChecksum.toString() == "true" &&
                    repository.requestSessionAlive().body()?.message.toString() == "OK" &&
                    repository.requestSessionAlive().body()?.name.toString() == "null"
                ) {
                    progress.value = 8
                    successLogin.call()
                }
            } catch (error: Throwable) {
                progress.value = 8
                errorMessage.call()
            }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "## LoginViewModel - onCleared() called!!")
        super.onCleared()
    }
}






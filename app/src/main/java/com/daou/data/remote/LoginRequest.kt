package com.daou.data.remote

data class LoginRequest(
    val username: String,
    val password: String,
    val locale: String="ko",
    val deviceId: String="GalaxyA30"
)
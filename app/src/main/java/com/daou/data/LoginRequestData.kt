package com.daou.data

data class LoginRequestData(
    var username: String,
    var password: String,
    var locale: String="ko",
    var deviceId: String="GalaxyA30"
)
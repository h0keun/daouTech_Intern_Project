package com.daou.model

// api에 호출할때 사용하는 변수들(내가 전달해야하는 겂) [INPUT]
// RequestBody 서버형식에 맞춤
data class RequestLogin(
    var username: String,
    var password: String,
    var locale: String="ko",
    var deviceId: String="GalaxyA30"
)
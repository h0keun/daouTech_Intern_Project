package com.daou.model

import com.google.gson.annotations.SerializedName

// 로그인 요청에 따른 응답형식으로 서버에서 주는 형식과 동일하게 설정해야함 [OUTPUT]
data class ResponseLogin(
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("__go_checksum__")
    val goChecksum: Boolean,
    @SerializedName("message")
    val message: String
)

data class Data(
    @SerializedName("redirect")
    val redirect: String,
    @SerializedName("xmppDomain")
    val xmppDomain: String
)
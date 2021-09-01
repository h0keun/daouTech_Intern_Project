package com.daou.data.remote
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val data: Data,
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
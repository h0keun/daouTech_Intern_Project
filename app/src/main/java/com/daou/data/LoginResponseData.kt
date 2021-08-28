package com.daou.data

import com.google.gson.annotations.SerializedName

data class LoginResponseData(
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
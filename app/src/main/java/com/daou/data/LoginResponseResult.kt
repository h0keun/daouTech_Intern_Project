package com.daou.data
import com.google.gson.annotations.SerializedName

data class LoginResponseResult(
    @SerializedName("code")
    val code: String,
    @SerializedName("__go_checksum__")
    val goChecksum: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: Any
)
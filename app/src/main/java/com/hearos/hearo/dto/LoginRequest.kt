package com.hearos.hearo.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("uid")
    val uid : String,

    @SerializedName("email")
    val email : String,

    @SerializedName("password")
    val password : String?
)

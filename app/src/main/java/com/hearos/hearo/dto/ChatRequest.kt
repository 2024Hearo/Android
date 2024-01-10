package com.hearos.hearo.dto

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName(value = "email")
    val email: String,
)

package com.hearos.hearo.dto

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName(value = "userEmail")
    val email: String,
)

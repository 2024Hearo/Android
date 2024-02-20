package com.hearos.hearo.dto

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "chatRoomName")
    val roomName: String,
    @SerializedName(value = "token")
    val token: String,
)

data class ChatAcceptRequest(
    @SerializedName(value = "roomId")
    val roomId: String,
    @SerializedName(value = "userId")
    val friendUid: String,
    @SerializedName(value = "token")
    val token: String,
)

data class ChatRejectRequest(
    @SerializedName(value = "roomId")
    val roomId: String,
    @SerializedName(value = "userId")
    val friendUid: String,
)

data class ChatRoomRequest(
    @SerializedName(value = "message")
    val message: String?,
)

package com.hearos.hearo.dto

import com.google.gson.annotations.SerializedName


data class ChatListRes(
    @SerializedName("profileImg")
    val profileImg : String,
    @SerializedName(value = "title")
    val title: String,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value = "time")
    val time: String,
)

data class ChatInviteRes(
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("code") val code: Int,
//    @SerializedName("message") val message: String?,
//    @SerializedName("result") val result: String?,
    @SerializedName("message") val result: String?,
)

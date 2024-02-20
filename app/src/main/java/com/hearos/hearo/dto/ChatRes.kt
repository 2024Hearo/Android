package com.hearos.hearo.dto

import com.google.gson.annotations.SerializedName


data class ChatInviteRes(
    @SerializedName("success") val success: Boolean,
    @SerializedName("roomId") val roomId: String?,
)

data class InvitedRes(
    @SerializedName("success") val success: Boolean,
)

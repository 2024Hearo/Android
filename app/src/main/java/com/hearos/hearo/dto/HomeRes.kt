package com.hearos.hearo.dto

import com.google.gson.annotations.SerializedName

data class HomeRes(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("rooms")
    val HomeList: List<HomeList>,
) data class HomeList(
    @SerializedName("accepted")
    val remindType : Boolean,
    @SerializedName("roomId")
    val roomId : String?,
    @SerializedName("inviter")
    val Friend: Friend
    )data class Friend(
    @SerializedName("name")
    val friendName : String?,
    @SerializedName("uid")
    val friendUid : String?,
    )

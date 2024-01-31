package com.hearos.hearo.dto

data class ChatList(
    val chatRoomId :String?,
    val chatProfile : String?,
    val roomName : String?,
    val lastMessage : String,
    val timeStamp: String?
)

data class MessageModel (
    val senderUid : String = "",
    val senderNickName : String = "",
    val contents : String = "",
    val sendTime : String = "",
    var viewType: Int = VIEW_TYPE_ME,
    val imageUrl : String? = "null",
) {
    companion object {
        const val VIEW_TYPE_YOU = 0
        const val VIEW_TYPE_ME = 1
    }
}
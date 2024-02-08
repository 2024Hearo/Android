package com.hearos.hearo.dto

data class ChatRoom(
    var chatRoomId :String? = null,
    val accepted : Boolean? = null,
    val roomName : String? = null,
    val chatProfile : String? = null,
    val lastMessage : String? = null,
    val timeStamp: String? = null,
)

data class MessageModel (
    val senderUid : String = "",
    val senderNickName : String? = "",
    val contents : String = "",
    val sendTime : String = "",
    val imageUrl : String? = "null",
    var viewType: Int = VIEW_TYPE_ME,
) {
    companion object {
        const val VIEW_TYPE_YOU = 0
        const val VIEW_TYPE_ME = 1
    }
}
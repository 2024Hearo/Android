package com.hearos.hearo.dto

data class ChatRoom(
    var ChatRoomID :String? = null,
    val roomname : String? = null,
    val chatProfile : String? = null,
    val lastMessage : String? = null,
    val timeStamp: String? = null,
    //val invitee: List<Invitee>? = null
)
//data class Invitee(
//    val name: String? = null,
//    val uid: String? = null,
//)

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

data class AiChat(
    val lastMessage : String? = null,
    val timeStamp: String? = null,
)
package com.hearos.hearo

data class ChatroomData(
    var name: String?,
    var uid: String?,
    val profileImageUrl : String? = null,
    var content: String?,
    var timestamp: String?
)

package com.hearos.hearo.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {
    companion object {
        val database = Firebase.database
        val userInfo = database.getReference("User")
        val chatRoom = database.getReference("ChatRooms")
    }
}
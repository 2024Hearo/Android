package com.hearos.hearo.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {
    companion object {
        val database = Firebase.database
        val userInfo = database.getReference("userInfo")
        val chatRoom = database.getReference("chatRoom")
        val message = database.getReference("message")
    }
}
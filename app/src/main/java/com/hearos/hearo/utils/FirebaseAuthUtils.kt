package com.hearos.hearo.utils

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class FirebaseAuthUtils {
    companion object {
        private lateinit var auth : FirebaseAuth
        fun getAuth() : FirebaseAuth{
            auth = FirebaseAuth.getInstance()
            return auth
        }
        fun getUid() : String {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }
    }
}
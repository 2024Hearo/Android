package com.hearos.hearo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.hearos.hearo.databinding.ActivitySignBinding
import com.hearos.hearo.dto.MessageModel
import com.hearos.hearo.utils.FirebaseRef
import com.hearos.hearo.utils.HearoApplication
import java.text.SimpleDateFormat

class SignActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val video = binding.flSign
        val VIDEO_PATH = "android.resource://com.hearos.hearo/" + R.raw.hello
        val video_uri = Uri.parse(VIDEO_PATH)
        video.setVideoURI(video_uri)
        video.setMediaController(MediaController(this))
        video.requestFocus()

        video.setOnPreparedListener {
            video.start()
        }
        binding.btnSignSend.setOnClickListener {
            Log.d("BTN", "btn")
            val message = "I headache I cold"
            binding.etSignTranslate.setText(message)
            val sendTime = getSendTime()
            val messageModel = MessageModel(HearoApplication.dataStore.dsUid!!, HearoApplication.dataStore.dsName, message, "")
            Log.d("test", messageModel.toString())
            Log.d("test", intent.getStringExtra("roomId").toString())
            FirebaseRef.chatRoom.child(intent.getStringExtra("roomId")!!).child("Message").push().setValue(messageModel)
            finish()
        }

    }
    private fun getSendTime(): String {
        try {
            val format = SimpleDateFormat("MM-dd HH:mm")
            return format.format(System.currentTimeMillis())
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("시간 정보를 불러오지 못함")
        }
    }

}

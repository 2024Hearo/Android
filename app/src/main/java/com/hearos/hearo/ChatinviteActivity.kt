package com.hearos.hearo


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hearos.hearo.databinding.ActivityChatinviteBinding
import com.hearos.hearo.dto.*
import com.hearos.hearo.utils.FirebaseAuthUtils
import com.hearos.hearo.utils.FirebaseRef
import com.hearos.hearo.utils.RetrofitService

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatinviteActivity: AppCompatActivity() {
    private lateinit var binding: ActivityChatinviteBinding
    private lateinit var nickName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatinviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChatinviteBack.setOnClickListener {
            finish()
        }
        binding.btnInvitechatInvite.setOnClickListener {
            sendEmail()
        }

    }

    private fun sendEmail() {
        val email = binding.etInvitechatEmail.text.toString()
        val roomName = binding.etInvitechatTitle.text.toString()
        val inviteChatReq = ChatRequest(email,roomName)
        CoroutineScope(Dispatchers.IO).launch {
            val response = inviteChat(inviteChatReq)
            Log.d("CHATINVITEACT", response.toString())
            if (response.isSuccess) {
                Log.d("CHATINVITEACT", "초대 성공 + ${response.result}")
                Toast.makeText(this@ChatinviteActivity,"초대 성공",Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.d("CHATINVITEACT", "실패 + ${response.message}")
                Toast.makeText(this@ChatinviteActivity, "초대 실패",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private suspend fun inviteChat(chatRequest : ChatRequest) : BaseResponse<ChatInviteRes> {
        return RetrofitService.chatApi.inviteChat(chatRequest)
    }

}
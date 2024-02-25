package com.hearos.hearo


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hearos.hearo.databinding.ActivityChatinviteBinding
import com.hearos.hearo.dto.*
import com.hearos.hearo.utils.FirebaseAuthUtils
import com.hearos.hearo.utils.RetrofitService

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            finish()
        }
    }

    private fun sendEmail() {
        val email = binding.etInvitechatEmail.text.toString()
        val roomName = binding.etInvitechatTitle.text.toString()
        FirebaseAuthUtils.getAuth().currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
            val token = result.token
            Log.d("TOKEN", token!!)
            val inviteChatReq = ChatRequest(email,roomName,token)
            CoroutineScope(Dispatchers.IO).launch {
                val response = inviteChat(inviteChatReq)
                Log.d("CHATINVITEACT", response.toString())
                if (response.success) {
                    Log.d("CHATINVITEACT", "초대 성공 + ${response}")
                    finish()
                } else {
                    Log.d("CHATINVITEACT", "실패 + ${response}")
                }
            }
        }?.addOnFailureListener { exception ->
            Log.e("TAG", "토큰 받아오기 실패: ${exception.message}")
        }

    }

    private suspend fun inviteChat(chatRequest : ChatRequest) : ChatInviteRes {
        return RetrofitService.chatApi.inviteChat(chatRequest)
    }

}
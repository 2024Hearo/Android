package com.hearos.hearo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hearos.hearo.databinding.ActivityChatinviteBinding
import com.hearos.hearo.dto.BaseResponse
import com.hearos.hearo.dto.ChatInviteRes
import com.hearos.hearo.dto.ChatRequest
import com.hearos.hearo.utils.RetrofitService

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatinviteActivity: AppCompatActivity() {
    private lateinit var binding: ActivityChatinviteBinding

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
        val inviteChatReq = ChatRequest(email)
        CoroutineScope(Dispatchers.IO).launch {
            val response = inviteChat(inviteChatReq)
            Log.d("ChatInviteActivity", response.toString())
            if (response.isSuccess) {
                Log.d("ChatInviteActivity", "이메일 전송 성공 + ${response.result}")
                Toast.makeText(this@ChatinviteActivity,"이메일 전송",Toast.LENGTH_SHORT).show()

            } else {
                Log.d("ChatInviteActivity", "실패 + ${response.message}")
                Toast.makeText(this@ChatinviteActivity, "전송 실패",Toast.LENGTH_SHORT).show()
            }
        }

    }
    private suspend fun inviteChat(chatRequest : ChatRequest) : BaseResponse<ChatInviteRes> {
        return RetrofitService.chatApi.inviteChat(chatRequest)
    }
}
package com.hearos.hearo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hearos.hearo.databinding.ActivityChatroomBinding
import com.hearos.hearo.dto.BaseResponse
import com.hearos.hearo.dto.ChatInviteRes
import com.hearos.hearo.dto.ChatRequest

import com.hearos.hearo.dto.MessageModel
import com.hearos.hearo.utils.FirebaseAuthUtils
import com.hearos.hearo.utils.FirebaseRef
import com.hearos.hearo.utils.HearoApplication
import com.hearos.hearo.utils.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class AiChatFragment : Fragment() {
    private lateinit var binding: ActivityChatroomBinding
    val messageList = mutableListOf<MessageModel>()
    lateinit var chatroomAdapter : ChatroomAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityChatroomBinding.inflate(inflater, container, false)

//        binding.btnChatroomSend.setOnClickListener {
//            sendMessage()
//        }
        chatroomAdapter = ChatroomAdapter(requireContext(), messageList)
        binding.rvChatroom.layoutManager = LinearLayoutManager(context)
        binding.rvChatroom.adapter = chatroomAdapter
        getMessageList()

        binding.btnChatroomSend.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val generativeModel = GenerativeModel(
                    modelName = "gemini-pro",
                    apiKey = BuildConfig.API_KEY
                )
                val prompt = binding.etChatroomInput.text.toString()
                val sendTime = getSendTime()
                val nickName = HearoApplication.dataStore.dsName
                val messageModel = MessageModel(HearoApplication.dataStore.dsUid!!, nickName, prompt, sendTime)
                FirebaseRef.userInfo.child(HearoApplication.dataStore.dsUid!!).child("GEMINI").push().setValue(messageModel)
                binding.etChatroomInput.setText(null)

                val response = generativeModel.generateContent(prompt).text
                val aiMessageModel = MessageModel("ai", "Hearobot", response!!, getSendTime())
                FirebaseRef.userInfo.child(HearoApplication.dataStore.dsUid!!).child("GEMINI").push().setValue(aiMessageModel)
                Log.d("AI", prompt + response)
            }
        }

        return binding.root
    }


    private fun getMessageList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageList.clear()
                val newMessages = mutableListOf<MessageModel>()
                for (datamModel in dataSnapshot.children) {
                    val messageModel = datamModel.getValue(MessageModel::class.java)
                    if(messageModel!!.senderUid != HearoApplication.dataStore.dsUid!!) {
                        messageModel.viewType = MessageModel.VIEW_TYPE_YOU
                    }
                    newMessages.add(messageModel!!)
                }
                messageList.addAll(newMessages)
                chatroomAdapter.notifyItemInserted(messageList.size)
                Log.d("AICHAT", messageList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("AICHAT", "onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfo.child(HearoApplication.dataStore.dsUid!!).child("GEMINI").addValueEventListener(postListener)
    }

//    private suspend fun sendMessage(generativeModel: GenerativeModel) {
//        val prompt = binding.etChatroomInput.text.toString()
//        val response = generativeModel.generateContent(prompt)
//        val sendTime = getSendTime()
//        val messageModel = MessageModel(HearoApplication.dataStore.dsUid!!, "Hearobot", prompt, sendTime)
//        FirebaseRef.userInfo.child("AiChat").push().setValue(messageModel)
//
//        binding.etChatroomInput.setText(null)
//    }

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
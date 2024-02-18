package com.hearos.hearo

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hearos.hearo.databinding.ActivityChatroomBinding
import com.hearos.hearo.databinding.ItemMessageBinding
import com.hearos.hearo.dto.MessageModel
import com.hearos.hearo.utils.FirebaseAuthUtils
import com.hearos.hearo.utils.FirebaseRef
import com.hearos.hearo.utils.HearoApplication
import java.text.SimpleDateFormat

class ChatroomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatroomBinding
    private lateinit var binding2: ItemMessageBinding
    val messageList = mutableListOf<MessageModel>()
    lateinit var chatroomAdapter : ChatroomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvChatroomTitle.text = intent.getStringExtra("roomName")
        val chatRoomId = intent.getStringExtra("roomId")


        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding.btnChatroomBack.setOnClickListener {
            setLastMessage(chatRoomId!!)
            finish()
        }
        binding.btnChatroomSend.setOnClickListener {
            sendMessage(chatRoomId!!)
        }
        binding.btnChatroomCamera.setOnClickListener {

        }

        chatroomAdapter = ChatroomAdapter(this, messageList)
        binding.rvChatroom.layoutManager = LinearLayoutManager(this)
        binding.rvChatroom.adapter = chatroomAdapter
        getMessageList(chatRoomId!!)


    }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setLastMessage(intent.getStringExtra("roomId")!!)
            finish()
        }
    }
    private fun setLastMessage(chatRoomId : String) {
        val last = messageList.last()
        val lastContentMap = mutableMapOf<String,Any>()
        lastContentMap["lastMessage"] = last.contents
        lastContentMap["timeStamp"] = last.sendTime
        FirebaseRef.userInfo.child(HearoApplication.dataStore.dsUid!!).child("chat").child(chatRoomId).updateChildren(lastContentMap)
    }

    private fun getMessageList(roomId: String) {
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
                Log.d("CHATROOM", messageList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("CHATROOM", "onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.chatRoom.child(roomId).child("Message").addValueEventListener(postListener)
        FirebaseRef.chatRoom.child(roomId).child("Message").addValueEventListener(postListener)
    }

    private fun sendMessage(roomId : String) {
        val contents = binding.etChatroomInput.text.toString()
        val sendTime = getSendTime()
        val messageModel = MessageModel(HearoApplication.dataStore.dsUid!!, HearoApplication.dataStore.dsName, contents, sendTime)
        FirebaseRef.chatRoom.child(roomId!!).child("Message").push().setValue(messageModel)

        binding.etChatroomInput.setText(null)
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
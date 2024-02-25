package com.hearos.hearo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hearos.hearo.databinding.FragmentChatBinding
import com.hearos.hearo.dto.ChatList
import com.hearos.hearo.utils.FirebaseAuthUtils
import com.hearos.hearo.utils.FirebaseRef

class ChatFragment : Fragment() {
    private lateinit var binding : FragmentChatBinding
    private var chatRoomList = mutableListOf<ChatList>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

//        binding.btnChatCreate.setOnClickListener {
//            startActivity(Intent(context, ChatinviteActivity::class.java))
//        }
//
//        initList()
       // binding.btnChatCreate.setOnClickListener {
            //val PATH = "android.resource://com.hearos.hearo/" + R.raw.sound1
            //val url = Uri.parse(PATH)

        //}

        return binding.root
    }



    private fun initList() {
        val chatAdapter : ChatAdapter = ChatAdapter(requireContext(), chatRoomList)
        binding.rvChat.layoutManager = LinearLayoutManager(context)
        binding.rvChat.adapter = chatAdapter

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatRoomList.clear()
                for (datamModel in dataSnapshot.children) {
                    val chatRoom = datamModel.getValue(ChatList::class.java)
                    chatRoomList.add(chatRoom!!)
                }
                chatAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databseError: DatabaseError) {
                Log.w("CHAT", "onCancelled", databseError.toException())
            }
        }
        FirebaseRef.chatRoom.child(FirebaseAuthUtils.getUid()).addValueEventListener(postListener)
    }


//    private fun getAccessToken(callback: (String) -> Unit) {
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val data = dataSnapshot.getValue(com.chrome.chattingapp.authentication.UserInfo::class.java)
//                val accessToken = data?.accessToken ?: ""
//                callback(accessToken)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.w("ChatListFragment", "onCancelled", databaseError.toException())
//            }
//        }
//
//        FirebaseRef.userInfo.child(FirebaseAuthUtils.getUid()).addListenerForSingleValueEvent(postListener)
//    }


}
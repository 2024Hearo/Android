package com.hearos.hearo

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.hearos.hearo.databinding.ItemMessageBinding
import com.hearos.hearo.databinding.ItemUsermessageBinding
import com.hearos.hearo.dto.*
import com.hearos.hearo.utils.RetrofitService

class ChatroomAdapter(val context: Context, val messageList: MutableList<MessageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            MessageModel.VIEW_TYPE_ME -> {
                val bindinguser: ItemUsermessageBinding = ItemUsermessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SendViewHolder(bindinguser)
            }
            MessageModel.VIEW_TYPE_YOU -> {
                val binding: ItemMessageBinding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ReceiveViewHolder(binding)
            }
            else -> throw RuntimeException("Invalid View Type Error")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when(currentMessage.viewType){
            MessageModel.VIEW_TYPE_ME -> {
                (holder as SendViewHolder).sendMessage.text = currentMessage.contents
                holder.sendTime.text = currentMessage.sendTime
//                if (currentMessage.imageUrl != "null") {
//                    Glide.with(context)
//                        .load(currentMessage.imageUrl)
//                        .into(holder.sendImg)
//                } else {
//                    //holder.sendImg.setImageResource(R.drawable.shape_roundbox_20)
//                }
            }
            MessageModel.VIEW_TYPE_YOU -> {
                (holder as ReceiveViewHolder).receiveMessage.text = currentMessage.contents
                holder.receiveName.text = currentMessage.senderNickName
                holder.receiveTime.text = currentMessage.sendTime
                holder.tranform.setOnClickListener {
                    val video = holder.recieveImage
                    val VIDEO_PATH = "android.resource://com.hearos.hearo/" + R.raw.hello
                    val video_uri = Uri.parse(VIDEO_PATH)
                    video.setVideoURI(video_uri)
                    video.setMediaController(MediaController(context))     // 없으면 에러
                    video.requestFocus()    // 준비하는 과정을 미리함

                    video.setOnPreparedListener {

                        video.start()       // 동영상 재개
                    }

                    holder.recieveImage.visibility = View.VISIBLE
//                    val chatRoomRequest = ChatRoomRequest(holder.receiveMessage.toString())
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val response = getSign(chatRoomRequest)
//                        if (response.isSuccess) {
//                            Log.d("TEXTTOSIGN", response.toString())
//                            val video = holder.recieveImage
//                            val VIDEO_PATH = "android.resource://com.hearos.hearo/" + R.raw.hello
//                            val video_uri = Uri.parse(VIDEO_PATH)
//                            video.setVideoURI(video_uri)
//                            video.setMediaController(MediaController(context))     // 없으면 에러
//                            video.requestFocus()    // 준비하는 과정을 미리함
//
//                            video.setOnPreparedListener {
//
//                                video.start()       // 동영상 재개
//                            }
//
//                            holder.recieveImage.visibility = View.VISIBLE
//                        } else {
//                            Log.d("TEXTTOSIGN", "실패 + ${response.message}")
//                        }
//                    }
                }
//                if (currentMessage.imageUrl != "null") {
//                    Glide.with(context)
//                        .load(currentMessage.imageUrl)
//                        .into(holder.recieveImage)
//                } else {
//                    //holder.recieveImage.setImageResource(R.drawable.shape_roundbox_20)
//                }
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return  messageList[position].viewType
    }

    class SendViewHolder(val binding: ItemUsermessageBinding): RecyclerView.ViewHolder(binding.root){
        val sendMessage: TextView = binding.tvUsermessageMessage
        val sendTime = binding.tvUsermessageTime
        //val sendImg = binding.ivUsermessageHandsign
    }

    class ReceiveViewHolder(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root){
        val receiveMessage: TextView = binding.tvMessageMessage
        val receiveName : TextView = binding.tvMessageName
        val receiveTime = binding.tvMessageTime
        val tranform : AppCompatButton = binding.btnMessageTransform
        val recieveImage = binding.ivMessageHandsign
    }

    private suspend fun getSign(chatRoomRequest: ChatRoomRequest) : BaseResponse<String> {
        return RetrofitService.chatApi.getSign(chatRoomRequest)
    }
}
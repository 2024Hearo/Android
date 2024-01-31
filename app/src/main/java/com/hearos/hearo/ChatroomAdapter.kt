package com.hearos.hearo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.hearos.hearo.databinding.ItemMessageBinding
import com.hearos.hearo.databinding.ItemUsermessageBinding
import com.hearos.hearo.dto.Message
import com.hearos.hearo.dto.MessageModel

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
            }
            MessageModel.VIEW_TYPE_YOU -> {
                (holder as ReceiveViewHolder).receiveMessage.text = currentMessage.contents
                holder.receiveName.text = currentMessage.senderNickName
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
    }

    class ReceiveViewHolder(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root){
        val receiveMessage: TextView = binding.tvMessageMessage
        val receiveName : TextView = binding.tvMessageName
    }
}
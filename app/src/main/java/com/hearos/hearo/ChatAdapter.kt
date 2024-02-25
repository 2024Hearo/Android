package com.hearos.hearo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hearos.hearo.databinding.ItemChatlistBinding
import com.hearos.hearo.dto.ChatRoom

class ChatAdapter(val context: Context, val dataList : List<ChatRoom>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding: ItemChatlistBinding = ItemChatlistBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = dataList[position]
        val imgUrl = dataList[position].chatProfile
        holder.roomName!!.text = currentUser.roomname
        holder.content!!.text = currentUser.lastMessage
        holder.time!!.text = currentUser.timeStamp
        if(imgUrl != null) {
            val imgUri = Uri.parse(imgUrl)
            Glide.with(context)
                .load(imgUri)
                .into(holder.profile!!)
        } else {
            holder.profile!!.setImageResource(R.drawable.shape_circle_profile)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatroomActivity::class.java)
            intent.putExtra("roomId", currentUser.ChatRoomID)
            intent.putExtra("roomName", currentUser.roomname)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val binding: ItemChatlistBinding): RecyclerView.ViewHolder(binding.root) {
        val roomName : TextView = binding.tvChatlistTitle
        val content : TextView = binding.tvChatlistContent
        val time : TextView = binding.tvChatlistTime
        val profile : ImageView = binding.ivChatlistProfile
    }
}
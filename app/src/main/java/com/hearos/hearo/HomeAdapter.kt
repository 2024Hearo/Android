package com.hearos.hearo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.hearos.hearo.databinding.ItemInvitelistBinding
import com.hearos.hearo.dto.HomeList
import com.hearos.hearo.dto.HomeRes

class HomeAdapter(val context: Context, val remindList : List<HomeList>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun onItemClick(v:View, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemInvitelistBinding = ItemInvitelistBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(remindList[position])

    }

    override fun getItemCount(): Int {
        return remindList.size
    }

    inner class ViewHolder(val binding: ItemInvitelistBinding): RecyclerView.ViewHolder(binding.root) {
        val title : TextView = binding.tvIteminviteTitle
        val content : TextView = binding.tvIteminviteContent
        val typeImg : ImageView = binding.ivIteminviteProfile
        val btnAccept : AppCompatButton = binding.btnIteminviteAccept
        val btnReject : AppCompatButton = binding.btnIteminviteReject

        fun bind(currentRemind: HomeList) {
            if(currentRemind.remindType){
                title!!.text = "NEW 채팅방"
                content.text = currentRemind.Friend.friendName + " 님과의 채팅이 가능해요."
                typeImg!!.setImageResource(R.drawable.ic_star_1)
            } else {
                title!!.text = "채팅방 초대 요청"
                content.text = currentRemind.Friend.friendName + " 님으로부터 채팅 요청이 왔어요."
                btnAccept.visibility = View.VISIBLE
                btnReject.visibility = View.VISIBLE
            }
            val pos = adapterPosition
                if(pos!= RecyclerView.NO_POSITION)
                {
                    itemView.setOnClickListener {
                        listener?.onItemClick(itemView,pos)
                    }
                }
        }

    }
}
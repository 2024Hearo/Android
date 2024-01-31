package com.hearos.hearo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hearos.hearo.databinding.FragmentChatBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

//        binding.btnHomeRemind.setOnClickListener {
//            startActivity(Intent(context, ChatinviteActivity::class.java))
//        }

        return binding.root
    }
}
package com.hearos.hearo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hearos.hearo.databinding.FragmentHomeBinding
import com.hearos.hearo.databinding.ItemInvitelistBinding
import com.hearos.hearo.dto.*
import com.hearos.hearo.utils.FirebaseAuthUtils
import com.hearos.hearo.utils.HearoApplication
import com.hearos.hearo.utils.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var bindingItem : ItemInvitelistBinding
    private var homeList = mutableListOf<HomeList>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.tvHomeContent.text = "Hi, "+HearoApplication.dataStore.dsName+"\nHow was your day today?"

        binding.btnHomeNewchat.setOnClickListener {
            startActivity(Intent(context, ChatinviteActivity::class.java))
        }
        binding.btnHomeAichat.setOnClickListener {
            changeFragment()
        }

        binding.btnHomeRemind.setOnClickListener {
            HearoApplication.dataStore.dsUid = ""
            HearoApplication.dataStore.dsName =""
            Log.d("HOMEFRA", "reset")
            startActivity(Intent(context, LoginActivity::class.java))
        }
        //initList()
        return binding.root
    }
    private fun changeFragment() {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_main, AiChatFragment())?.commitAllowingStateLoss()
    }


    private fun setList(result : List<HomeList>) {
        val homeAdapter: HomeAdapter = HomeAdapter(requireContext(), result)
        binding.rvHomeRemind.layoutManager = LinearLayoutManager(context)
        binding.rvHomeRemind.adapter = homeAdapter

        homeAdapter.setOnItemClickListener(object :
        HomeAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                bindingItem.btnIteminviteAccept.setOnClickListener {
                    FirebaseAuthUtils.getAuth().currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
                        val token = result.token
                        Log.d("TOKEN", token!!)
                        CoroutineScope(Dispatchers.IO).launch {
                            val acceptReq = ChatAcceptRequest(homeList[pos].roomId!!, homeList[pos].Friend.friendUid!!,token)
                            acceptInvite(acceptReq)
                        }
                    }?.addOnFailureListener { exception ->
                        Log.e("TAG", "토큰 받아오기 실패: ${exception.message}")
                    }


                }
                bindingItem.btnIteminviteReject.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val rejectReq = ChatRejectRequest(homeList[pos].roomId!!, homeList[pos].Friend.friendUid!!)
                        rejectInvite(rejectReq)
                    }
                }
            }
        }
        )
    }

    private fun initList(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRemindList(HearoApplication.dataStore.dsName!!, HearoApplication.dataStore.dsUid!!)
            if (response.success) {
                Log.d("HOMEFRA", response.HomeList.toString())
                setList(response.HomeList!!)
            } else {
            }
        }
    }

    private suspend fun acceptInvite(dataReq : ChatAcceptRequest) : InvitedRes {
        return RetrofitService.chatApi.acceptInvite(dataReq)
    }
    private suspend fun rejectInvite(dataReq: ChatRejectRequest) : InvitedRes {
        return RetrofitService.chatApi.rejectInvite(dataReq)
    }
    private suspend fun getRemindList(name: String, uid: String) : HomeRes {
        return RetrofitService.chatApi.getRemind(name, uid)
    }
}
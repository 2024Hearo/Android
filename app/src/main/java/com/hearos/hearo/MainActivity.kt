package com.hearos.hearo

import MypageFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hearos.hearo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        startActivity(Intent(this, ChatinviteActivity::class.java))

        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main, HomeFragment()).commitAllowingStateLoss()

        binding.bnvMain.itemIconTintList = null

        binding.bnvMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnvitem_home -> {
                    HomeFragment().changeFragment()
                }
                R.id.bnvitem_chat -> {
                    ChatFragment().changeFragment()
                }
                R.id.bnvitem_mypage -> {
                    MypageFragment().changeFragment()
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun Fragment.changeFragment() {
        manager.beginTransaction().replace(R.id.frame_main, this).commitAllowingStateLoss()
    }
}

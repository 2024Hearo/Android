package com.hearos.hearo

import MypageFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MypageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        // MypageFragment 인스턴스 생성
        val mypageFragment = MypageFragment()

        // FragmentManager를 사용하여 Fragment를 Activity 레이아웃에 추가
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, mypageFragment)
            .commit()
    }
}

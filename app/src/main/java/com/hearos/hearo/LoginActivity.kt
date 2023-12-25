package com.hearos.hearo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 상단바 숨기기
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)
    }
}

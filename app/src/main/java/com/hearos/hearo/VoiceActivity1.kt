package com.hearos.hearo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class VoiceActivity1 :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice1)

    val ContinuButton: Button = findViewById(R.id.btn_continue)
        ContinuButton.setOnClickListener{
            val intent = Intent (this,VoiceActivity2::class.java)
            startActivity(intent)
        }
    }
}
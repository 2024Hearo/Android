package com.hearos.hearo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class VoiceActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice2)

        val ContinueButton: Button = findViewById(R.id.btn_continue2)
        ContinueButton.setOnClickListener{
            val intent = Intent (this,VoiceActivity3::class.java)
            startActivity(intent)
        }
    }
}
package com.hearos.hearo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val backButton = findViewById<ImageView>(R.id.ic_back)
        backButton.setOnClickListener {
            // Handle the back button click event
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Optionally, you can finish the current activity if needed
        }

    }
}
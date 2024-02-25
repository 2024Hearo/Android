package com.hearos.hearo

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import android.Manifest
import android.net.Uri
import android.widget.Toast
import com.hearos.hearo.api.MyVoiceInterface
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class VoiceActivity1 : AppCompatActivity() {

    private var isRecording = false
    private lateinit var recordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice1)

        val continueButton: Button = findViewById(R.id.btn_continue)
        continueButton.setOnClickListener {
            val intent = Intent(this@VoiceActivity1, VoiceActivity2::class.java)
            startActivity(intent)
        }

        recordButton = findViewById(R.id.btn_record)
        recordButton.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            }

            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }
    }

    private fun startRecording() {
        // Simulate start recording logic without actual MediaRecorder
        isRecording = true
        recordButton.text = "Stop Recording"
        // Here you would normally initialize and start the MediaRecorder
        Log.d("VoiceActivity1", "Simulated startRecording()")
    }

    private fun stopRecording() {
        // Simulate stop recording logic
        isRecording = false
        recordButton.text = "Start Recording"
        // Here you would normally stop and release the MediaRecorder
        Log.d("VoiceActivity1", "Simulated stopRecording()")
    }

    // Retrofit 인스턴스 생성
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://34.168.33.254:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun uploadFileToServer(fileUri: Uri) {
        val apiService = retrofit.create(/* service = */ MyVoiceInterface::class.java)

        val file = File(fileUri.path)
        val requestBody = RequestBody.create(
            "app/src/main/res/raw/problem.mp3".toMediaTypeOrNull(),
            file
        )
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestBody)

        apiService.uploadVoice(multipartBody).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@VoiceActivity1, "Upload successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@VoiceActivity1, "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@VoiceActivity1, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



package com.hearos.hearo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hearos.hearo.api.RetrofitClient
import com.hearos.hearo.api.VoiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoiceActivity3 : AppCompatActivity() {

    private val TAG = "VoiceActivity3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice3)

        val imageView: ImageView = findViewById(R.id.ImageView)
        val btnShow: Button = findViewById(R.id.btn_show)

        // ProgressBar 인스턴스 찾기 및 초기 설정
        val progressBar1: ProgressBar = findViewById(R.id.progressBar1)
        progressBar1.max = 5
        progressBar1.progress = 0

        val progressBar2: ProgressBar = findViewById(R.id.progressBar2)
        progressBar2.max = 5
        progressBar2.progress = 0

        val progressBar3: ProgressBar = findViewById(R.id.progressBar3)
        progressBar3.max = 5
        progressBar3.progress = 0

        val progressBar4: ProgressBar = findViewById(R.id.progressBar4)
        progressBar4.max = 5
        progressBar4.progress = 0

        btnShow.setOnClickListener {
            // "생성중.." 토스트 메시지 띄우기
            Toast.makeText(this@VoiceActivity3, "생성중..", Toast.LENGTH_SHORT).show()

            // Handler를 사용하여 2초 후에 작업 수행
            Handler(Looper.getMainLooper()).postDelayed({
                // 이미지 리소스 변경
                imageView.setImageResource(R.drawable.cat)
                imageView.invalidate()

                // 임시 데이터를 사용하여 ProgressBar 업데이트
                val results = listOf(1, 0, 1, 0)
                val progressBars = listOf(progressBar1, progressBar2, progressBar3, progressBar4)
                progressBars.forEachIndexed { index, progressBar ->
                    val value = results.getOrNull(index) ?: 0
                    when (value) {
                        1 -> progressBar.progress = Math.min(progressBar.max, progressBar.progress + 5)
                        0 -> progressBar.progress = Math.min(progressBar.max, progressBar.progress + 2)
                    }
                }

                // 작업 완료 후 성공 메시지 표시
                Toast.makeText(this@VoiceActivity3, "ProgressBar 업데이트 성공!", Toast.LENGTH_SHORT).show()
            }, 2000) // 2초 지연
        }
    }

    private fun runVoiceScript() {
        RetrofitClient.instance.runVoiceScript().enqueue(object : Callback<VoiceResponse> {
            override fun onResponse(call: Call<VoiceResponse>, response: Response<VoiceResponse>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "Response successful: ${response.body()?.message}")
                    Toast.makeText(this@VoiceActivity3, response.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Log.e(TAG, "Response error: ${response.message()}")
                    Toast.makeText(this@VoiceActivity3 , "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<VoiceResponse>, t: Throwable) {
                Log.e(TAG, "Network call failed: ${t.message}", t)
                Toast.makeText(this@VoiceActivity3 , "Failure: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}

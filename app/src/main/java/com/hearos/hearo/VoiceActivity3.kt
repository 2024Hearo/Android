package com.hearos.hearo

import android.app.ProgressDialog
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
import com.google.firebase.storage.FirebaseStorage
import com.hearos.hearo.api.RetrofitClient
import com.hearos.hearo.api.VoiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class VoiceActivity3 : AppCompatActivity() {

    private val TAG = "VoiceActivity3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice3)

        val imageView: ImageView = findViewById(R.id.ImageView)
        val btnShow: Button = findViewById(R.id.btn_show)

        val continueButton: Button = findViewById(R.id.btn_mypage)
        continueButton.setOnClickListener {
            val intent = Intent(this@VoiceActivity3, MainActivity::class.java)
            startActivity(intent)
        }

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
            // 로딩 다이얼로그 생성 및 표시
            val loadingDialog = ProgressDialog(this@VoiceActivity3).apply {
                setMessage("Generating.. it may take time.")
                setCancelable(false) // 다이얼로그 바깥 영역 터치해도 닫히지 않게 설정
                show()
            }

            // Firebase Storage 인스턴스 및 참조 생성
            val storageRef = FirebaseStorage.getInstance().reference.child("string/analysis_results.txt")

            // 임시 파일 생성
            val tempFile = File.createTempFile("analysis_results", ".txt", getExternalFilesDir(null))

            // Firebase Storage에서 파일 다운로드
            storageRef.getFile(tempFile).addOnSuccessListener {
                // 파일 다운로드 성공, 임시 파일에서 텍스트 읽기
                val resultsText = tempFile.readText()
                // 파일 내용을 줄바꿈으로 분할하여 리스트로 변환
                val results = resultsText.split("\n").map { it.trim().toInt() }

                val progressBars = listOf(progressBar1, progressBar2, progressBar3, progressBar4)
                progressBars.forEachIndexed { index, progressBar ->
                    // 파일에서 읽은 값을 기반으로 ProgressBar 업데이트
                    val value = results.getOrNull(index) ?: 0
                    progressBar.progress = when (value) {
                        1 -> Math.min(progressBar.max, progressBar.progress + 5) // 값이 1인 경우
                        else -> Math.min(progressBar.max, progressBar.progress + 2) // 그 외의 경우, 여기서는 0으로 가정
                    }
                }

                // 로딩 다이얼로그 닫기
                loadingDialog.dismiss()

                // 작업 완료 후 성공 메시지 표시
                Toast.makeText(this@VoiceActivity3, "ProgressBar 업데이트 성공!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                // 파일 다운로드 실패, 로딩 다이얼로그 닫기
                loadingDialog.dismiss()

                // 실패 메시지 표시
                Toast.makeText(this@VoiceActivity3, "파일 다운로드 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
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

package com.hearos.hearo

import MypageFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var progressBar: ProgressBar
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.progressBar)

        // 회원가입하기 TextView 참조 및 클릭 리스너 설정
        val registerTextView = findViewById<TextView>(R.id.txt_login_register)
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 로그인 세션 초기화
        FirebaseAuth.getInstance().signOut()

        // GoogleSignInOptions 구성
        val defaultWebClientId = resources.getString(
            resources.getIdentifier("default_web_client_id", "string", packageName)
        )

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(defaultWebClientId)
            .requestEmail()
            .build()

        // GoogleSignInClient 초기화
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Google 로그인 버튼 참조 및 클릭 리스너 설정
        findViewById<Button>(R.id.btn_login_google).setOnClickListener { signIn() }
    }

    private fun signIn() {
        progressBar.visibility = View.VISIBLE  // 로그인 시작 시 ProgressBar 표시
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                progressBar.visibility = View.GONE  // 로그인 실패 시 ProgressBar 숨김
                Toast.makeText(this, "Google 로그인 실패: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d("LoginActivity", "ID Token: $idToken")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE  // 로그인 성공 또는 실패 시 ProgressBar 숨김
                if (task.isSuccessful) {
                    // 로그인 성공 - 메인 액티비티로 이동
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        // Firestore에 사용자 데이터 저장
                        saveUserToFirestore(user)
                    }
                    // MypageActivity로 이동(임시)
                    val intent = Intent(this, MypageFragment::class.java)
                    startActivity(intent)
                    finish() // LoginActivity 종료
                    //navigateToMainActivity(idToken, user?.email ?: "")
                } else {
                    // Firebase 인증 실패
                    Toast.makeText(this, "Firebase 인증 실패: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }

<<<<<<< Updated upstream
=======
    // 사용자 정의 데이터 서버로 전송
    private fun loginUser(postUserReq: LoginRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = createUser(postUserReq)
                if (response.isSuccess) {
                    // 서버 응답 성공 처리
                } else {
                    // 서버 응답 실패 처리
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 네트워크 오류 등의 예외 처리
            }
        }
    }

    private suspend fun createUser(loginRequest: LoginRequest) : BaseResponse<LoginResponse> {
        return RetrofitService.loginApi.postLoginUser(loginRequest)
    }

>>>>>>> Stashed changes
    private fun navigateToMainActivity(idToken: String, email: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("ID_TOKEN", idToken)
            putExtra("USER_EMAIL", email)
        }
        startActivity(intent)
        finish()
    }

    private fun saveUserToFirestore(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        val userProfile = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "name" to user.displayName
            // 기타 필요한 사용자 정보
        )

        db.collection("users").document(user.uid)
            .set(userProfile)
            .addOnSuccessListener {
                Log.d("LoginActivity", "Firestore에 사용자 정보 저장 성공")
            }
            .addOnFailureListener { e ->
                Log.d("LoginActivity", "Firestore에 사용자 정보 저장 실패", e)
            }
    }

}
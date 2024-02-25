package com.hearos.hearo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hearos.hearo.dto.UserInfo
import com.hearos.hearo.utils.FirebaseAuthUtils
import com.hearos.hearo.utils.FirebaseRef
import com.hearos.hearo.utils.HearoApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.prefs.Preferences

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


        //test code
        findViewById<Button>(R.id.btn_login_kakao).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.btn_login_submit).setOnClickListener {
            login()
        }

    }

    private fun login() {
        Log.d("LoginActivity", "로그인 시작") // 로그 추가
        val email = findViewById<EditText>(R.id.edt_login_username).text.toString()
        val password = findViewById<EditText>(R.id.edt_login_password).text.toString()

        progressBar.visibility = View.VISIBLE // 로딩 인디케이터 표시

        FirebaseAuthUtils.getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            progressBar.visibility = View.GONE // 로그인 시도 완료 후 로딩 인디케이터 숨김
            if (task.isSuccessful) {
                Log.d("LoginActivity", "로그인 성공") // 로그 추가
                Toast.makeText(this, "로그인에 성공했습니다!", Toast.LENGTH_SHORT).show()
                handleSuccessLogin(email, intent.getStringExtra("nickName").toString())
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Log.d("LoginActivity", "로그인 실패: ${task.exception?.localizedMessage}") // 로그 추가
                Toast.makeText(this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSuccessLogin(email: String, nickName: String) {
        if( FirebaseAuthUtils.getAuth().currentUser == null) {
            Toast.makeText(this,"로그인 정보가 올바르지 않습니다",Toast.LENGTH_SHORT).show()
            finish()
        }
        val userId = FirebaseAuthUtils.getAuth().currentUser?.uid.orEmpty()
        val currentDB = FirebaseRef.userInfo.child(userId)
        val userInfoMap = mutableMapOf<String,Any>()
        //saveUserInfo(userId, nickName)
        userInfoMap["userId"] = userId
        userInfoMap["name"] = nickName
        userInfoMap["email"] = email
        currentDB.updateChildren(userInfoMap)
    }

    private fun saveUserInfo(userId: String, name : String) {
        CoroutineScope(Dispatchers.Main).launch {
            HearoApp.getInstance().getDataStore().setUserInfo(userId, name)
        }
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

    // Google 로그인 함수 수정
    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d("LoginActivity", "Google 로그인 시도: ID Token: $idToken") // 로그 추가
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE // 로그인 완료 후 로딩 인디케이터 숨김
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "Google 로그인 성공") // 로그 추가
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val email = FirebaseAuthUtils.getAuth().currentUser?.email.orEmpty()
                    val name = FirebaseAuthUtils.getAuth().currentUser?.displayName
                    handleSuccessLogin(email, name!!)
                    navigateToMainActivity(idToken, email)
                } else {
                    Log.d("LoginActivity", "Google 로그인 실패: ${task.exception?.localizedMessage}") // 로그 추가
                    Toast.makeText(this, "Firebase 인증 실패: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainActivity(idToken: String, email: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("ID_TOKEN", idToken)
            putExtra("USER_EMAIL", email)
        }
        startActivity(intent)
        finish()
    }
}

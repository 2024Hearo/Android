package com.hearos.hearo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.play.integrity.internal.t
import com.hearos.hearo.databinding.ActivityRegisterBinding
import com.hearos.hearo.utils.FirebaseAuthUtils

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_register)


        detectEmailAndPasswordEmpty()
        binding.btnRegisterDone.setOnClickListener {
            registerUser()
        }
    }
    private fun detectEmailAndPasswordEmpty() {
        binding.btnRegisterDone.isEnabled = false

        binding.edtRegisterEmail.addTextChangedListener {
            val email = binding.edtRegisterEmail.text.toString()
            val password = binding.edtRegisterPassword.text.toString()
            binding.btnRegisterDone.isEnabled = email.isNotEmpty() && password.isNotEmpty()
        }

        binding.edtRegisterPassword.addTextChangedListener {
            val email = binding.edtRegisterEmail.text.toString()
            val password = binding.edtRegisterPassword.text.toString()
            var enalbed = email.isNotEmpty() && password.isNotEmpty()
            binding.btnRegisterDone.isEnabled = enalbed
        }

    }
    private fun registerUser() {
        val email = binding.edtRegisterEmail.text.toString()
        val password = binding.edtRegisterPassword.text.toString()
        val nickName = binding.edtRegisterNickname.text.toString()

        FirebaseAuthUtils.getAuth().createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this,"회원가입에 성공했습니다!",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,LoginActivity::class.java)
                intent.putExtra("nickName", nickName)
                startActivity(intent)
            } else{
                Toast.makeText(this,"이미 존재하는 계정이거나, 회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        }

    }
}
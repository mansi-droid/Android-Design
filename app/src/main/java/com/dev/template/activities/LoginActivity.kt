package com.dev.template.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dev.template.R
import com.dev.template.databinding.ActivityLoginBinding
import com.dev.template.utils.ApplicationUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = Firebase.auth

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }
    }

    private fun login() {
        if (ApplicationUtil.isInternetAvailable(this@LoginActivity)) {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (binding.etEmail.text.toString() == "") {
                binding.tlEmail.isErrorEnabled = true
                binding.tlEmail.error = getString(R.string.please_enter_name)
            } else if (binding.etPassword.text.toString() == "") {
                binding.tlEmail.isErrorEnabled = false
                binding.tlPassword.isErrorEnabled = true
                binding.tlPassword.error = getString(R.string.please_enter_password)
            } else {
                binding.tlPassword.isErrorEnabled = false
                ApplicationUtil.showProgressBar(binding.progressBar, binding.progressBarHolder, this@LoginActivity)
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                ApplicationUtil.hideProgressBar(binding.progressBar, binding.progressBarHolder, this@LoginActivity)
                                val verification = auth.currentUser?.isEmailVerified
                                if (verification == true) {
                                    Toast.makeText(this@LoginActivity, getString(R.string.welcome_msg), Toast.LENGTH_SHORT)
                                            .show()
                                    Log.d("Success", "createUserWithEmail:success")
                                    updateUI()
                                } else {
                                    Toast.makeText(this@LoginActivity, getString(R.string.please_verify_your_email), Toast.LENGTH_SHORT)
                                            .show()
                                }
                            } else {
                                ApplicationUtil.hideProgressBar(binding.progressBar, binding.progressBarHolder, this@LoginActivity)
                                Toast.makeText(this@LoginActivity, getString(R.string.authentication_failed), Toast.LENGTH_SHORT)
                                        .show()
//                                updateUI(null)
                            }
                        }
            }
        } else {
            Toast.makeText(this@LoginActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun updateUI() {
        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        finish()
    }
}
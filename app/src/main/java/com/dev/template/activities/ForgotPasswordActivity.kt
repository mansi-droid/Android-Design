package com.dev.template.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.dev.template.R
import com.dev.template.databinding.ActivityForgotPasswordBinding
import com.dev.template.utils.ApplicationUtil
import com.dev.template.utils.ApplicationUtil.Companion.isEmailValid

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        auth = FirebaseAuth.getInstance()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnForgotPassword.setOnClickListener {
            forgotPasswordUser()
        }
    }

    private fun forgotPasswordUser() {
        if (ApplicationUtil.isInternetAvailable(this@ForgotPasswordActivity)) {
            val email = binding.etEmail.text.toString()
            if (email == "") {
                binding.tlEmail.isErrorEnabled = true
                binding.tlEmail.error = getString(R.string.please_enter_email_address)
            } else if (!email.isEmailValid()) {
                binding.tlEmail.isErrorEnabled = true
                binding.tlEmail.error = getString(R.string.please_enter_valid_email_address)
            } else {
                ApplicationUtil.showProgressBar(binding.progressBar, binding.progressBarHolder, this@ForgotPasswordActivity)
                binding.tlEmail.isErrorEnabled = false
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    ApplicationUtil.hideProgressBar(binding.progressBar, binding.progressBarHolder, this@ForgotPasswordActivity)
                    Toast.makeText(this@ForgotPasswordActivity, getString(R.string.please_check_your_email), Toast.LENGTH_SHORT)
                            .show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this@ForgotPasswordActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT)
                    .show()
        }
    }
}
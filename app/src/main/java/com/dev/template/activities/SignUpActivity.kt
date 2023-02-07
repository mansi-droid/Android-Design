package com.dev.template.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dev.template.models.Users
import com.dev.template.utils.ApplicationUtil
import com.dev.template.utils.ApplicationUtil.Companion.isEmailValid
import com.dev.template.utils.CONSTANTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.dev.template.R
import com.dev.template.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding : ActivitySignUpBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            saveUserData()
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun saveUserData() {
        if (ApplicationUtil.isInternetAvailable(this@SignUpActivity)) {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (name == "") {
                binding.tlName.isErrorEnabled = true
                binding.tlName.error = getString(R.string.please_enter_name)
            } else if (email == "") {
                binding.tlName.isErrorEnabled = false
                binding.tlEmail.isErrorEnabled = true
                binding.tlEmail.error = getString(R.string.please_enter_email_address)
            } else if (!email
                        .isEmailValid()
            ) {
                binding.tlEmail.isErrorEnabled = true
                binding.tlEmail.error = getString(R.string.please_enter_valid_email_address)
            } else if (password == "") {
                binding.tlEmail.isErrorEnabled = false
                binding.tlPassword.isErrorEnabled = true
                binding.tlPassword.error = getString(R.string.please_enter_password)
            } else {
                binding.tlPassword.isErrorEnabled = false
                ApplicationUtil.showProgressBar(binding.progressBar, binding.progressBarHolder, this@SignUpActivity)
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                ApplicationUtil.hideProgressBar(binding.progressBar, binding.progressBarHolder, this@SignUpActivity)
                                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                                    Toast.makeText(this@SignUpActivity, getString(R.string.please_verify_your_email), Toast.LENGTH_SHORT)
                                            .show()
                                    Log.d("Success", "createUserWithEmail:success")
                                    saveUserDataOnFirebase(name, email, password)
                                    binding.etName.text?.clear()
                                    binding.etEmail.text?.clear()
                                    binding.etPassword.text?.clear()
                                    updateUI()
                                }?.addOnFailureListener {
                                    Toast.makeText(this@SignUpActivity, "Error", Toast.LENGTH_SHORT)
                                            .show()
                                }
                            } else {
                                ApplicationUtil.hideProgressBar(binding.progressBar, binding.progressBarHolder, this@SignUpActivity)
                                Toast.makeText(this, getString(R.string.error_user_registered), Toast.LENGTH_SHORT)
                                        .show()
//                                updateUI(null)
                            }
                        }
            }
        } else {
            Toast.makeText(this@SignUpActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun saveUserDataOnFirebase(name : String, email : String, password : String) {
        val user = Users(name, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child(CONSTANTS.fireBaseDataBase).child(userId).setValue(user)
    }

    private fun updateUI() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
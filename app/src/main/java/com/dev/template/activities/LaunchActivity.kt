package com.dev.template.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dev.template.R
import com.dev.template.databinding.ActivityLaunchBinding

class LaunchActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLaunchBinding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launch)

        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        binding.ivAppLogo.startAnimation(slideAnimation)
        binding.tvAppName.startAnimation(slideAnimation)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
package com.dev.template.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar

open class ApplicationUtil : Application() {
    private lateinit var mContext : Context
    private lateinit var applicationUtil : ApplicationUtil

    override fun onCreate() {
        super.onCreate()
        mContext = this
        applicationUtil = this
    }

    companion object{

        fun isInternetAvailable(context : Context) : Boolean {
            var result = false
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }

            return result
        }

        fun String.isEmailValid() : Boolean {
            return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
                    .matches()
        }

        fun hideProgressBar(
                progressBar : ProgressBar, progressBarHolder : FrameLayout, activity : Activity
        ) {
            try {
                progressBarHolder.visibility = View.GONE
                progressBar.visibility = View.GONE
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }

        fun showProgressBar(
                progressBar : ProgressBar, progressBarHolder : FrameLayout, activity : Activity
        ) {
            try {
                progressBarHolder.visibility = View.VISIBLE
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                progressBar.visibility = View.VISIBLE
                progressBar.invalidate()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }
}
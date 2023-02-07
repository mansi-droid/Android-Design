package com.dev.template.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.dev.template.R
import com.dev.template.databinding.ActivityDashboardBinding
import com.dev.template.utils.CONSTANTS

class DashboardActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        database = Firebase.database.reference
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        setSupportActionBar(binding.appBarDashboard.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_gallery, R.id.nav_about_us, R.id.nav_plans, R.id.nav_contact_us, R.id.nav_logout
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        Log.e("NavigationViewID", binding.navView.checkedItem.toString())
        val headerView = binding.navView.getHeaderView(0)
        val name = headerView.findViewById<TextView>(R.id.tvName)
        val email = headerView.findViewById<TextView>(R.id.tvEmail)

        database.child(CONSTANTS.fireBaseDataBase).child(userId).get().addOnSuccessListener {
            name.text = it.child("name").value.toString()
            email.text = it.child("email").value.toString()
            Log.i("DashboardActivity", "Got value $it")
        }.addOnFailureListener {
            Log.e("DashboardActivity", "Error getting data", it)
        }

        binding.navView.setNavigationItemSelectedListener {
            val id = it.itemId
            if (id == R.id.nav_logout) {
                Firebase.auth.signOut()
                Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

    }

    override fun onSupportNavigateUp() : Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
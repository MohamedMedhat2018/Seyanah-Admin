package com.example.serviceadmin.activities.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.serviceadmin.R
import com.example.serviceadmin.activities.login.LoginActivity
import com.example.serviceadmin.fragments.NotificationsFragment
import com.example.serviceadmin.fragments.ProfileFragment
import com.example.serviceadmin.models.Notification
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    val TAG = HomeActivity::class.java.name
    var listOfNotification: ArrayList<Notification> = ArrayList<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        com.beyond_tech.seyanahadminapp.helper.Helper(this).makeFullScreen(savedInstanceState)

        setContentView(R.layout.activity_main3)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        init()
        setupBottomNavView()
        loadFragment(NotificationsFragment())
    }

    private fun setupBottomNavView() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        var fragment: Fragment? = null
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_notifications -> {
//                    supportActionBar?.title = getString(R.string.title_notifications)
                    fragment = NotificationsFragment()

                }
                R.id.navigation_profile -> {
//                    supportActionBar?.title = getString(R.string.title_profile)
                    fragment = ProfileFragment()


                }
                R.id.navigation_logout -> {
//                    supportActionBar?.title = getString(R.string.title_logout)
                    logOut()
                }
            }
            loadFragment(fragment!!)
            return@setOnNavigationItemSelectedListener true

        }
    }

    private fun logOut() {
        com.beyond_tech.seyanahadminapp.helper.Helper(applicationContext).logOutUser()
        finish()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun init() {
//        supportActionBar?.title = getString(R.string.title_notifications)
        supportActionBar?.title = getString(R.string.app_name_root)

    }

    private fun loadFragment(fragment: Fragment) {
        //load fragment
        Log.e(TAG, "Teeest")
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}

package com.beyond_tech.seyanah_admin.activities.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.beyond_tech.seyanah_admin.R
import com.beyond_tech.seyanah_admin.activities.login.LoginActivity
import com.beyond_tech.seyanah_admin.fragments.NotificationsFragment
import com.beyond_tech.seyanah_admin.fragments.ProfileFragment
import com.beyond_tech.seyanah_admin.models.Notification
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {

    val TAG = HomeActivity::class.java.name
    var listOfNotification: ArrayList<Notification> = ArrayList<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        com.beyond_tech.seyanahadminapp.helper.Helper(this).makeFullScreen(savedInstanceState)

        setContentView(R.layout.activity_main3)

//        showBadge(this, mBottomNavigationView, R.id.navigation_notifications, "4+")

    }


    fun showBadge(
        context: Context?,
        bottomNavigationView: BottomNavigationView, @IdRes itemId: Int,
        value: String?
    ) {
        removeBadge(bottomNavigationView, itemId)
        val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
        val badge: View = LayoutInflater.from(context)
            .inflate(R.layout.custom_action_item_layout2, bottomNavigationView, false)
        val text = badge.findViewById<TextView>(R.id.badge_text_view)
        text.text = value
    }

    fun removeBadge(bottomNavigationView: BottomNavigationView, @IdRes itemId: Int) {
        val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
        if (itemView.childCount == 3) {
            itemView.removeViewAt(2)
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        init()
        setupBottomNavView()


        //loadFragment(NotificationsFragment())
        replaceFragmentSafely(
            NotificationsFragment(),
            NotificationsFragment::class.java.name,
            true,
            R.anim.fade_in,
            R.anim.fade_out,
//            R.anim.enter_from_left,
//            R.anim.enter_from_right,
//            R.anim.exit_to_left,
//            R.anim.exit_to_right,
            0, 0
        )


    }

    private fun setupBottomNavView() {
        val navView: BottomNavigationView = findViewById(R.id.mBottomNavigationView)
        var fragment: Fragment? = null
        var tag: String? = null
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_notifications -> {
//                    supportActionBar?.title = getString(R.string.title_notifications)
                    fragment = NotificationsFragment()
                    tag = NotificationsFragment::class.java.name
                }
                R.id.navigation_profile -> {
//                    supportActionBar?.title = getString(R.string.title_profile)
                    fragment = ProfileFragment()
                    tag = ProfileFragment::class.java.name
                }
                R.id.navigation_logout -> {
//                    supportActionBar?.title = getString(R.string.title_logout)
                    logOut()
                }
            }
            //loadFragment(fragment!!)
            replaceFragmentSafely(
                fragment!!, tag!!,
                true,
                R.anim.fade_in,
                R.anim.fade_out,
//            R.anim.enter_from_left,
//            R.anim.enter_from_right,
//            R.anim.exit_to_left,
//            R.anim.exit_to_right,
                0, 0
            )
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

    /**
     * Method to replace the fragment. The [fragment] is added to the container view with id
     * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
     */
    fun replaceFragmentSafely(
        fragment: Fragment,
        tag: String,
        allowStateLoss: Boolean = false,
//        containerViewId: Int,
        enterAnimation: Int = 0,
        exitAnimation: Int = 0,
        popEnterAnimation: Int = 0,
        popExitAnimation: Int = 0
    ) {
        val ft = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
//            .replace(containerViewId, fragment, tag)
            .replace(R.id.frame_container, fragment)
        if (!supportFragmentManager.isStateSaved) {
            ft.commit()
        } else if (allowStateLoss) {
            ft.commitAllowingStateLoss()
        }
    }

}

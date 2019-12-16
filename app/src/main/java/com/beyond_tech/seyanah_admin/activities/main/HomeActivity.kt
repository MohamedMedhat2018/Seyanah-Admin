package com.beyond_tech.seyanah_admin.activities.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.beyond_tech.seyanah_admin.R
import com.beyond_tech.seyanah_admin.activities.emailpass_register.LoginWithEmailPassActivity
import com.beyond_tech.seyanah_admin.constants.Constants
import com.beyond_tech.seyanah_admin.fire_utils.RefBase
import com.beyond_tech.seyanah_admin.fragments.NotificationsFragment
import com.beyond_tech.seyanah_admin.fragments.ProfileFragment
import com.beyond_tech.seyanah_admin.models.Notification
import com.beyond_tech.seyanah_admin.models.UserAdmin
import com.beyond_tech.seyanahadminapp.helper.Helper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pixplicity.easyprefs.library.Prefs
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {

    val TAG = HomeActivity::class.java.name
    var listOfNotification: ArrayList<Notification> = ArrayList<Notification>()
    var notificationCounter: Int = 0
    internal var gson = Gson()
    lateinit var userAdmin: UserAdmin
    lateinit var bottomNavigation: AHBottomNavigation
    var fragment: Fragment? = null
    var tag: String? = null

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
//        setupBottomNavView()
        setupBottomNavViewWithBadge()


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

        fetchTheNumberOfNotification()
    }

    private fun setupBottomNavViewWithBadge() {

        bottomNavigation =
            findViewById<AHBottomNavigation>(R.id.mBottomNavigationView)

// Create items
        // Create items
        val item1 =
            AHBottomNavigationItem(
                R.string.title_notifications,
                R.drawable.ic_notifications_black_24dp,
                R.color.color_tab_1
            )
        val item2 = AHBottomNavigationItem(
            R.string.title_profile,
            R.drawable.ic_person_black_24dp,
            R.color.color_tab_2
        )
        val item3 = AHBottomNavigationItem(
            R.string.title_logout,
            R.drawable.ic_exit_to_app_24px,
            R.color.color_tab_3
        )

// Add items
        // Add items
        bottomNavigation.addItem(item1)
        bottomNavigation.addItem(item2)
        bottomNavigation.addItem(item3)

// Set background color
        // Set background color
        bottomNavigation.defaultBackgroundColor = Color.parseColor("#FEFEFE")

// Disable the translation inside the CoordinatorLayout
        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.isBehaviorTranslationEnabled = false

// Enable the translation of the FloatingActionButton
        // Enable the translation of the FloatingActionButton
//        bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton)

// Change colors
        // Change colors
        bottomNavigation.accentColor = Color.parseColor("#F63D2B")
        bottomNavigation.inactiveColor = Color.parseColor("#747474")

// Force to tint the drawable (useful for font with icon for example)
        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.isForceTint = true

// Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        // Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        bottomNavigation.isTranslucentNavigationEnabled = true

// Manage titles
        // Manage titles
//        bottomNavigation.titleState = AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE
//        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
//        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE

// Use colored navigation with circle reveal effect
        // Use colored navigation with circle reveal effect
//        bottomNavigation.isColored = true
//
// Set current item programmatically
        // Set current item programmatically
        bottomNavigation.currentItem = 0

// Customize notification (title, background, typeface)
        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            bottomNavigation.setNotificationBackgroundColor(getColor(R.color.Orange))
//        } else {
//            bottomNavigation.setNotificationBackgroundColor(
//                ContextCompat.getColor(
//                    applicationContext,
//                    R.color.Orange
//                )
//            )
//        }

// Add or remove notification for each item
        // Add or remove notification for each item
//        bottomNavigation.setNotification("1", 0)
// OR
        // OR
//        val notification = AHNotification.Builder()
//            .setText("1")
//            .setBackgroundColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.color_notification_back
//                )
//            )
//            .setTextColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.color_notification_text
//                )
//            )
//            .build()
//        bottomNavigation.setNotification(notification, 1)

// Enable / disable item & set disable color
        // Enable / disable item & set disable color
//        bottomNavigation.enableItemAtPosition(2)
//        bottomNavigation.disableItemAtPosition(2)
//        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"))

// Set listeners
        // Set listeners
        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->

            when (position) {
                0 -> {
//                    supportActionBar?.title = getString(R.string.title_notifications)
                    fragment = NotificationsFragment()
                    tag = NotificationsFragment::class.java.name
                }
                1 -> {
//                    supportActionBar?.title = getString(R.string.title_profile)
                    fragment = ProfileFragment()
                    tag = ProfileFragment::class.java.name
                }
                2 -> {
//                    supportActionBar?.title = getString(R.string.title_logout)
                    logOut()
                }
            }

            if (fragment != null) {

                //loadFragment(fragment!!)
                replaceFragmentSafely(
                    fragment!!,
                    tag!!,
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

            true
        }
        bottomNavigation.setOnNavigationPositionListener {
            // Manage the new y position
        }

    }

    private fun fetchTheNumberOfNotification() {
        RefBase.cpNotification()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //                                dataSnapshot.getRef().removeEventListener(this);
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        notificationCounter = 0
                        for (snap in dataSnapshot.children) {
                            val hashMap =
                                snap.value as HashMap<String, Any?>?
                            if (hashMap != null) {
                                if (hashMap[Constants.ORDER_STATE] != null) {
                                    val shown =
                                        hashMap[Constants.ORDER_STATE] as Boolean
                                    if (shown) {
                                        notificationCounter++
                                    }
                                }
                            }
                        }
                        Log.e(TAG, notificationCounter.toString())

                        if (notificationCounter != 0) {
                            if ((notificationCounter <= 9)) {
                                bottomNavigation.setNotification(notificationCounter.toString(), 0)
                            } else {
                                bottomNavigation.setNotification("9+", 0)
                            }
                        } else {
//                            bottomNavigation.setCount(ID_NOTIFICATION, "")
                            bottomNavigation.setNotification("", 0)
                        }

                    } else {
                        Log.e(TAG, "ooh it is empty")
                        bottomNavigation.setNotification("", 0)

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun setupBottomNavView() {
        val navView: BottomNavigationView = findViewById(R.id.mBottomNavigationView)

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

            if (fragment != null) {

                //loadFragment(fragment!!)
                replaceFragmentSafely(
                    fragment!!,
                    tag!!,
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
            return@setOnNavigationItemSelectedListener true

        }
    }

    private fun logOut() {

//        val dialogLoggingOut = Helper(applicationContext).createProgressDialog(getString(R.string.loging_out))
        val dialogLoggingOut = Helper(this).createProgressDialog(getString(R.string.loging_out))
        dialogLoggingOut?.show()


//        com.beyond_tech.seyanahadminapp.helper.Helper(applicationContext).logOutUser()
        Prefs.remove(Constants.LOGGED_BEFORE).apply { }
        FirebaseAuth.getInstance().signOut()
        userAdmin = gson.fromJson<UserAdmin>(
            Prefs.getString(Constants.USER_ADMIN, ""),
            object : TypeToken<UserAdmin>() {
            }.type
        )
        RefBase.refAdmins(userAdmin.id!!)
            .child(Constants.MESSAGE_TOKEN).setValue("").addOnCompleteListener(
                OnCompleteListener {
                    if (it.isComplete) {
                        dialogLoggingOut?.dismiss()
//        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        startActivity(
                            Intent(
                                applicationContext,
                                LoginWithEmailPassActivity::class.java
                            )
                        )
                        finish()
                    }
                })


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.makeAllAsRead -> {
//                Toast.makeText(
//                    applicationContext, "Make All As Read",
//                    Toast.LENGTH_SHORT
//                ).show()
                changeAllCpNotificationAsRead()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeAllCpNotificationAsRead() {
        RefBase.cpNotification()
            .orderByChild(Constants.ORDER_STATE)
            .equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.removeEventListener(this)
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        dataSnapshot.children.iterator().forEach { dataSnapshot ->
                            dataSnapshot.ref.child(Constants.ORDER_STATE).setValue(false)
                        }

                        Toast.makeText(
                            applicationContext, "All Notifications Maked As Read",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onCancelled(p0: DatabaseError) {


                }
            })
    }

}

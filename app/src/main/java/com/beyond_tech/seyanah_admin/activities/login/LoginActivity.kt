package com.beyond_tech.seyanah_admin.activities.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beyond_tech.seyanahadminapp.helper.Helper
import com.beyond_tech.seyanah_admin.R
import com.beyond_tech.seyanah_admin.activities.main.HomeActivity
import com.beyond_tech.seyanah_admin.constants.Constants
import com.beyond_tech.seyanah_admin.models.UserAdmin
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_main.*


class LoginActivity : AppCompatActivity() {

    var TAG = LoginActivity::class.java.simpleName
    internal var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Helper(this).makeFullScreen(savedInstanceState)
//        makeFullScreen(savedInstanceState)

        if (checkIfUserLoggedInBefore()) {
            finish()
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        } else {
            //logged in before
            setContentView(R.layout.activity_main)
        }
//        checkIfUserLoggedInBefore().apply {
//            if (false) {
//                //logged in before
//                setContentView(R.layout.activity_main)
//            } else {
//                finish()
//                startActivity(Intent(applicationContext, HomeActivity::class.java))
//            }
//        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)



        if (checkIfUserLoggedInBefore()) {
            return
        }

//        checkIfUserLoggedInBefore().apply {
//            if (true) {
//                return
//            }
//        }


        accessRegisterViewPagerSub1()
        accessLogiViewPagerSub1()


        accessRegisterationViewPagerSub2()
        accessLoginViewPagerSub2()

        accessViewPager()


        addTetWatcherToShowPasswrdImageView(
            findViewById(R.id.etEnterPass_sub1),
            findViewById(R.id.ivShowPassword_sub1)
        )
        addTetWatcherToShowPasswrdImageView(
            findViewById(R.id.etEnterPass_sub2),
            findViewById(R.id.ivShowPassword1_reg)
        )
        addTetWatcherToShowPasswrdImageView(
            findViewById(R.id.etEnterConfPass_sub2),
            findViewById(R.id.ivShowPassword2_reg)
        )


    }

    private fun addTetWatcherToShowPasswrdImageView(editText: EditText, imageView: ImageView) {
        Log.e(TAG, "addTetWatcherToShowPasswrdImageView")
        var flagShowHidePassword = true
        imageView.setOnClickListener {
            if (flagShowHidePassword) {
                editText.transformationMethod = HideReturnsTransformationMethod()
                flagShowHidePassword = !flagShowHidePassword
                imageView.setImageResource(R.drawable.invisible)
            } else {
                editText.transformationMethod = PasswordTransformationMethod()
                flagShowHidePassword = !flagShowHidePassword
                imageView.setImageResource(R.drawable.visibile)
            }
        }
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
//                    Log.e(TAG, s.toString())
                if (s.toString().isNotEmpty()) {
//                    if(!s.toString()){
                    imageView.visibility = View.VISIBLE
                } else {
                    imageView.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
//                    Log.e(TAG, s.toString())


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    Log.e(TAG, s.toString())

            }
        })


    }

    private fun accessViewPager() {

        val animIn = AnimationUtils.loadAnimation(applicationContext, android.R.anim.slide_in_left)
        val animOut =
            AnimationUtils.loadAnimation(applicationContext, android.R.anim.slide_out_right)

        viewSwitcher.inAnimation = animIn
        viewSwitcher.outAnimation = animOut


    }

    private fun accessLoginViewPagerSub2() {
        btn_back.setOnClickListener {
            viewSwitcher.showPrevious()
        }

    }

    private fun accessRegisterationViewPagerSub2() {
        btn_register_sub2.setOnClickListener {
            //            registerUserSub1()
            registerUser_sub1()
        }

    }

    private fun accessLogiViewPagerSub1() {


        btn_login_sub1.setOnClickListener {

            if (etEnterUsername_sub1.text.isEmpty()) {
                fireToast(getString(R.string.enter_username))
                return@setOnClickListener
            }

            if (etEnterPass_sub1.text.isEmpty()) {
                fireToast(getString(R.string.enter_password))
                return@setOnClickListener
            }

//        if (etEnterPass_sub2.text.toString() != etEnterConfPass_sub2.text.toString()) {
//            fireToast(getString(R.string.password_dont_matches))
//            return
//        }


            val progress = Helper(this).createProgressDialog(getString(R.string.please_wait))
            progress?.show()

            FirebaseDatabase.getInstance()
                .reference
                .child(Constants.ADMINS)
                .orderByChild(Constants.REG_USERNAME)
                .equalTo(etEnterUsername_sub1.text.toString().trim())
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.removeEventListener(this)
                        if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {

                            FirebaseDatabase.getInstance()
                                .reference
                                .child(Constants.ADMINS)
                                .orderByChild(Constants.REG_USERNAME)
                                .equalTo(etEnterUsername_sub1.text.toString().trim())
                                .addChildEventListener(object : ChildEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                    }

                                    override fun onChildAdded(
                                        dataSnapshot: DataSnapshot,
                                        p1: String?
                                    ) {
                                        dataSnapshot.ref.removeEventListener(this)
                                        if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                                            progress?.dismiss()
                                            val userAdmin: UserAdmin? =
                                                dataSnapshot.getValue(UserAdmin::class.java)
                                            if (userAdmin?.username != etEnterUsername_sub1.text.toString()) {
                                                fireToast(getString(R.string.username_is_invalid))
                                                Log.e(TAG, getString(R.string.username_is_invalid))
                                            } else {
                                                if (userAdmin.password != etEnterPass_sub1.text.toString()) {
                                                    fireToast(getString(R.string.password_is_invalid))
                                                    Log.e(
                                                        TAG,
                                                        getString(R.string.password_is_invalid)
                                                    )

                                                } else {
//                                                    fireToast("Logged in success")
                                                    //for user session
                                                    Prefs.edit()
                                                        .putString(Constants.LOGGED_BEFORE, "")
                                                        .apply()

                                                    Prefs.putString(
                                                        Constants.USER_ADMIN,
                                                        gson.toJson(
                                                            userAdmin,
                                                            object : TypeToken<UserAdmin>() {
                                                            }.type
                                                        )
                                                    )

                                                    startActivity(
                                                        Intent(
                                                            applicationContext,
                                                            HomeActivity::class.java
                                                        )
                                                    )

                                                }
                                            }
                                        } else {

                                        }
                                    }

                                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {


                                    }

                                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {


                                    }

                                    override fun onChildRemoved(p0: DataSnapshot) {


                                    }
                                })




                        } else {
                            fireToast(getString(R.string.username_is_invalid))
                            progress?.dismiss()
                        }
                    }

                })
        }

    }

    fun fireToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
            .show()
    }

    private fun accessRegisterViewPagerSub1() {
        btn_register_sub1.setOnClickListener {
            viewSwitcher.showNext()
            //registerUser_sub1()
        }
    }

    private fun registerUser_sub1() {
        if (etEnterUsername_sub2.text.isEmpty()) {
            fireToast(getString(R.string.enter_username))
            return
        }

        if (etEnterEmail.text.isEmpty()) {
            fireToast(getString(R.string.enter_email_required))
            return
        }

        if (!Helper(applicationContext).isEmailValid(etEnterEmail.text.toString())) {
            fireToast(getString(R.string.enter_valid_email))
            return
        }

        if (etEnterPhone.text.isEmpty()) {
            fireToast(getString(R.string.enter_phone_number_required))
            return
        }

        if (etEnterPass_sub2.text.isEmpty()) {
            fireToast(getString(R.string.enter_password))
            return
        }

        if (etEnterPass_sub2.text.toString() != etEnterConfPass_sub2.text.toString()) {
            fireToast(getString(R.string.password_dont_matches))
            return
        }


//        val progress = Helper(this).createProgressDialog(getString(R.string.please_wait))
        val progress = Helper(this).createProgressDialog(getString(R.string.registering))
        progress?.show()

        //check if the username already exist or not
        FirebaseDatabase.getInstance().reference
            .child(Constants.ADMINS)
            .orderByChild(Constants.REG_USERNAME)
//            .equalTo(etEnterUsername_sub1.text.toString())
            .equalTo(etEnterUsername_sub2.text.toString().trim())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.removeEventListener(this)
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        dataSnapshot.ref.addChildEventListener(object : ChildEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                                dataSnapshot.ref.removeEventListener(this)
                                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                                    fireToast(getString(R.string.username_exist))
                                    progress?.dismiss()

                                } else {
                                    registerUserSub1(progress!!)
                                }
                            }

                            override fun onChildChanged(p0: DataSnapshot, p1: String?) {


                            }

                            override fun onChildMoved(p0: DataSnapshot, p1: String?) {


                            }

                            override fun onChildRemoved(p0: DataSnapshot) {


                            }
                        })
                    } else {
                        registerUserSub1(progress!!)
                    }
                }


            })

    }

    private fun registerUserSub1(dialog: AlertDialog) {
        FirebaseDatabase.getInstance().let {
            val userAdmin = UserAdmin()
//            userAdmin.username = etEnterUsername_sub1.text.toString()
//            userAdmin.password = etEnterPass_sub1.text.toString()
            userAdmin.username = etEnterUsername_sub2.text.toString().trim()
            userAdmin.password = etEnterPass_sub2.text.toString().trim()
            userAdmin.email = etEnterEmail.text.toString().trim()
            userAdmin.phoneNumber = etEnterPhone.text.toString().trim()


//            val dialog = Helper(this).createProgressDialog(getString(R.string.loading))

            var pushedKey = it.reference.database.reference
                .child(Constants.ADMINS)
                .push().key


            userAdmin.id = pushedKey

            it.reference.database.reference
                .child(Constants.ADMINS)
                .child(pushedKey!!)
                .setValue(userAdmin).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.admin_created_succes),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        dialog.dismiss()

                        viewSwitcher.showPrevious()
                        etEnterUsername_sub1.text = etEnterUsername_sub2.text

                        etEnterUsername_sub2.text.clear()
                        etEnterPass_sub2.text.clear()
                        etEnterEmail.text.clear()
                        etEnterPass_sub2.text.clear()
                        etEnterConfPass_sub2.text.clear()

//                        finish()
//                        startActivity(Intent(applicationContext, HomeActivity::class.java))

                    }
                    if (it.isCanceled) {
                        dialog.dismiss()
//                        Toast.makeText(
////                            applicationContext,
////                            Constants.NETWORK_ERROR,
////                            Toast.LENGTH_SHORT
////                        )
////                            .show()

                        fireToast(getString(R.string.network_error))

                    }
                }
        }

    }

    fun checkIfUserLoggedInBefore(): Boolean {
        if (Prefs.contains(Constants.LOGGED_BEFORE)) {
            return true
        }
        return false
    }
}

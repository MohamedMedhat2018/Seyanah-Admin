package com.beyond_tech.seyanah_admin.activities.emailpass_register

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beyond_tech.seyanah_admin.R
import com.beyond_tech.seyanah_admin.activities.main.HomeActivity
import com.beyond_tech.seyanah_admin.constants.Constants
import com.beyond_tech.seyanah_admin.models.UserAdmin
import com.beyond_tech.seyanahadminapp.helper.Helper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.irozon.sneaker.Sneaker
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_main_email_pass_register.*


class LoginWithEmailPassActivity : AppCompatActivity() {
    var TAG = LoginWithEmailPassActivity::class.java.simpleName
    internal var gson = Gson()
    var progress: AlertDialog? = null
    val firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Helper(this).makeFullScreen(savedInstanceState)
//        makeFullScreen(savedInstanceState)
        if (checkIfUserLoggedInBefore()) {
            finish()
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        } else {
            //logged in before
            setContentView(R.layout.activity_main_email_pass_register)
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
        FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
                // the user will receive another verification email.
//                sendVerificationEmail(user)
            } else {
                // User is signed out

            }
        }


    }

    private fun sendVerificationEmail(firebaseUser: FirebaseUser) {

        var message: String? = null

        if (registering) {
            message = getString(R.string.registering)
        } else {
            message = getString(R.string.resending)
        }

        progress = Helper(this).createProgressDialog(message)
        progress!!.show()

        firebaseUser.sendEmailVerification()
            .addOnCompleteListener(OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    // email sent
                    // after email is sent just logout the user and finish this activity
//                   FirebaseAuth.getInstance().signOut()
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.email_sent_to),
                        Toast.LENGTH_LONG
                    ).show()


                    btn_resend_ver_email.visibility = View.GONE

                    progress!!.dismiss()


                    if (registering) {
                        registerUserSub1()
                    } else {
                    }


                } else {
                    progress!!.dismiss()
                    // email not sent, so display message and restart the activity or do whatever you wish to do
                    Toast.makeText(
                        applicationContext,
                        "Network connection error",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

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


        btn_resend_ver_email.setOnClickListener {
            sendVerificationEmail(FirebaseAuth.getInstance().currentUser!!)
        }

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

    private fun testingScalingAnim() {

        etEnterEmailAddress_sub1.isFocusable = false
        etEnterPass_sub1.isFocusable = false

        //show resend
        btn_resend_ver_email.visibility = View.VISIBLE
        val anim = ScaleAnimation(
            0.0f,
            1.0f,
            1.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        anim.duration = 700
        btn_resend_ver_email.startAnimation(anim)

//        val anim = ScaleAnimation(
//            1.0f,
//            0.0f,
//            1.0f,
//            1.0f,
//            Animation.RELATIVE_TO_SELF,
//            1.0f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f
//        )
//        anim.duration = 700
//        btn_resend_ver_email.startAnimation(anim)
//        anim.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation) {
//
//            }
//
//            override fun onAnimationEnd(animation: Animation) {
//                btn_resend_ver_email.visibility = View.GONE
//            }
//
//            override fun onAnimationRepeat(animation: Animation) {
//
//            }
//        })


        //hide login
        val anim2 = ScaleAnimation(
            1.0f,
            1.0f,
            1.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f
        )
        anim2.duration = 700
        btn_login_sub1.startAnimation(anim2)
        anim2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                //btn_resend_ver_email.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }

    private fun accessLogiViewPagerSub1() {
        btn_login_sub1.setOnClickListener {
            Log.e(TAG, "setOnClickListener")

            if (etEnterEmailAddress_sub1.text.isEmpty()) {
                fireToast(getString(R.string.enter_email_required))
                return@setOnClickListener
            }

            if (etEnterPass_sub1.text.isEmpty()) {
                fireToast(getString(R.string.enter_password))
                return@setOnClickListener
            }


            btn_resend_ver_email.visibility = View.GONE


//            progress = Helper(this).createProgressDialog(getString(R.string.please_wait))
            progress = Helper(this).createProgressDialog(getString(R.string.loggin_in))
            progress!!.show()

            val email = etEnterEmailAddress_sub1.text.toString()
            val pass = etEnterPass_sub1.text.toString()

            Log.e(TAG, email)
            Log.e(TAG, pass)


            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                etEnterEmailAddress_sub1.text.toString(),
                etEnterPass_sub1.text.toString()
            )
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult> { task ->
                        Log.e("TAG", "signInWithEmail:onComplete:" + task.isSuccessful)

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful) {
                            //Log.w("TAG", "signInWithEmail:failed", task.getException());
                            fireToast(getString(R.string.invalid_email_or_pass))
                            progress!!.dismiss()
                            return@OnCompleteListener
                        } else {
                            if (FirebaseAuth.getInstance().currentUser != null &&
                                !FirebaseAuth.getInstance().currentUser!!.isEmailVerified
                            ) {
                                //sendVerificationEmail(FirebaseAuth.getInstance().currentUser!!)


                                btn_resend_ver_email.visibility = View.VISIBLE
//                                val anim = ScaleAnimation(
//                                    0.0f,
//                                    1.0f,
//                                    1.0f,
//                                    1.0f,
//                                    Animation.RELATIVE_TO_SELF,
//                                    1.0f,
//                                    Animation.RELATIVE_TO_SELF,
//                                    0.5f
//                                )
//                                anim.duration = 700
//                                btn_resend_ver_email.startAnimation(anim)


                                fireToast(getString(R.string.verify_email_address))
                                progress?.dismiss()
                                return@OnCompleteListener
                            } else {

                                FirebaseInstanceId.getInstance()
                                    .instanceId.addOnSuccessListener(OnSuccessListener {
                                    val newToken = it.token

                                    FirebaseDatabase.getInstance()
                                        .reference
                                        .child(Constants.ADMINS)
                                        .orderByChild(Constants.REG_EMAIL)
//                                    .orderByChild(Constants.REG_USERNAME)
                                        .equalTo(etEnterEmailAddress_sub1.text.toString().trim())
                                        .addValueEventListener(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {
                                            }

                                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                                dataSnapshot.ref.removeEventListener(this)
//                                           dataSnapshot.children.forEach { dataSnapshot ->
//                                           }
                                                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                                                    val dataSnapshot =
                                                        dataSnapshot.children.elementAt(0)
                                                    progress?.dismiss()

                                                    dataSnapshot.ref.child(Constants.MESSAGE_TOKEN)
                                                        .setValue(newToken)

                                                    val userAdmin: UserAdmin? =
                                                        dataSnapshot.getValue(UserAdmin::class.java)

                                                    //Saving that user have been logged in before
                                                    Prefs.edit()
                                                        .putString(Constants.LOGGED_BEFORE, "")
                                                        .apply()

                                                    //saving the fetched user admin as the Profile Fragment
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
                                                } else {
                                                    progress?.dismiss()
                                                    fireToast(message = getString(R.string.network_error))
                                                }

                                            }
                                        })

                                })


//                                    .addChildEventListener(object : ChildEventListener {
//                                        override fun onCancelled(p0: DatabaseError) {
//                                        }
//
//                                        override fun onChildAdded(
//                                            dataSnapshot: DataSnapshot,
//                                            p1: String?
//                                        ) {
//                                            dataSnapshot.ref.removeEventListener(this)
//                                            if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
//                                                progress?.dismiss()
//                                                val userAdmin: UserAdmin? =
//                                                    dataSnapshot.getValue(UserAdmin::class.java)
//
//                                                //Saving that user have been logged in before
//                                                Prefs.edit()
//                                                    .putString(Constants.LOGGED_BEFORE, "")
//                                                    .apply()
//
//                                                //saving the fetched user admin as the Profile Fragment
//                                                Prefs.putString(
//                                                    Constants.USER_ADMIN,
//                                                    gson.toJson(
//                                                        userAdmin,
//                                                        object : TypeToken<UserAdmin>() {
//                                                        }.type
//                                                    )
//                                                )
//
//                                                startActivity(
//                                                    Intent(
//                                                        applicationContext,
//                                                        HomeActivity::class.java
//                                                    )
//                                                )
//                                            } else {
//                                                progress?.dismiss()
//                                                fireToast(message = getString(R.string.network_error))
//                                            }
//
//                                        }
//
//                                        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//
//
//                                        }
//
//                                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
//
//
//                                        }
//
//                                        override fun onChildRemoved(p0: DataSnapshot) {
//
//                                        }
//                                    })


                            }
                        }
                        // ...
                    })
                .addOnFailureListener(OnFailureListener {
                    Log.e(TAG, it.localizedMessage)
                    progress?.dismiss()
                })

        }

    }

    var registering: Boolean = false

    private fun registerNewUser(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    //        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
                    FirebaseInstanceId.getInstance()
                        .instanceId.addOnSuccessListener(OnSuccessListener {
                        afterCreateInstanceId(it)
                    })

                } else {
                    fireToast(message = getString(R.string.email_already_exist))
                    viewSwitcher.showPrevious()
                    progress?.dismiss()
                }
            })
            .addOnFailureListener(OnFailureListener {
                Log.e(TAG, it.localizedMessage)
                progress?.dismiss()
//                Toast.makeText(
//                    applicationContext,
//                    "The email address is already in use by another account.",
//                    Toast.LENGTH_LONG
//                ).show()
            })

    }


    private fun afterCreateInstanceId(instanceIdResult: InstanceIdResult) {
        val newToken = instanceIdResult.token
        userAdmin.messageTokenId = newToken
        registering = true
        sendVerificationEmail(FirebaseAuth.getInstance().currentUser!!)
        fireToast(message = getString(R.string.verify_email_address))
        //progress?.dismiss()
//                    registerUserSub1(progress!!)
    }

    private fun fireToast(message: String) {
//        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
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

        if (etEnterEmail_required.text.isEmpty()) {
            fireToast(getString(R.string.enter_email_required))
            return
        }

        if (!Helper(applicationContext).isEmailValid(etEnterEmail_required.text.toString())) {
            fireToast(getString(R.string.enter_valid_email))
            return
        }

        val txt: String? = etEnterEmail_required.text.toString()
//        val regex = "@seyanah-uae.com"
        val regex = "@btechme.com"
//        val regex = ""

        /*
        * Email domain restriction*/
//        if (!txt!!.trim().endsWith(regex, true)) {
//            fireToast(getString(R.string.enter_email_required))
//            return
//        }

//        if (etEnterPhone.text.isEmpty()) {
//            fireToast(getString(R.string.enter_phone_number_required))
//            return
//        }

        if (etEnterPass_sub2.text.isEmpty()) {
            fireToast(getString(R.string.enter_password))
            return
        }

        if (etEnterPass_sub2.text.length < 6) {
            fireToast(getString(R.string.pass_should_at_least))
            return
        }

        if (etEnterPass_sub2.text.toString() != etEnterConfPass_sub2.text.toString()) {
            fireToast(getString(R.string.password_dont_matches))
            return
        }

//               progress = Helper(this).createProgressDialog(getString(R.string.registering))
//        progress?.show()


        registerNewUser(
            etEnterEmail_required.text.toString(),
            etEnterPass_sub2.text.toString()
        )

    }

    val userAdmin = UserAdmin()

    private fun registerUserSub1() {

        var dialog: AlertDialog? = null
        dialog = Helper(this).createProgressDialog(getString(R.string.registering))
        dialog?.show()


        FirebaseDatabase.getInstance().let {

            //            userAdmin.username = etEnterUsername_sub1.text.toString()
//            userAdmin.password = etEnterPass_sub1.text.toString()
            userAdmin.username = etEnterUsername_sub2.text.toString().trim()
            userAdmin.password = etEnterPass_sub2.text.toString().trim()
            userAdmin.email = etEnterEmail_required.text.toString().trim()
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
//                        Toast.makeText(
//                            applicationContext,
//                            getString(R.string.admin_created_succes),
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()

                        Sneaker.with(this)
//                            .setTitle(getString(R.string.done_success))
                            .setCornerRadius(5, 5)
                            .setMessage(getString(R.string.admin_created_succes))
                            .sneakSuccess()


                        dialog?.dismiss()

                        viewSwitcher.showPrevious()
                        etEnterEmailAddress_sub1.text = etEnterEmail_required.text

                        etEnterUsername_sub2.text.clear()
                        etEnterPass_sub2.text.clear()
                        etEnterEmail_required.text.clear()
                        etEnterPass_sub2.text.clear()
                        etEnterConfPass_sub2.text.clear()


                        if (FirebaseAuth.getInstance().currentUser != null &&
                            !FirebaseAuth.getInstance().currentUser!!.isEmailVerified
                        ) {
//                            sendVerificationEmail(FirebaseAuth.getInstance().currentUser!!)
//                            Toast.makeText(
//                                applicationContext,
//                                "Please verify your email address!!!",
//                                Toast.LENGTH_LONG
//                            ).show()
                            dialog?.dismiss()
                        } else {
                            dialog?.dismiss()


                            finish()
//                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)

                        }

                    }
//                    if (it.isCanceled) {
//                        dialog.dismiss()
////                        Toast.makeText(
//////                            applicationContext,
//////                            Constants.NETWORK_ERROR,
//////                            Toast.LENGTH_SHORT
//////                        )
//////                            .show()
//
//                        fireToast(getString(R.string.network_error))

//                    }
                }
        }

    }

    private fun checkIfUserLoggedInBefore(): Boolean {
        if (Prefs.contains(Constants.LOGGED_BEFORE)) {
            return true
        }
        return false
    }
}

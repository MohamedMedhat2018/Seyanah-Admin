package com.beyond_tech.seyanahadminapp.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.beyond_tech.seyanah_admin.constants.Constants
import com.google.firebase.auth.FirebaseAuth
import com.pixplicity.easyprefs.library.Prefs
import java.util.regex.Matcher
import java.util.regex.Pattern


class Helper {
    var context : Context? = null
    var activity : Activity? = null



    constructor(activity: Activity?){
        this.activity = activity
    }

    constructor(context: Context?){
        this.context = context
    }


    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }


    fun isValidPassword(password: String): Boolean {

        val pattern: Pattern
        val matcher: Matcher

        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"

        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)

        return matcher.matches()

    }




    fun makeFullScreen(savedInstanceState: Bundle?) {
        activity?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun createProgressDialog(message: String): AlertDialog? {

        val llPadding = 30
        val ll = LinearLayout(activity)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(activity)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(activity)
//        tvText.text = activity.getString(R.string.loading)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setView(ll)

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
//        dialog.show()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = layoutParams
        }

        return dialog
    }

    fun logOutUser(): Boolean{
        Prefs.remove(Constants.LOGGED_BEFORE).apply {  }
        FirebaseAuth.getInstance().signOut()

        Prefs.remove(Constants.LOGGED_BEFORE).apply {
           return true
        }
        return false
    }


    public fun changeShapeColor(mContext: Activity?, background: Drawable?, color: Int?) {
        when (background) {
            is ShapeDrawable -> {
                background.paint.color = ContextCompat.getColor(mContext!!, color!!)
            }

            is GradientDrawable -> background.setColor(
                ContextCompat.getColor(
                    mContext!!,
                    color!!
                )
            )
            is ColorDrawable -> background.color =
                ContextCompat.getColor(mContext!!, color!!)
        }
    }


}
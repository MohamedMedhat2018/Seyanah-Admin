package com.beyond_tech.seyanah_admin.sheets


import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.beyond_tech.seyanahadminapp.helper.Helper
import com.beyond_tech.seyanah_admin.R
import com.beyond_tech.seyanah_admin.constants.Constants
import com.beyond_tech.seyanah_admin.events.RxEvent
import com.beyond_tech.seyanah_admin.fire_utils.RefBase
import com.beyond_tech.seyanah_admin.models.UserAdmin
import com.beyond_tech.seyanah_admin.rxbus.RxBus
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.annotations.NotNull
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class BottomSheet : SuperBottomSheetFragment() {

    var TAG: String = BottomSheet::class.java.name

    init {
        isCancelable = true
    }

    companion object {
//        const val TAG = BottomSheet::getTag
    }

    override fun onCreateView(
        @NotNull inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //        viewRoot = inflater.inflate(R.layout.fragment_user_worker_profile, container, false);
        //        ButterKnife.bind(this@BottomFrgForgotPass, viewRoot)
//        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        return inflater.inflate(R.layout.fragment_bottom_sheet, null);
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        RxBus.publish(RxEvent.EventUserDataUpdated(true))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.e(TAG, getEditType())
        accessEditTextFromExtras()
        btn_confirm_edit.setOnClickListener {
            //            Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show()
            btnUpdatePhone()
        }
    }

    private fun accessEditTextFromExtras() {
        when (getEditType()) {
            Constants.EDIT_PHONE_NUMBER -> {
                view?.findViewById<EditText>(R.id.et_input_type)!!.setText(
                    arguments!!.getString(
                        Constants.EDIT_PHONE_NUMBER
                    ).toString()
                )
            }
            Constants.EDIT_EMAIL -> {
                //                view?.findViewById<EditText>(R.id.et_input_type)!!.text =
//                    Editable.Factory.getInstance()
//                        .newEditable(arguments!!.get(Constants.EDIT_EMAIL).toString())
                view?.findViewById<EditText>(R.id.et_input_type)!!.setText(
                    arguments!!.getString(
                        Constants.EDIT_EMAIL
                    ).toString()
                )
            }
            Constants.EDIT_PASSWORD -> {
                //                view?.findViewById<EditText>(R.id.et_input_type)!!.text =
//                    Editable.Factory.getInstance()
//                        .newEditable(arguments!!.getString(Constants.EDIT_PASSWORD).toString())
                view?.findViewById<EditText>(R.id.et_input_type)!!.setText(
                    arguments!!.getString(
                        Constants.EDIT_PASSWORD
                    ).toString()
                )

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getEditType(): String {
        if (arguments != null) {

            Log.e(TAG, arguments!!.getString(Constants.EDIT_PHONE_NUMBER).toString())
            Log.e(TAG, arguments!!.getString(Constants.EDIT_EMAIL).toString())
            Log.e(TAG, arguments!!.getString(Constants.EDIT_PASSWORD).toString())

            if (arguments!!.containsKey(Constants.EDIT_PHONE_NUMBER)) {
                return Constants.EDIT_PHONE_NUMBER
            }

            if (arguments!!.containsKey(Constants.EDIT_EMAIL)) {
                return Constants.EDIT_EMAIL
            }

            if (arguments!!.containsKey(Constants.EDIT_PASSWORD)) {
                return Constants.EDIT_PASSWORD
            }
        }
        return ""
    }


    private fun btnUpdatePhone() {
        when (getEditType()) {
            Constants.EDIT_PHONE_NUMBER -> {
                if (TextUtils.isEmpty(et_input_type!!.text)) {
//                    Utils.startWobble(activity, etNewPass)
//                    tv_error!!.text = "Phone must not be empty"
                    tv_error.visibility = View.VISIBLE
                    tv_error!!.text = getString(R.string.enter_phone_number_required)
                    return
                }
            }

            Constants.EDIT_EMAIL -> {
                if (TextUtils.isEmpty(et_input_type!!.text)) {
//                    Utils.startWobble(activity, etNewPass)
//                    tv_error!!.text = "Email must not be empty"
                    tv_error!!.text = getString(R.string.enter_email_required)
                    tv_error.visibility = View.VISIBLE

                    return
                } else if (!Helper(activity).isEmailValid(et_input_type!!.text.toString().trim { it <= ' ' })) {
//                        Utils.startWobble(activity, etNewPass)
//                    tv_error!!.error = "Enter valid email"
//                    tv_error!!.error = getString(R.string.enter_valid_email)
                    tv_error!!.text = getString(R.string.enter_valid_email)
                    tv_error.visibility = View.VISIBLE

                    return
                }
            }

            Constants.EDIT_PASSWORD -> {
                if (TextUtils.isEmpty(et_input_type!!.text)) {
//                    Utils.startWobble(activity, etNewPass)
//                    tv_error!!.text = "Password must not be empty"
                    tv_error!!.text = getString(R.string.enter_password)
                    tv_error.visibility = View.VISIBLE

                    return
                } else if (Helper(activity).isValidPassword(et_input_type!!.text.toString().trim { it <= ' ' })) {
//                        Utils.startWobble(activity, etNewPass)
//                    tv_error!!.text = "Password should contain at least 6 characters with one upper and lower character"
                    tv_error!!.text = getString(R.string.pass_should_at_least)
                    tv_error.visibility = View.VISIBLE

                    return
                }
                return
            }
        }

        tv_error.visibility = View.GONE

//        if (tv_error.text.isEmpty()) {
        if (tv_error.visibility == View.GONE) {

            val gson = Gson()
            var userAdmin = gson.fromJson<UserAdmin>(
                Prefs.getString(Constants.USER_ADMIN, ""),
                object : TypeToken<UserAdmin>() {}.type
            )

            val map = HashMap<String, Any>()
            val s = et_input_type.text.toString().trim()

            when (getEditType()) {
                Constants.EDIT_PHONE_NUMBER -> {
                    map[Constants.KEY_PHONE_NUMBER] = s
                    Log.e(TAG, "KEY_PHONE_NUMBER")


                }
                Constants.EDIT_EMAIL -> {
                    map[Constants.KEY_EMAIL_ADDRESS] = s
                    Log.e(TAG, "KEY_EMAIL_ADDRESS")

                }
                Constants.EDIT_PASSWORD -> {
                    map[Constants.KEY_PASSWORD] = s
                    Log.e(TAG, "KEY_PASSWORD")

                }
            }
//            Log.e(TAG, "btnUpdatePassword: " + Prefs.getString(Constants.FIREBASE_UID, ""))
            RefBase.refAdmins(userAdmin.id!!)
                .updateChildren(map)
                .addOnCompleteListener(OnCompleteListener<Void> { task ->
                    if (task.isSuccessful) {
                        dismiss()
                        Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show()
//                        Prefs.edit().remove(Constants.FIREBASE_UID).apply()
                        Prefs.putString(Constants.USER_ADMIN, Gson().toJson(userAdmin))
                    } else {

                    }
                })
        }


    }

}

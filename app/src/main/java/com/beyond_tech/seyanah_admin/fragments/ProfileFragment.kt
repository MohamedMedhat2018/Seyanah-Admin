package com.beyond_tech.seyanah_admin.fragments

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.beyond_tech.seyanahadminapp.helper.Helper
import com.beyond_tech.seyanah_admin.R
import com.beyond_tech.seyanah_admin.constants.Constants
import com.beyond_tech.seyanah_admin.events.RxEvent
import com.beyond_tech.seyanah_admin.fire_utils.RefBase
import com.beyond_tech.seyanah_admin.models.UserAdmin
import com.beyond_tech.seyanah_admin.rxbus.RxBus
import com.beyond_tech.seyanah_admin.sheets.BottomSheet
import com.beyond_tech.seyanah_admin.sheets.FileUtils
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.io.*
import java.util.*

class ProfileFragment : Fragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    var detached: Boolean = false;
    private lateinit var disposable: Disposable
    internal lateinit var easyImage: EasyImage
    //    lateinit var bottomSheetFragment: BottomSheet
    lateinit var bundle: Bundle
    lateinit var userAdmin: UserAdmin
    internal var gson = Gson()
    var TAG = ProfileFragment::javaClass.name
    private var dialog: BottomSheetMenuDialog? = null
    private var ref: StorageReference? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        //            Manifest.permission.WRذITE_CONTACTS,
        Manifest.permission.READ_EXTERNAL_STORAGE
        , Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var spruceAnimator: Animator? = null


    companion object {
        const val RC_CAMERA_AND_STORAGE = 121
    }


    override fun onResume() {
        super.onResume()
        spruceAnimator?.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_profile, null)
    }


    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()
        initEasyImage()
        setOnClickListeners()
        loadProfileData()
        initDialogPickPhotoSource()

        disposable = RxBus.listen(RxEvent.EventUserDataUpdated::class.java).subscribe {
            loadProfileData()
        }
    }


    private fun initEasyImage() {
        easyImage = EasyImage.Builder(this.activity!!)

            // Chooser only
            // Will appear as a system chooser title, DEFAULT empty string
            //.setChooserTitle("Pick media")
            // Will tell chooser that it should show documents or gallery apps
            //.setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)  you can use this or the one below
            //.setChooserType(ChooserType.CAMERA_AND_GALLERY)
            // Setting to true will cause taken pictures to show up in the device gallery, DEFAULT false
            .setCopyImagesToPublicGalleryFolder(false)
            // Sets the name for images stored if setCopyImagesToPublicGalleryFolder = true
            .setFolderName("EasyImage sample")
            // Allow multiple picking
            .allowMultiple(true)
            .build()
    }


    private fun loadProfileData() {


        val progress = Helper(activity).createProgressDialog(getString(R.string.please_wait))
        progress?.show()


        userAdmin = gson.fromJson<UserAdmin>(
            Prefs.getString(Constants.USER_ADMIN, ""),
            object : TypeToken<UserAdmin>() {
            }.type
        )
        RefBase.refAdmins(userAdmin.id!!).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.ref.removeEventListener(this)
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    if (!detached) {
                        val userAdmin = dataSnapshot.getValue(UserAdmin::class.java)
                        view!!.findViewById<TextView>(R.id.tv_Profile_email).text =
                            Editable.Factory.getInstance().newEditable(userAdmin!!.email.toString())
                        view!!.findViewById<TextView>(R.id.tv_Profile_password).text =
                            Editable.Factory.getInstance()
                                .newEditable(userAdmin.password.toString())
                        view!!.findViewById<TextView>(R.id.tv_Profile_phone).text =
                            Editable.Factory.getInstance()
                                .newEditable(userAdmin.phoneNumber.toString())
                        view!!.findViewById<TextView>(R.id.tv_Profile_name).text =
                            Editable.Factory.getInstance()
                                .newEditable(userAdmin.username.toString())


                        if (userAdmin.profilePhoto.toString().trimmedLength() != 0) {
                            Picasso.get().load(userAdmin.profilePhoto)
                                .into(ivUserPhoto, object : Callback {
                                    override fun onSuccess() {
                                        Log.e(TAG, "Loaded success")
                                        view?.findViewById<ProgressBar>(R.id.progressImageLoaded)
                                            ?.visibility = View.GONE
                                    }

                                    override fun onError(e: Exception?) {


                                    }
                                })
                        } else {
                            view?.findViewById<ProgressBar>(R.id.progressImageLoaded)
                                ?.visibility = View.GONE
                        }

                        progress?.dismiss()

                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                progress?.dismiss()
            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        detached = true;
    }

    private fun initVars() {
//        bottomSheetFragment = BottomSheet()
        bundle = Bundle()
        ivUserPhoto.setOnClickListener {
            //            Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show()
            requestCamAndStoragePerms()
        }
    }

    private fun setOnClickListeners() {

        cv_profile_phone.setOnClickListener {
            bundle.clear()
            bundle.putString(
                Constants.EDIT_PHONE_NUMBER,
                view!!.findViewById<TextView>(R.id.tv_Profile_phone).text.toString()
            )
            showBottomSheet(bundle, Constants.EDIT_PHONE_NUMBER)
        }

        cv_profile_email.setOnClickListener {
            bundle.clear()
            bundle.putString(
                Constants.EDIT_EMAIL,
                view!!.findViewById<TextView>(R.id.tv_Profile_email).text.toString()
            )
            showBottomSheet(bundle, Constants.EDIT_EMAIL)
        }

        cv_profile_pass.setOnClickListener {
            bundle.clear()
            bundle.putString(
                Constants.EDIT_PASSWORD,
                view!!.findViewById<TextView>(R.id.tv_Profile_password).text.toString()
            )
            showBottomSheet(bundle, Constants.EDIT_PASSWORD)
        }
    }

    private fun showBottomSheet(bundle: Bundle?, tag: String) {
        var bottomSheetFragment: BottomSheet? = BottomSheet()
        bottomSheetFragment?.arguments = bundle
        //            bottomSheetFragment.showNow(childFragmentManager, ProfileFragment::javaClass.name)
        bottomSheetFragment?.show(childFragmentManager, tag)
//        bundle?.clear()

    }


    private fun initDialogPickPhotoSource() {
        dialog = BottomSheetBuilder(context, null)
            .setMode(BottomSheetBuilder.MODE_LIST)
            //                .setMode(BottomSheetBuilder.MODE_GRID)
            .addDividerItem()
            .expandOnStart(true)
            .setDividerBackground(R.color.grey_400)
            .setBackground(R.drawable.ripple_grey)
            .setMenu(R.menu.menu_image_picker)
            .setItemClickListener { item ->
                when (item.itemId) {
                    R.id.chooseFromCamera -> {
                        //EasyImage.openChooserWithGallery(getApplicationContext(), "Ch", int type);
                        easyImage.openCameraForImage(this)
                    }
                    R.id.chooseFromGellery -> {
                        easyImage.openGallery(this)

                    }
                }//                        case R.id.removePhoto:
                //                            removePhoto();
                //                            break;
            }
            .createDialog()
        //        dialog.show();
    }


    @SuppressLint("StringFormatInvalid")
    @AfterPermissionGranted(RC_CAMERA_AND_STORAGE)
    private fun requestCamAndStoragePerms() {
        if (EasyPermissions.hasPermissions(activity!!, *PERMISSIONS)) {
            // Already have permission, do the thing
            if (!dialog?.isShowing!!) {
                initDialogPickPhotoSource()
                dialog!!.show()
            }

        } else {
            // Do not have permissions, request them now
            //            EasyPermissions.requestPermissions(this, getString(R.string.contacts_and_storage_rationale),
            //                    RC_CONTACT_AND_STORAGE, perms);
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, RC_CAMERA_AND_STORAGE, *PERMISSIONS)
                    .setRationale(
                        getString(
                            R.string.cam_and_storage_rationale,
                            getString(R.string.app_name_root)
                        )
                    )
                    .setPositiveButtonText(R.string.rationale_ask_ok)
                    .setNegativeButtonText(R.string.rationale_ask_cancel)
                    //                            .setTheme(R.style.AppTheme)
                    .build()
            )

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this.activity!!,
            object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    val uri = Uri.fromFile(imageFiles[0].file)
                    Picasso.get().load(uri)
                        .into(ivUserPhoto, object : Callback {
                            override fun onSuccess() {
                                //                                Toast.makeText(getActivity(), "Photo updated", Toast.LENGTH_SHORT).show();
                            }

                            override fun onError(e: Exception) {

                            }
                        })
//                    uploadPhoto(uri)
                    uploadPhoto2(uri)


                }
            })
    }

    private fun uploadPhoto2(uri: Uri?) {

        progressDialog = ProgressDialog(context)
        progressDialog!!.setMessage("Uploading...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()


        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        //ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref = storageReference!!.child(UUID.randomUUID().toString())

        ref!!.putFile(uri!!)
            .addOnSuccessListener { taskSnapshot ->
                //                                                progressDialog.dismiss();
                ref!!.downloadUrl.addOnSuccessListener(
                    OnSuccessListener<Uri> { uri ->
                        Log.e(TAG, "uploaded:")
                        val url = uri.toString()
                        RefBase.refAdmins(userAdmin.id!!)
                            .addValueEventListener(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    dataSnapshot.ref.removeEventListener(
                                        this
                                    )
                                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                                        val userAdmin =
                                            dataSnapshot.getValue<UserAdmin>(
                                                UserAdmin::class.java
                                            )
                                        userAdmin!!.profilePhoto = url
                                        dataSnapshot.ref.setValue(userAdmin)
                                        progressDialog!!.dismiss()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })


                    }).addOnFailureListener { e ->

                }
            }
            .addOnFailureListener { e ->
                Toasty.error(
                    Objects.requireNonNull<FragmentActivity>(
                        activity
                    ), Constants.NETWORK_ERROR
                ).show()
            }
            .addOnProgressListener { taskSnapshot ->
                val progress =
                    100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                        .totalByteCount
                progressDialog!!.setMessage("Uploaded " + progress.toInt() + "%")
            }


    }

    private var progressDialog: ProgressDialog? = null

    @SuppressLint("CheckResult")
    private fun uploadPhoto(filePath: Uri?) {


        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        //ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref = storageReference!!.child(UUID.randomUUID().toString())

        if (filePath != null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Uploading...")
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()

//            Log.e("hey_1", "hey_1")
            var file: File? = null
            try {
                file = FileUtils.from(this.activity!!, filePath)
                Log.e("hey_1", "hey_1")

            } catch (e: IOException) {
                e.printStackTrace()
//                Log.e("hey_2", "hey_2")

            }
            if (file == null) {
                Log.e(TAG, "Oooohh sheet")
                return
            }


            val iStream = arrayOf<InputStream>()
            val inputData = arrayOf<ByteArray>()
            // Compress image using RxJava in background thread
            Compressor(context)
                .compressToFileAsFlowable(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ file ->
                    try {
                        iStream[0] =
                            context?.contentResolver?.openInputStream(Uri.fromFile(file))!!
                        inputData[0] = getBytes(iStream[0])
                        if (inputData[0] != null) {
                            Log.e(TAG, "hey_1")
                            //ref.putFile(filePath)
                            //ref = storageReference.child("images/" + UUID.randomUUID().toString());
                            ref!!.putBytes(inputData[0])
                                .addOnSuccessListener { taskSnapshot ->
                                    //                                                progressDialog.dismiss();
                                    ref!!.downloadUrl.addOnSuccessListener(
                                        OnSuccessListener<Uri> { uri ->
                                            Log.e(TAG, "uploaded:")
                                            val url = uri.toString()
                                            RefBase.refAdmins(userAdmin.id!!)
                                                .addValueEventListener(object :
                                                    ValueEventListener {
                                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                        dataSnapshot.ref.removeEventListener(
                                                            this
                                                        )
                                                        if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                                                            val user =
                                                                dataSnapshot.getValue<UserAdmin>(
                                                                    UserAdmin::class.java
                                                                )
                                                            user!!.profilePhoto = url
                                                            dataSnapshot.ref.setValue(user)
                                                            progressDialog!!.dismiss()
                                                        }
                                                    }

                                                    override fun onCancelled(databaseError: DatabaseError) {

                                                    }
                                                })


                                        }).addOnFailureListener { e ->

                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toasty.error(
                                        Objects.requireNonNull<FragmentActivity>(
                                            activity
                                        ), Constants.NETWORK_ERROR
                                    ).show()
                                }
                                .addOnProgressListener { taskSnapshot ->
                                    val progress =
                                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                                            .totalByteCount
                                    progressDialog!!.setMessage("Uploaded " + progress.toInt() + "%")
                                }
                        }


                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }, { throwable ->
                    throwable.printStackTrace()
                    //                            showError(throwable.getBody());
                    Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
//                    Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "hey_2")
                    Log.e(TAG, throwable.localizedMessage)

                })


        }

    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0

//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len)
//        }
//        do{
//            val bytesRead = inputStream.read(buffer)
//        } while(len != -1)
        while (inputStream.read(buffer).also { len = it } >= 0) {
            byteBuffer.write(buffer, 0, len)
        }


        return byteBuffer.toByteArray()
//        return bytesRead.toByteArray()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)


    }


    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (!dialog!!.isShowing) {
            dialog!!.show()
        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

    }

    override fun onRationaleAccepted(requestCode: Int) {
        //        updateProfilePhoto();
    }

    override fun onRationaleDenied(requestCode: Int) {


    }

}
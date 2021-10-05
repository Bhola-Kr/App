package com.dreammedia.dreammedia.dashboard

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.dashboard.dashboard.DashboardFragment
import com.dreammedia.dreammedia.databinding.FragmentEditProfileBinding
import com.dreammedia.dreammedia.model.ProfileResponse
import com.dreammedia.dreammedia.model.UpdateProfileResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import com.dreammedia.dreammedia.utlis.Util
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditProfileFragment : Fragment() {

    lateinit var binding : FragmentEditProfileBinding

    var isCover: Boolean = false

    private lateinit var file: File
    private lateinit var file2: File

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_edit_profile, container, false)
        return binding.root
    }

    val myCalendar = Calendar.getInstance()

    var date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.edtDob.setText(sdf.format(myCalendar.time))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //(activity as DashboardActivity?)!!.appbar!!.visibility    = View.GONE
        Config.init(activity)

        binding.icCross.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.icEditProfile.setOnClickListener {
            isCover = false
            GalleryOnclick()
        }

        binding.icEditCover.setOnClickListener {
               isCover = true
              GalleryOnclick()
        }

        Glide.with(this.requireContext()).load(Config.getUserProfile())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_user_)
                .into(binding.circlerProfilePic)

        /* Glide.with(activity!!).load(Config.getUserCover())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_user_)
                .into(binding.circlerProfilePic) */

        binding.edtDob.setOnClickListener {
            DatePickerDialog(this.requireContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.tvUpdate.setOnClickListener {

            if (TextUtils.isEmpty(binding.editName.text.toString().trim())) {
                binding.editName.setError(resources.getString(R.string.nameLeftEmpty))
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.edtPhone.text.toString().trim())) {
                binding.edtPhone.setError(resources.getString(R.string.mobileLeftEmpty))
                return@setOnClickListener
            } else if ((binding.edtPhone.text.toString().trim().length)!=0) {
                if ((binding.edtPhone.text.toString().trim().length)!=10) {
                    binding.edtPhone.setError("number should be 10 digit")
                    return@setOnClickListener
                }
                return@setOnClickListener
            }else{
                updateUserProfile(
                    Config.getUserProfile() ,
                    Config.getUserCover() ,
                    Config.getUserID() ,
                    binding.editName.text.toString() ,
                    binding.edtUserName.text.toString() ,
                    binding.edtWebsite.text.toString() ,
                    binding.edtWorkDetail.text.toString() ,
                    binding.edtDob.text.toString() ,
                    binding.edtPlace.text.toString() ,
                    binding.edtPhone.text.toString() ,
                    binding.edtBio.text.toString()
                )
            }
        }
           getUserProfile()
    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("Edit Profile")
    }

    private fun getUserProfile() {

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<ProfileResponse> = apiService.getUserProfile(ApiConstant.APIKEY_VALUE, Config.getAccessToken() ,Config.getUserID())

        call.enqueue(object : Callback<ProfileResponse?> {
            override fun onResponse(call: Call<ProfileResponse?>, response: Response<ProfileResponse?>) {

                Log.e("onResponse", "onResponse: "+response.body()!!.responce.get(0).coverPic)
                Log.e("onResponse", "onResponse: "+response.body()!!.responce.get(0).profileImage)

                try {
                    if (response.body()!!.status){

                        Config.init(activity)
                        Config.setUserProfile(response.body()!!.responce.get(0).profileImage)
                        Config.setUserProfile(response.body()!!.responce.get(0).profileImage)
                        Config.setUserCover(response.body()!!.responce.get(0).coverPic)
                        Config.savePreferences()

                        binding.editName.setText(response.body()!!.responce.get(0).fullname)
                        binding.edtUserName.setText(response.body()!!.responce.get(0).username)
                        binding.edtWebsite.setText(response.body()!!.responce.get(0).website)
                        binding.edtWorkDetail.setText(response.body()!!.responce.get(0).workdetail)
                        binding.edtPhone.setText(response.body()!!.responce.get(0).mobile)
                        binding.edtDob.setText(response.body()!!.responce.get(0).dob)
                        binding.edtPlace.setText(response.body()!!.responce.get(0).place)
                        binding.edtBio.setText(response.body()!!.responce.get(0).bio)

                        try {
                            Glide.with(activity!!).load(response.body()!!.responce.get(0).profileImage)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.ic_user_)
                                    .into(binding.circlerProfilePic)

                            (activity as DashboardFragment?)!!.cardAvtar.setImageDrawable(null)
                            Glide.with(activity!!).load(response.body()!!.responce.get(0).profileImage)
                                    .into((activity as DashboardFragment?)!!.cardAvtar)


                        } catch (e: Exception) { }

                        Glide.with(activity!!).load(response.body()!!.responce.get(0).coverPic)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding.coverPic)

                    }else{
                      // Toast.makeText(this@EditProfileFragment, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) { }

            }override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {}
        })
    }

    private fun updateUserProfile(
            prImaf    : String ,
            cover_pic : String,
            user_id   : String,
            fullname  : String,
            username  : String,
            website   : String,
            workdetail : String,
            dob : String,
            place : String,
            phone_no : String,bio : String ) {

        binding.progress.visibility = View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<UpdateProfileResponse> = apiService.updateProfile(ApiConstant.APIKEY_VALUE, Config.getAccessToken() ,
                  prImaf,cover_pic,user_id,fullname,username,website,workdetail,dob,place,phone_no ,bio )
            call.enqueue(object : Callback<UpdateProfileResponse?> {
            override fun onResponse(call: Call<UpdateProfileResponse?>, response: Response<UpdateProfileResponse?>) {

                binding.progress.visibility = View.GONE
                Log.e("onResponse", "onResponse: "+response.body()?.message)
                Toast.makeText(activity , response.body()!!.message , Toast.LENGTH_LONG).show()
                if (response.body()!!.status) {


                } else{
               //  Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<UpdateProfileResponse?>, t: Throwable) {
                binding.progress.visibility = View.GONE
                Log.e("onResponse", "onResponse: "+t.message)
            }
        })
    }

    // open gallery
    private fun GalleryOnclick() {
        // if user declin Deny permission then
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Util.hasReadPermissions(activity) && Util.hasWritePermissions(activity) && Util.hasCameraPermissions(activity)) {
                selectImage()
            } else {
                requestAppPermissions()
            }
        } else {
            selectImage()
        }
    }

    private fun requestAppPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        if (Util.hasReadPermissions(activity) && Util.hasWritePermissions(activity) && Util.hasCameraPermissions(activity)) {
            return
        }
        ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA), 100) // your request code
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != RESULT_CANCELED) {

            Log.e("onActivityResult", "onActivityResult: " + "success" )
          //  Log.e("onActivityResult", "onActivityResult: " + mImageUri)

            when (requestCode) {

                0 -> if (resultCode == RESULT_OK ) {

                    Log.e("onActivityResult", "onActivityResult: " + "0" )

                //    val selectedImageUri = data!!.data
                    val imageEncoded: String = Util.getRealPathFromURI(activity, mImageUri )
                    val selectedImage: Bitmap = BitmapFactory.decodeFile(imageEncoded)

                    if (isCover){
                        binding.coverPic.setImageBitmap(selectedImage)
                       // file2 = File( data.data!!.path.toString())
                        file2 = File(imageEncoded)
                        addUpdateImageCover()
                    }else{
                        binding.circlerProfilePic.setImageBitmap(selectedImage)
                        file = File(imageEncoded)
                        addUpdateImage()
                      }

                }  1 -> if (resultCode == RESULT_OK && data != null) {

                Log.e("onActivityResult", "onActivityResult: " + "1" )

                val selectedImageUri = data!!.data
                val imageEncoded: String = Util.getRealPathFromURI(activity, selectedImageUri)
                val selectedImage: Bitmap = BitmapFactory.decodeFile(imageEncoded)

                if (isCover){
                    file2 = File(imageEncoded)
                    addUpdateImageCover()

                    binding.coverPic.setImageBitmap(selectedImage)

                }else{

                    file = File(imageEncoded)
                    addUpdateImage()
                    Glide.with(this.requireActivity()).load(selectedImage)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.ic_user_)
                            .into(binding.circlerProfilePic)

                }

                /*    val selectedImage = data.data
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        if (selectedImage != null) {
                            val cursor: Cursor? = activity!!.getContentResolver().query(selectedImage, filePathColumn, null, null, null)
                            if (cursor != null) {
                                cursor.moveToFirst()
                                val columnIndex: Int    = cursor.getColumnIndex(filePathColumn[0])
                                val picturePath: String = cursor.getString(columnIndex)
                                binding.circlerProfilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                                cursor.close()

                                file2 = File(picturePath)
                                addUpdateImage()

                            }
                        }*/
                }
            }

        }
    }

    private fun addUpdateImage() {

        Config.init(activity)
        binding.progress.visibility =View.VISIBLE

        val userId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), Config.getUserID())

        val reqFile2    = RequestBody.create("image/png".toMediaType(),file)
        val bodyProfile = MultipartBody.Part.createFormData("profile_image", file.name, reqFile2)

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<UpdateProfileResponse> = apiService.updateProfile(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , bodyProfile , userId )

        call.enqueue(object : Callback<UpdateProfileResponse?> {
             override fun onResponse(call: Call<UpdateProfileResponse?>, response: Response<UpdateProfileResponse?>) {

                Log.e("onResponseprofile", "onResponse: "+response.body()?.status)
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_LONG).show()

                binding.progress.visibility = View.GONE
                if (response.body()!!.status){
                    try {
                        file.delete()
                    } catch (e: Exception) { }
                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }override fun onFailure(call: Call<UpdateProfileResponse?>, t: Throwable) {
                Log.e("onResponse", "onResponse: "+ t.message)

            }
        })
    }

    private fun addUpdateImageCover() {

        Config.init(activity)
        binding.progress.visibility =View.VISIBLE

        val userId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), Config.getUserID())

        val reqFile2    = RequestBody.create("image/png".toMediaType(),file2)
        val bodyProfile = MultipartBody.Part.createFormData("cover_pic", file2.name, reqFile2)

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<UpdateProfileResponse> = apiService.updateColver(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , bodyProfile , userId )

        call.enqueue(object : Callback<UpdateProfileResponse?> {
             override fun onResponse(call: Call<UpdateProfileResponse?>, response: Response<UpdateProfileResponse?>) {

                Log.e("onResponse", "onResponse: "+response.body()?.status)
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_LONG).show()
                binding.progress.visibility = View.GONE

                if (response.body()!!.status){
                    try {
                        file2.delete()
                    } catch (e: Exception) {}
                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            }override fun onFailure(call: Call<UpdateProfileResponse?>, t: Throwable) { Log.e("onResponse", "onResponse: "+ t.message) }
        })
    }

    lateinit var mImageUri : Uri

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        builder.setTitle("Choose your profile picture")
        builder.setItems(options, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, item: Int) {
                if (options[item] == "Take Photo") {

                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.TITLE, "sd")
                    mImageUri = activity?.getContentResolver()?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
                    startActivityForResult(cameraIntent, 0)


/*
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
*/

                } else if (options[item] == "Choose from Gallery") {
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            }
        })
        builder.show()
    }

}
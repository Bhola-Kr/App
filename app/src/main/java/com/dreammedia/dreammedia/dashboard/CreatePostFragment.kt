package com.dreammedia.dreammedia.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.adapter.SelectedPhotoAdapter
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.databinding.FragmentCreatePostBinding
import com.dreammedia.dreammedia.model.PostAddResponse
import com.dreammedia.dreammedia.model.SelectPhotoModel
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import com.dreammedia.dreammedia.utlis.Constant.IMAGE_RESULT
import com.dreammedia.dreammedia.utlis.FilePath
import com.dreammedia.dreammedia.utlis.Util
import com.dreammedia.dreammedia.utlis.Util.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreatePostFragment : Fragment() ,SelectedPhotoAdapter.ClickListener{

    lateinit var binding : FragmentCreatePostBinding
    private val VIDEO_RESULT = 300

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_create_post, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.icImage.setOnClickListener {

           GalleryOnclick()

          //  val intent = Intent()
           // intent.type = "image/*"
           // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
          //  intent.action = Intent.ACTION_GET_CONTENT
          //  startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)

        }

        binding.icVideo.setOnClickListener {
            VideoOnclick()
        }

        binding.icRemoveImg.setOnClickListener {

            binding.lySlectVideo.visibility = View.GONE
            list.clear()

            binding.tvPost.visibility   = View.GONE
            binding.tvNoPost.visibility = View.VISIBLE

        }

        binding.edtPost.addTextChangedListener(textWatcher)

        binding.tvPost.setOnClickListener {

              binding.tvPost.visibility=View.GONE
              binding.tvNoPost.visibility=View.VISIBLE

              addAddPost(binding.edtPost.text.toString())
            //addVideoAddPost(binding.edtPost.text.toString())
        }

        binding.selectedRecycler.setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false))
        binding.selectedRecycler.setHasFixedSize(true)

        adapter = SelectedPhotoAdapter(list ,this@CreatePostFragment, this.requireContext())
        binding.selectedRecycler.adapter = adapter

        Config.init(activity)

        try {
            binding.tvName.setText(Config.getName())
            binding.tvCat.setText(Config.getUserName())
        } catch (e: Exception) { }

        try {
            Glide.with(this.requireActivity()).load(Config.getUserProfile())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_user_)
                .into(binding.avtar)

        } catch (e: Exception) { }

    }

     var adapter: SelectedPhotoAdapter?  = null
    var list: MutableList<SelectPhotoModel>        = mutableListOf()

    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            Log.e("start", "onTextChanged: " +s)
            if(TextUtils.isEmpty(s.toString())){

                if (list.size == 0){
                    binding.tvPost.visibility   = View.GONE
                    binding.tvNoPost.visibility = View.VISIBLE
                }

              //  binding.tvPost.setBackgroundResource(R.drawable.drawable_edit_round_corner_solid)
              //  binding.tvPost.setTextColor(resources.getColor(R.color.gray_8))

            }else{
                binding.tvPost.visibility = View.VISIBLE
                binding.tvNoPost.visibility = View.GONE

              //  binding.tvPost.setBackgroundResource(R.drawable.drawable_rectangle_corner_solid)
              //  binding.tvPost.setTextColor(resources.getColor(R.color.white))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.hide()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.GONE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("")

    }

    // open gallery
    private fun GalleryOnclick() {
        // if user declin Deny permission then
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (hasReadPermissions(activity) && hasWritePermissions(activity) && hasCameraPermissions(activity)) {
                startActivityForResult(getPickImageChooserIntent(this.requireContext()),IMAGE_RESULT)

            } else {
                requestAppPermissions()
            }
        } else {
                startActivityForResult(getPickImageChooserIntent(this.requireContext()), IMAGE_RESULT)
        }
    }

    // open gallery
    private fun VideoOnclick() {
        // if user declin Deny permission then
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (hasReadPermissions(activity) && hasWritePermissions(activity) && hasCameraPermissions(activity)) {

                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, VIDEO_RESULT)

            } else {
                requestAppPermissions()
            }
        } else {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, VIDEO_RESULT)
        }
    }

    private fun requestAppPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        if (hasReadPermissions(activity) && hasWritePermissions(activity) && hasCameraPermissions(activity)) {
            return
        }
        ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA), 100) // your request code
    }

    // onActivity result for all camera and video intents
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) when (requestCode) {
            IMAGE_RESULT -> {
/*
                var filePath = getImageFilePath(data, this.requireContext())
                //Log.e("hello", "onActivityResult: $filePath")

                if (filePath == null) {
                    filePath = getPath(activity , data?.data )
                }*/
                Log.e("videoPath123", "onActivityResult: " + data?.data )

                 if (data?.data != null) {
                 //  Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

                     binding.tvPost.visibility = View.VISIBLE
                     binding.tvNoPost.visibility = View.GONE

                     if (isVideo == true && list.size != 0)
                         list.clear()

                     binding.lySlectVideo.visibility        = View.GONE
                     binding.selectedRecycler.visibility = View.VISIBLE

                     val selectedImageUri = data!!.data
                     var filePath = getImageFilePath(data, this.requireContext())

                     if (filePath == null) {
                         filePath = FilePath.getPath(this.context, data.data)
                     }

                  // val imageEncoded: String = getRealPathFromURI(activity, selectedImageUri )
                     val selectedImage: Bitmap = BitmapFactory.decodeFile(filePath)

              //    /storage/emulated/0/Android/data/com.dreammedia.dreammedia/files/patient_data/IMG_1614259035816.png
                     Log.e("videoPath", "onActivityResult: " + filePath )

                     val vv    = SelectPhotoModel()
                     val myUri = Uri.parse(filePath)
                     vv.path   = myUri;
                     list.add(vv)

                     adapter!!.notifyDataSetChanged()

                     isVideo = false

                     //file = File(imageEncoded)

                     // image.setImageBitmap(selectedImage)
                     /*   //add images in list
                     */
                } else {
                    Toast.makeText(activity, "image not found.", Toast.LENGTH_SHORT).show()
                }

            } VIDEO_RESULT ->{

                binding.tvPost.visibility = View.VISIBLE
                binding.tvNoPost.visibility = View.GONE

                var selectedVideoUri = data!!.data
               // var imageEncoded: String? = geVideotRealPathFromURI(selectedVideoUri)
                var imageEncoded: String? = Util.getPath(selectedVideoUri , activity)

                Log.e("videoPath", "onActivityResult: " + imageEncoded )
                binding.lySlectVideo.visibility        = View.VISIBLE
                binding.selectedRecycler.visibility = View.GONE

                // Log.v(TAG, uri.toString());
                binding.videoView.setVideoURI(imageEncoded?.toUri())
                binding.videoView.requestFocus()
                binding.videoView.start()

                if (list.size != 0)
                    list.clear()

                val vv    = SelectPhotoModel()
                val myUri = Uri.parse(imageEncoded)
                vv.path   = myUri;
                list.add(vv)

                isVideo = true

            }
        }
    }



    fun geVideotRealPathFromURI(contentUri: Uri?): String? {
        var path: String? = null
        val proj = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor: Cursor? = activity?.getContentResolver()?.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            path = cursor.getString(column_index)
        }
        cursor!!.close()
        return path
    }

    private lateinit var file: File
    var isVideo: Boolean = false

    private fun addAddPost(desc :String) {

        Config.init(activity)
        binding.progress.visibility =View.VISIBLE

        Log.e("uploadFile", "addAddPost: "+ list.size )
        val name: RequestBody   = RequestBody.create("text/plain".toMediaTypeOrNull(), desc)
        val userId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), Config.getUserID())

        val imageListBody = arrayOfNulls<MultipartBody.Part>(list.size)
        //send multiple image with multipart
        for (i in 0 until list.size) {
            val cc      = list.get(i).path.toString()
            val file    = File(cc)
            Log.e("sdasda", "addAddPost: " +list.get(i).path.toString() )

            var reqFile :RequestBody

            if (isVideo){
                 reqFile = RequestBody.create("video/Mp4".toMediaType(), file)
            }else{
                 reqFile = RequestBody.create("image/png".toMediaType(), file)
            }

            imageListBody[i] = MultipartBody.Part.createFormData("userPhoto", file.name, reqFile)
        }

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<PostAddResponse> = apiService.addPost(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , imageListBody ,  userId  , name)

        call.enqueue(object : Callback<PostAddResponse?> {
            override fun onResponse(call: Call<PostAddResponse?>, response: Response<PostAddResponse?>) {

                Log.e("onResponse", "onResponse: "+response.body()?.status)
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()

                binding.progress.visibility = View.GONE
                if (response.body()!!.status){

                   // list.clear()
                    binding.edtPost.setText("")
                    activity!!.onBackPressed()

                }else{
                  //  Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<PostAddResponse?>, t: Throwable) {
                Log.e("onResponse", "onResponse: "+ t.message)

            }
        })
    }

    private fun addVideoAddPost(desc :String) {

        Config.init(activity)
        binding.progress.visibility =View.VISIBLE

        Log.e("uploadFile", "addAddPost: "+ list.size )
        val name: RequestBody   = RequestBody.create("text/plain".toMediaTypeOrNull(), desc)
        val userId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), Config.getUserID())

      //  val reqFile = RequestBody.create("video/*".toMediaType(), file)
      // imageListBody[0] = MultipartBody.Part.createFormData("userPhoto", file.name, reqFile)

        Log.e("onResponse", "onResponse: "+ file.name)
        Log.e("onResponse", "onResponse: "+ file)

        val reqFile = file.asRequestBody("video/*".toMediaType())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("userPhoto", file.name, reqFile)

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<PostAddResponse> = apiService.addVideoPost(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , body ,  userId  , name)

        call.enqueue(object : Callback<PostAddResponse?> {
            override fun onResponse(call: Call<PostAddResponse?>, response: Response<PostAddResponse?>) {

                Log.e("onResponse", "onResponse: "+response.body()?.status)
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()

                binding.progress.visibility = View.GONE
                if (response.body()!!.status){

                   // list.clear()
                    binding.edtPost.setText("")
                    activity!!.onBackPressed()

                }else{
                  //  Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<PostAddResponse?>, t: Throwable) {
                Log.e("onResponse", "onResponse: "+ t.message)

            }
        })
    }

    override fun onClickListener(pos: Int, id: Int, mlist: SelectPhotoModel) {

        if (R.id.imgcard ==  id){


        }else if(R.id.icRemoveImg ==  id){

            list.removeAt(pos)
            adapter!!.notifyDataSetChanged()

            if (list.size == 0)
            binding.tvPost.visibility = View.GONE
            binding.tvNoPost.visibility = View.VISIBLE

        }

    }

}
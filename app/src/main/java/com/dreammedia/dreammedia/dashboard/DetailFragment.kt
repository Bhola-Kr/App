package com.dreammedia.dreammedia.dashboard

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.databinding.FragmentDetailBinding
import com.dreammedia.dreammedia.model.DeletePostResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import com.dreammedia.dreammedia.utlis.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    lateinit var binding : FragmentDetailBinding
    var MP: MediaPlayer? = null
    private var flag     = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     // binding.imageView.setImage(ImageSource.resource(R.drawable.tiger));

        val type: String          = requireArguments().getString("type").toString()
        val imageVideoUrl: String = requireArguments().getString("image").toString()
        val desc: String          = requireArguments().getString("desc").toString()

        if(type.equals("video")){

            val postID: String                  = requireArguments().getString("postID").toString()
            try {
                val videoCount: String          = requireArguments().getString("videoCount").toString()
                addVideoCount(postID)

                if (TextUtils.isEmpty(videoCount)){
                    binding.tvView.setText("0")
                }else{
                    binding.tvView.setText(videoCount)
                }
            } catch (e: Exception) { }

            val mediaController = MediaController(this.requireActivity())
            mediaController.setAnchorView(binding.DetailvideoView)
            binding.DetailvideoView.setMediaController(mediaController)

            binding.image.visibility            = View.GONE
            binding.tvScroll.visibility         = View.GONE
            binding.icAudio.visibility          = View.VISIBLE
            binding.tvView.visibility           = View.VISIBLE
            binding.DetailvideoView.visibility  = View.VISIBLE

            binding.DetailvideoView.setOnPreparedListener(OnPreparedListener { mp ->
                MP = mp
            })

            Log.e("onPrepared", "onPrepared: " + "onPrepair2")
            binding.DetailvideoView.setVideoURI(Uri.parse(imageVideoUrl))
            binding.DetailvideoView.start()

        }else if (type.equals("image") || type.equals("image_template")){

            binding.image.visibility            = View.VISIBLE
            binding.tvScroll.visibility         = View.GONE
            binding.DetailvideoView.visibility  = View.GONE
            binding.icAudio.visibility          = View.GONE
            binding.tvView.visibility           = View.GONE

            Glide.with(this).load(imageVideoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image)

            if (type.equals("image_template")){

                binding.lybottm.visibility           = View.VISIBLE

                binding.tvShare.setOnClickListener {

                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, imageVideoUrl)
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)

                }

                binding.tvDownload.setOnClickListener {


                    try {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (Util.hasReadPermissions(activity) && Util.hasWritePermissions(activity) && Util.hasCameraPermissions(activity)) {


                                        val r = DownloadManager.Request(Uri.parse(imageVideoUrl))
                                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName")
                                        r.allowScanningByMediaScanner()
                                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                        val dm = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                                        dm.enqueue(r)


                            } else {
                                requestAppPermissions()
                            }
                        } else {

                            val r = DownloadManager.Request(Uri.parse(imageVideoUrl))
                            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName")
                            r.allowScanningByMediaScanner()
                            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            val dm = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            dm.enqueue(r)

                        }

                    } catch (e: Exception) { }
                }

                
            }else{
                binding.lybottm.visibility           = View.GONE
            }

        }else{

            binding.tvScroll.visibility         = View.VISIBLE
            binding.image.visibility            = View.GONE
            binding.DetailvideoView.visibility  = View.GONE
            binding.icAudio.visibility          = View.GONE
            binding.tvView.visibility           = View.GONE

            binding.tvAutoFit.setText(desc)

        }

        binding.icAudio.setOnClickListener {
        try {
                if (flag == 0) {
                    mute()
                } else {
                    unMute()
                }
            } catch (e: Exception) {}
        }

        binding.icPlay.setOnClickListener {
            try {
                binding.icPlay.setVisibility(View.GONE)
                binding.DetailvideoView.start()
            } catch (e: Exception) {
            }
        }

        binding.DetailvideoView.setOnClickListener {
           // binding.icPlay.setVisibility(View.VISIBLE)
           // binding.DetailvideoView.pause()
        }

    }

    private fun requestAppPermissions() {
        ActivityCompat.requestPermissions(activity!!, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        ), 100) // your request code
    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.hide()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.GONE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("")

    }

    private fun mute() {
        try {
            flag = 1
            binding.icAudio.setImageResource(R.drawable.ic_mute)
            MP!!.setVolume(0f, 0f)
            MP!!.isLooping = true
        } catch (e: Exception) {
        }
    }

    private fun unMute() {
        try {
            flag = 0
            binding.icAudio.setImageResource(R.drawable.ic_speaker)
            MP!!.setVolume(1.0f, 1.0f)
            MP!!.isLooping = true
        } catch (e: Exception) {
        }

    }


    private fun addVideoCount(postId : String) {

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DeletePostResponse> = apiService.addVideoCount(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , postId )

        call.enqueue(object : Callback<DeletePostResponse?> {
            override fun onResponse(call: Call<DeletePostResponse?>, response: Response<DeletePostResponse?>) {

                Log.e("onResponse", "onResponse: "+response.body()?.status)
                if (response.body()!!.status){
                }else{
                   // Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {}
        })
    }

}
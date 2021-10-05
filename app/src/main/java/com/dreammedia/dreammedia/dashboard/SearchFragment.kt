package com.dreammedia.dreammedia.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.adapter.SearchWhoToFollowAdapter
import com.dreammedia.dreammedia.adapter.WhoToFollow2Adapter
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.dashboard.dashboard.MyPostLIstAdapter2
import com.dreammedia.dreammedia.databinding.FragmentSearchBinding
import com.dreammedia.dreammedia.model.*
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import com.dreammedia.dreammedia.utlis.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment()  , MyPostLIstAdapter2.ClickListener ,SearchWhoToFollowAdapter.ClickListener  ,WhoToFollow2Adapter.ClickListener{

    lateinit var binding : FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_search, container, false)
        return binding.root
    }

    var isPostSelect = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tcClear.setOnClickListener {

            try {
                adapter!!.clear()
                adapterUser!!.clear()
            } catch (e: Exception) { }

            binding.edtSearch.setText("")

        }

        binding.searchPost.setTextColor(resources.getColor(R.color.bold_text))
        binding.searchUser.setTextColor(resources.getColor(R.color.white))
        binding.searchUser.setBackgroundResource(R.drawable.drawable_rectangle_corner_solid)
        binding.searchPost.setBackgroundResource(R.drawable.drawable_rectangle_corner_boarder)
        isPostSelect = false
        binding.searchPostRecycler.visibility = View.GONE
        binding.searchUserRecycler.visibility = View.VISIBLE

        binding.searchPost.setOnClickListener {
            binding.searchPost.setTextColor(resources.getColor(R.color.white))
            binding.searchUser.setTextColor(resources.getColor(R.color.bold_text))
            binding.searchPost.setBackgroundResource(R.drawable.drawable_rectangle_corner_solid)
            binding.searchUser.setBackgroundResource(R.drawable.drawable_rectangle_corner_boarder)
            isPostSelect = true

            binding.searchPostRecycler.visibility = View.VISIBLE
            binding.searchUserRecycler.visibility = View.GONE
        }

        binding.searchUser.setOnClickListener {
            binding.searchPost.setTextColor(resources.getColor(R.color.bold_text))
            binding.searchUser.setTextColor(resources.getColor(R.color.white))

            binding.searchUser.setBackgroundResource(R.drawable.drawable_rectangle_corner_solid)
            binding.searchPost.setBackgroundResource(R.drawable.drawable_rectangle_corner_boarder)
            isPostSelect = false
            binding.searchPostRecycler.visibility = View.GONE
            binding.searchUserRecycler.visibility = View.VISIBLE
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.e("sdsd", "onTextChanged: " + s.toString() )
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (isPostSelect){
                    getPOsts(s.toString())
                }else{
                    getUsers(s.toString())
                }
            }
        })

        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.searchPostRecycler.setLayoutManager(linearLayoutManager)
        binding.searchPostRecycler.setHasFixedSize(true)

        linearLayoutManager2 = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.searchUserRecycler.setLayoutManager(linearLayoutManager2)
        binding.searchUserRecycler.setHasFixedSize(true)

    }

    private var linearLayoutManager: LinearLayoutManager? = null
    private var linearLayoutManager2: LinearLayoutManager? = null
    var list: MutableList<DashBoardUserMainResponse.Post>    = ArrayList()
    var adapter: MyPostLIstAdapter2?       = null
    var adapterUser: SearchWhoToFollowAdapter?   = null

    var listUser: MutableList<WhoToFollowResponse.Responce>        = ArrayList()

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.hide()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.GONE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("")

    }

    var mToast: Toast? = null

    fun showAToast(message: String?) {
        if (mToast != null) {
            mToast!!.cancel()
        }
        mToast = Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT)
        mToast!!.show()
    }

    private fun getPOsts(serch : String ) {

        Config.init(activity)
      // binding.progress.visibility = View.VISIBLE
         val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)


         val call: Call<SearchPostResponse> = apiService.searchPostApi(ApiConstant.APIKEY_VALUE,
                 Config.getAccessToken(), Config.getUserID() , "1" ,serch ,"post")

        call.enqueue(object : Callback<SearchPostResponse?> {
             override fun onResponse(call: Call<SearchPostResponse?>, response: Response<SearchPostResponse?>) {

                 try {

                     if (response.body()!!.responce.size == 0){
                         showAToast("No data found")
                     }

                     try {
                         list.clear()
                         adapter?.clear()
                     } catch (e: Exception) {
                     }

                     for (i in 0 until response.body()!!.responce.size) {

                         val vv: DashBoardUserMainResponse.Post = DashBoardUserMainResponse.Post()
                         vv.userId       = response.body()!!.responce.get(i).userId
                         vv.postId       = response.body()!!.responce.get(i).postId
                         vv.images       = response.body()!!.responce.get(i).images
                         vv.description  = response.body()!!.responce.get(i).description
                         vv.isDownload   = response.body()!!.responce.get(i).isDownload
                         vv.profileImage = response.body()!!.responce.get(i).profileImage
                         vv.fullname     = response.body()!!.responce.get(i).fullname
                         vv.totalLike    = response.body()!!.responce.get(i).totalLike
                         vv.postImage    = response.body()!!.responce.get(i).postImage
                         vv.totalShare   = response.body()!!.responce.get(i).totalShare
                         vv.is_like      = response.body()!!.responce.get(i).is_like
                         vv.added      = response.body()!!.responce.get(i).added

                         list.add(vv)

                     }

                     adapter = MyPostLIstAdapter2(activity ,Config.getUserID(),this@SearchFragment, this@SearchFragment)
                     binding.searchPostRecycler.adapter = adapter

                     adapter!!.addAll(list)

                 } catch (e: Exception) { }

             }override fun onFailure(call: Call<SearchPostResponse?>, t: Throwable) { }
        })
    }

    private fun getUsers(serch : String ) {

        Config.init(activity)
       // binding.progress.visibility = View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<WhoToFollowResponse> = apiService.searchUserApi(ApiConstant.APIKEY_VALUE,
                Config.getAccessToken(), Config.getUserID() , "1" ,serch ,"user")

        call.enqueue(object : Callback<WhoToFollowResponse?> {
             override fun onResponse(call: Call<WhoToFollowResponse?>, response: Response<WhoToFollowResponse?>) {

                 try {

                     Log.e("seARCuSER", "onFailure: "+response.body()!!.message )

                     if (response.body()!!.responce.size == 0){
                         showAToast("No data found")
                     }

                     listUser = response.body()!!.responce

                     for (i in 0 until listUser.size) {

                         Log.e("getIs_follow", "onResponse: "+listUser.get(i).getIs_follow())
                         if (listUser.get(i).getIs_follow() == 0){
                             listUser.get(i).setmFllowing(false)
                         }else{
                             listUser.get(i).setmFllowing(true)
                         }
                     }

                     adapterUser = SearchWhoToFollowAdapter(listUser ,this@SearchFragment, activity!!)
                     binding.searchUserRecycler.adapter = adapterUser

                 } catch (e: Exception) { }

             }override fun onFailure(call: Call<WhoToFollowResponse?>, t: Throwable) {

                Log.e("seARCuSER", "onFailure: "+t.message )
            }
        })
    }

    private fun likePost(postId : String ) {

        // binding.progress.visibility =View.VISIBLE
        Log.e("likePost", "onResponse: "+postId)
        Log.e("likePost", "onResponse: "+Config.getUserID())

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DeletePostResponse> = apiService.likePost(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),Config.getUserID() ,postId)

        call.enqueue(object : Callback<DeletePostResponse?> {
            override fun onResponse(call: Call<DeletePostResponse?>, response: Response<DeletePostResponse?>) {
                Log.e("likePost", "onResponse: "+response.body()?.message)
                //  binding.progress.visibility =View.GONE
                if (response.body()!!.status){


                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {}
        })
    }

    private fun deletePost(postId : String , pos : Int) {

        binding.progress.visibility =View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DeletePostResponse> = apiService.deletePost(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),postId)

        call.enqueue(object : Callback<DeletePostResponse?> {
            override fun onResponse(call: Call<DeletePostResponse?>, response: Response<DeletePostResponse?>) {
                Log.e("FollowResponse", "onResponse: "+response.body()?.message)
                binding.progress.visibility =View.GONE
                if (response.body()!!.status){

                    adapter!!.removeItem(pos)
                    adapter!!.notifyDataSetChanged()

                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {}
        })
    }

    override fun onClickListener(pos: Int, id: Int, mlist: WhoToFollowResponse.Responce) {

        if (id ==  R.id.imgCard){

            val vv = Bundle()
            vv.putString("key", mlist.id.toString())
            findNavController().navigate(R.id.action_SearchFragment_to_ProfileFragment, vv)

        }else if (id == R.id.tvFollow){

            if (mlist!!.getmFllowing()){
                mlist?.setmFllowing(false)
                adapterUser?.notifyDataSetChanged()
                followRemove( Config.getUserID() , mlist!!.id.toString())
            }else{
                mlist?.setmFllowing(true)
                adapterUser?.notifyDataSetChanged()
                addFollow( Config.getUserID() , mlist.id.toString())
            }

        }
    }

    private fun followRemove(userId : String , receiverId : String) {

        binding.progress.visibility =View.VISIBLE
        Log.e("onResponse", "onResponse: "+Config.getAccessToken())

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<AddFollowResponse> = apiService.followRemove(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),userId,receiverId)

        call.enqueue(object : Callback<AddFollowResponse?> {
            override fun onResponse(call: Call<AddFollowResponse?>, response: Response<AddFollowResponse?>) {

                Log.e("FollowResponse", "onResponse: "+response.body()?.message)
                binding.progress.visibility =View.GONE
                if (response.body()!!.status){
                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<AddFollowResponse?>, t: Throwable) {}
        })
    }


    private fun addFollow(userId : String , receiverId : String) {

        //  binding.progress.visibility =View.VISIBLE
        Log.e("onResponse", "onResponse: "+Config.getAccessToken())

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<AddFollowResponse> = apiService.addFollow(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),userId,receiverId)

        call.enqueue(object : Callback<AddFollowResponse?> {
            override fun onResponse(call: Call<AddFollowResponse?>, response: Response<AddFollowResponse?>) {

                Log.e("FollowResponse", "onResponse: "+response.body()?.message)
                //  binding.progress.visibility =View.GONE
                if (response.body()!!.status){

                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<AddFollowResponse?>, t: Throwable) {}
        })
    }

    override fun onItemClick(pos: Int, id: Int, mlist: DashBoardUserMainResponse.Post?) {

        if (id == R.id.avtard) {

            if (!Config.getUserID().equals(mlist!!.userId)){
                val vv = Bundle()
                vv.putString("key", mlist.userId.toString())
                findNavController().navigate(R.id.action_SearchFragment_to_ProfileFragment, vv)
            }

        } else if (id == R.id.item_report) {

            Log.e("item_report", "onClickListener: " + "report" )
            val vv = Bundle()
            vv.putString("postId", mlist!!.postId.toString())
            findNavController().navigate(R.id.action_SearchFragment_to_RepoartFragment,vv)

        } else if (id == R.id.item_delete) {

            deletePost(mlist!!.postId.toString() , pos)
            Log.e("item_report", "onClickListener: " + "item_delete" )

        }else if (id == R.id.item_sharePost) {

            sharePost(Config.getUserID(), mlist!!.postId.toString())

        }else if (id == R.id.pager) {

            Log.e("item_report", "onClickListener: " + "item_delete" )
            val vv = Bundle()
            if (mlist!!.type == "video") {
                vv.putString("image", mlist.video_url)
                vv.putString("type", "video")
            } else {
                vv.putString("image", mlist.postImage[pos])
                vv.putString("type", "image")
            }
            vv.putString("desc", "")
            vv.putString("postID", mlist.postId.toString())
            vv.putString("videoCount", mlist.is_video)
            findNavController().navigate(R.id.action_SearchFragment_to_DetailFragment,vv)

        } else if (id == R.id.tvAutoFit) {
            val vv = Bundle()
            vv.putString("image", "")
            vv.putString("desc", mlist!!.description)
            vv.putString("type", "text")
            vv.putString("postID", mlist.postId.toString())
            vv.putString("videoCount", mlist.is_video)
            findNavController().navigate(R.id.action_SearchFragment_to_DetailFragment,vv)

        }else if (id == R.id.icLike) {
            try {
                if (mlist!!.is_like == 0) {
                    likePost(mlist.postId.toString())
                } else {
                    likeRemove(mlist.postId.toString())
                }
            } catch (e: Exception) { }
        }else if (id == R.id.icShare) {

            try {

                var img = ""

                val sb = StringBuilder()
                for (i in mlist!!.postImage.indices) {
                    sb.append(mlist!!.postImage[i])
                    sb.append("\n")
                }

                img = sb.toString()
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, """Shared frome Dream Media${mlist.description}  $img""")
                sendIntent.type = "text/plain"
                startActivity(sendIntent)

            } catch (e: Exception) { }

        }else if (id == R.id.tvDownload) {
            // DownloadImageFromPath(mlist?.postImage?.get(0))
            try {
                var path: String? = ""


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (Util.hasReadPermissions(activity) && Util.hasWritePermissions(activity) && Util.hasCameraPermissions(activity)) {

                        if (mlist!!.type == "video") {
                            mlist.video_url
                            val r = DownloadManager.Request(Uri.parse(path))
                            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName")
                            r.allowScanningByMediaScanner()
                            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            val dm = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            dm.enqueue(r)

                        } else {

                            Log.e("onItemClick", "onItemClick: " +mlist?.postImage.indices )
                            for (i in mlist?.postImage.indices) {

                                path = mlist.postImage[i]

                                val r = DownloadManager.Request(Uri.parse(path))
                                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName")
                                r.allowScanningByMediaScanner()
                                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                val dm = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                                dm.enqueue(r)
                            }
                        }
                    } else {
                        requestAppPermissions()
                    }
                } else {

                    if (mlist!!.type == "video") {
                        mlist.video_url
                        val r = DownloadManager.Request(Uri.parse(path))
                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName")
                        r.allowScanningByMediaScanner()
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        val dm = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        dm.enqueue(r)

                    } else {

                        Log.e("onItemClick", "onItemClick: " +mlist?.postImage.indices )
                        for (i in mlist?.postImage.indices) {

                            path = mlist.postImage[i]

                            val r = DownloadManager.Request(Uri.parse(path))
                            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName")
                            r.allowScanningByMediaScanner()
                            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            val dm = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            dm.enqueue(r)
                        }
                    }
                }

            } catch (e: Exception) { }
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun requestAppPermissions() {
        ActivityCompat.requestPermissions(activity!!, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        ), 100) // your request code
    }

    override fun onClickListener(pos: Int, id: Int, list: DashBoardResponse.Randomuser) {


    }

    private fun likeRemove(postId : String ) {

        // binding.progress.visibility =View.VISIBLE
        Log.e("likePost", "onResponse: "+postId)
        Log.e("likePost", "onResponse: "+Config.getUserID())

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DeletePostResponse> = apiService.likeRemove(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),Config.getUserID() ,postId)

        call.enqueue(object : Callback<DeletePostResponse?> {
            override fun onResponse(call: Call<DeletePostResponse?>, response: Response<DeletePostResponse?>) {
                Log.e("likePost", "onResponse: "+response.body()?.message)
                //  binding.progress.visibility =View.GONE
                if (response.body()!!.status){


                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {}
        })
    }

    private fun sharePost(userId : String , postIds : String ) {

        // binding.progress.visibility =View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<PostAddResponse> = apiService.sharePost(ApiConstant.APIKEY_VALUE, Config.getAccessToken(), userId,postIds )

        call.enqueue(object : Callback<PostAddResponse?> {
            override fun onResponse(call: Call<PostAddResponse?>, response: Response<PostAddResponse?>) {
                Log.e("likePost", "onResponse: "+response.body()?.message)
                //  binding.progress.visibility =View.GONE
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()

            }override fun onFailure(call: Call<PostAddResponse?>, t: Throwable) {}
        })
    }

}
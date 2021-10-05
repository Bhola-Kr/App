package com.dreammedia.dreammedia.dashboard

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.adapter.SugestFollowrsdapter
import com.dreammedia.dreammedia.adapter.WhoToFollow2Adapter
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.dashboard.dashboard.MyPostLIstAdapter2
import com.dreammedia.dreammedia.dashboard.dashboard.utils.PaginationScrollListener
import com.dreammedia.dreammedia.databinding.FragmentProfileBinding
import com.dreammedia.dreammedia.model.*
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
class ProfileFragment : Fragment(), MyPostLIstAdapter2.ClickListener,
    SugestFollowrsdapter.ClickListener, WhoToFollow2Adapter.ClickListener {

    lateinit var binding: FragmentProfileBinding
    var adapter: MyPostLIstAdapter2? = null

    var adapterSuggest: SugestFollowrsdapter? = null
    var listSuggestUSer: MutableList<DashBoardUserMainResponse.Alluser> = ArrayList()

    var userId: String? = null

    var PAGE_START = 1
    var isLoading = false
    var isLastPage = false
    var TOTAL_PAGES = 1
    public var PAGE_SIZE = 6
    var currentPage = PAGE_START
    val bundle: Bundle? = null
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    var flag: Int = 0

    var followrsCOunt: String = ""
    var followingCOunt: String = ""
    var userName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recycler.setLayoutManager(linearLayoutManager)
        binding.recycler.setHasFixedSize(true)
        adapter = MyPostLIstAdapter2(
            activity,
            Config.getUserID(),
            this@ProfileFragment,
            this@ProfileFragment
        )

        //  adapter = DashboardAdapter(list ,this@ProfileFragment, this.requireContext())
        //  binding.recycler.adapter = adapter
        binding.recycler.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {

            override fun loadMoreItems() {

                this@ProfileFragment.isLoading = true
                currentPage += 1
                getUserProfileNexttPage()
                Log.e("currentPage", "loadMoreItems: $currentPage")

            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return this@ProfileFragment.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@ProfileFragment.isLoading
            }
        })


        // binding.recycler.isNestedScrollingEnabled = false

        binding.suggestUserRecycler.setLayoutManager(
            LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        binding.suggestUserRecycler.setHasFixedSize(true)
        binding.suggestUserRecycler.isNestedScrollingEnabled = false

        Config.init(activity)

        binding.follwVisiblityCard.setOnClickListener {
            if (flag == 0) {
                binding.lySuggest.visibility = View.VISIBLE
                flag = 1
            } else {
                binding.lySuggest.visibility = View.GONE
                flag = 0
            }
        }


        try {

            try {
                userId = requireArguments().getString("key")
                Log.e("string", "onViewCreated: " + userId)
            } catch (e: Exception) {
            }

            if (TextUtils.isEmpty(userId)) {
                userId = Config.getUserID()
            }

            if (userId.equals(Config.getUserID())) {

                binding.editProfile.visibility = View.VISIBLE
                binding.lyCenterCreatPost.visibility = View.VISIBLE

                binding.FollowLy.visibility = View.GONE
                binding.lySuggest.visibility = View.GONE

                binding.tvMobile.visibility = View.VISIBLE
                binding.tvEmail.visibility = View.VISIBLE

            } else {

                binding.editProfile.visibility = View.GONE
                binding.lyCenterCreatPost.visibility = View.GONE

                binding.FollowLy.visibility = View.VISIBLE
                binding.lySuggest.visibility = View.VISIBLE

                binding.tvMobile.visibility = View.GONE
                binding.tvEmail.visibility = View.GONE

            }

        } catch (e: Exception) {
        }

        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.createPost.setOnClickListener {
            findNavController().navigate(R.id.action_ProfileFragment_to_CreatePostFragment)
        }
        binding.caerdImage.setOnClickListener {
            findNavController().navigate(R.id.action_ProfileFragment_to_CreatePostFragment)
        }

        binding.tvNavigation.setOnClickListener {
            (activity as DashboardActivity?)!!.drawerLayout.openDrawer(Gravity.RIGHT)
        }

        binding.tvNotification.setOnClickListener {
            findNavController().navigate(R.id.action_ProfileFragment_to_NotificationFragment)
        }

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_ProfileFragment_to_SearchFragment)
        }

        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_ProfileFragment_to_EditProfileFragment)
        }

        binding.lyFollowes.setOnClickListener {
            var bb = Bundle()
            bb.putString("id", "follow")
            bb.putString("userid", userId)
            bb.putString("followrsCOunt", followrsCOunt)
            bb.putString("followingCOunt", followingCOunt)
            bb.putString("userName", userName)
            findNavController().navigate(R.id.action_ProfileFragment_to_TabFragment, bb)
        }

        binding.lyFollowing.setOnClickListener {
            var bb = Bundle()
            bb.putString("id", "following")
            bb.putString("userid", userId)
            bb.putString("followingCOunt", followingCOunt)
            bb.putString("followrsCOunt", followrsCOunt)
            bb.putString("userName", userName)
            findNavController().navigate(R.id.action_ProfileFragment_to_TabFragment, bb)
        }

        binding.lyPost.setOnClickListener {
            binding.nestedScroll.scrollBy(0, binding.lyTopMain.getHeight());
        }

        binding.tvViewMore.setOnClickListener {

            if (myFlag == 0) {
                myFlag = 1
                binding.tvViewMore.setText("Show less")
                binding.lyMoreDetrail.visibility = View.VISIBLE
            } else {
                myFlag = 0
                binding.tvViewMore.setText("See your About Info")
                binding.lyMoreDetrail.visibility = View.GONE
            }
        }

        binding.tvViewAllFollow.setOnClickListener {

            val vv = Bundle()
            vv.putString("key", "home")
            findNavController().navigate(
                R.id.action_DashboardFragment_to_WhoToFollowDashBoardFragment,
                vv
            )

        }

        getUserProfileFirstPage()

        binding.mySwipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            TOTAL_PAGES = 1
            isLastPage = false
            adapter!!.clear()
            getUserProfileFirstPage()
            binding.mySwipeRefreshLayout.isRefreshing = false
        }


        binding.tvProfileFollow.setOnClickListener {

            if (binding.tvProfileFollow.text.toString().equals("Follow")) {

                binding.tvProfileFollow.setText("UnFollow")
                addFollow(Config.getUserID(), userId.toString())
            } else {

                followRemove(Config.getUserID(), userId.toString())

            }
        }

    }

    var myFlag: Int = 0

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.hide()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("")

        currentPage = 1
        TOTAL_PAGES = 1
        isLastPage = false

        try {
            binding.tvFollowrsProfile.setText(followrsCOunt)
            binding.tvFollowingProfile.setText(followingCOunt)
        } catch (e: Exception) {
        }

    }

    private fun fetchResults(response: Response<DashBoardUserMainResponse?>): List<DashBoardUserMainResponse.Post> {
        val response1 = response.body()
        return response1!!.responce.posts
    }

    private fun getUserProfileFirstPage() {

        binding.progress.visibility = View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DashBoardUserMainResponse> = apiService.getUserMain(
            ApiConstant.APIKEY_VALUE,
            Config.getAccessToken(),
            Config.getUserID(),
            userId,
            currentPage.toString()
        )

        call.enqueue(object : Callback<DashBoardUserMainResponse?> {
            override fun onResponse(
                call: Call<DashBoardUserMainResponse?>,
                response: Response<DashBoardUserMainResponse?>
            ) {

                try {

                    Log.e("onResponse", "onResponse: " + response.body()?.status)
                    binding.progress.visibility = View.GONE
                    //------page count

                    val totalPost: Int = response.body()!!.responce.totalposts.toInt()

                    TOTAL_PAGES = totalPost / PAGE_SIZE
                    if (totalPost % PAGE_SIZE == 0) {
                    } else {
                        TOTAL_PAGES = TOTAL_PAGES + 1
                    }
                    //-------------
                    if (response.body()!!.status) {

                        if (response.body()!!.responce.user.size != 0) {

                            Log.e(
                                "response123",
                                "onResponse: " + response.body()!!.responce.user.get(0).totalFollowing.toString()
                            )

                            binding.tvFullName.setText(response.body()!!.responce.user.get(0).fullname)

                            binding.tvTotalPosts.setText(response.body()!!.responce.user.get(0).totalPost.toString())
                            binding.tvFollowrsProfile.setText(response.body()!!.responce.user.get(0).totalFollower.toString())
                            binding.tvFollowingProfile.setText(response.body()!!.responce.user.get(0).totalFollowing.toString())

                            binding.tvMobile.setText(response.body()!!.responce.user.get(0).mobile.toString())

                            followrsCOunt =
                                response.body()!!.responce.user.get(0).totalFollower.toString()
                            followingCOunt =
                                response.body()!!.responce.user.get(0).totalFollowing.toString()
                            userName = response.body()!!.responce.user.get(0).fullname.toString()

                            if (!TextUtils.isEmpty(response.body()!!.responce.user.get(0).email.toString())) {
                                binding.tvEmail.setText(response.body()!!.responce.user.get(0).email.toString())
                            } else {
                                binding.tvEmail.visibility = View.GONE
                            }

                            if (!TextUtils.isEmpty(response.body()!!.responce.user.get(0).dob.toString())) {
                                binding.tvDob.setText(response.body()!!.responce.user.get(0).dob.toString())
                            } else {
                                binding.tvDob.visibility = View.GONE
                            }

                            if (!TextUtils.isEmpty(response.body()!!.responce.user.get(0).workdetail.toString())) {
                                binding.tvWorkDetail.setText(response.body()!!.responce.user.get(0).workdetail.toString())
                            } else {
                                binding.tvWorkDetail.visibility = View.GONE
                            }

                            if (!TextUtils.isEmpty(response.body()!!.responce.user.get(0).website.toString())) {
                                binding.tvWevSite.setText(response.body()!!.responce.user.get(0).website.toString())
                            } else {
                                binding.tvWevSite.visibility = View.GONE
                            }


                            if (!TextUtils.isEmpty(response.body()!!.responce.user.get(0).place.toString())) {
                                binding.tvPlace.setText(response.body()!!.responce.user.get(0).place.toString())
                                binding.tvPlace.visibility = View.VISIBLE
                            } else {
                                binding.tvPlace.visibility = View.GONE
                            }

                            if (!TextUtils.isEmpty(response.body()!!.responce.user.get(0).username.toString())) {
                                binding.tvUserName.setText(response.body()!!.responce.user.get(0).username.toString())
                                binding.tvUserName.visibility = View.VISIBLE
                            } else {
                                binding.tvUserName.visibility = View.GONE
                            }

                            try {
                                if (!TextUtils.isEmpty(response.body()!!.responce.user.get(0).bio.toString())) {
                                    binding.tvBio.setText(response.body()!!.responce.user.get(0).bio.toString())
                                    binding.tvBio.visibility = View.VISIBLE
                                } else {
                                    binding.tvBio.visibility = View.GONE
                                }

                            } catch (e: Exception) {
                            }

                            Log.e(
                                "onResponse",
                                "onResponse: " + response.body()!!.responce.user.get(0).profileImage
                            )
                            Log.e(
                                "onResponse",
                                "onResponse: " + response.body()!!.responce.user.get(0).coverPic
                            )

                            Glide.with(activity!!.applicationContext)
                                .load(response.body()!!.responce.user.get(0).profileImage)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.ic_user_)
                                .into(binding.profileAvtar)


                            Config.init(activity)


                            if (userId.equals(Config.getUserID())) {
                                Config.setUserProfile(response.body()!!.responce.user.get(0).profileImage)
                                Config.savePreferences()
                            }


                            Glide.with(activity!!.applicationContext)
                                .load(response.body()!!.responce.user.get(0).profileImage)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.ic_user_)
                                .into(binding.cardAvtar)


                            if (userId.equals(Config.getUserID())) {
                                Config.setUserProfile(response.body()!!.responce.user.get(0).profileImage)
                            }


                            Config.setUserCover(response.body()!!.responce.user.get(0).coverPic)
                            Config.savePreferences()

                            Glide.with(activity!!.applicationContext)
                                .load(response.body()!!.responce.user.get(0).coverPic)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.nature)
                                .into(binding.coverPhoto)

                            if (response.body()!!.responce.user.get(0).is_follow == 0) {
                                binding.tvProfileFollow.visibility = View.VISIBLE
                                binding.tvProfileFollow.setText("Follow")
                            } else {
                                binding.tvProfileFollow.visibility = View.VISIBLE
                                binding.tvProfileFollow.setText("UnFollow")
                            }

                        }

                        // Got data. Send it to adapter
                        val results: List<DashBoardUserMainResponse.Post> = fetchResults(response)

                        binding.recycler.adapter = adapter
                        adapter!!.addAll(results)

                        Log.e("5555555", "onResponse: " + userId)
                        Log.e("5555555", "onResponse: " + Config.getUserID())
                        Log.e("5555555", "onResponse: " + results.size)


                        if (!userId.equals(Config.getUserID())) {
                            listSuggestUSer = response.body()!!.responce.allusers

                            for (i in 0 until listSuggestUSer.size) {
                                listSuggestUSer.get(i).setmFllowing(false)
                            }

                            adapterSuggest = SugestFollowrsdapter(
                                listSuggestUSer,
                                this@ProfileFragment,
                                activity!!
                            )
                            binding.suggestUserRecycler.adapter = adapterSuggest

                        }

                        if (currentPage < TOTAL_PAGES) {
                            adapter!!.addLoadingFooter()
                        } else {
                            isLastPage = true
                        }

                    } else {
                        Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                } catch (e: Exception) {
                }

            }

            override fun onFailure(call: Call<DashBoardUserMainResponse?>, t: Throwable) {}
        })
    }

    private fun getUserProfileNexttPage() {

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DashBoardUserMainResponse> = apiService.getUserMain(
            ApiConstant.APIKEY_VALUE,
            Config.getAccessToken(),
            Config.getUserID(),
            userId,
            currentPage.toString()
        )

        call.enqueue(object : Callback<DashBoardUserMainResponse?> {
            override fun onResponse(
                call: Call<DashBoardUserMainResponse?>,
                response: Response<DashBoardUserMainResponse?>
            ) {

                try {

                    if (response.body()!!.status) {

                        adapter!!.removeLoadingFooter()
                        isLoading = false

                        val results: List<DashBoardUserMainResponse.Post> = fetchResults(response)
                        adapter!!.addAll(results)

                        if (currentPage != TOTAL_PAGES) adapter!!.addLoadingFooter() else isLastPage =
                            true

                    } else {
                        Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                }

            }

            override fun onFailure(call: Call<DashBoardUserMainResponse?>, t: Throwable) {}
        })
    }

    private fun likePost(postId: String) {

        // binding.progress.visibility =View.VISIBLE
        Log.e("likePost", "onResponse: " + postId)
        Log.e("likePost", "onResponse: " + Config.getUserID())

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DeletePostResponse> = apiService.likePost(
            ApiConstant.APIKEY_VALUE,
            Config.getAccessToken(),
            Config.getUserID(),
            postId
        )

        call.enqueue(object : Callback<DeletePostResponse?> {
            override fun onResponse(
                call: Call<DeletePostResponse?>,
                response: Response<DeletePostResponse?>
            ) {
                Log.e("likePost", "onResponse: " + response.body()?.message)
                //  binding.progress.visibility =View.GONE
                if (response.body()!!.status) {


                } else {
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {}
        })
    }

    private fun likeRemove(postId: String) {

        // binding.progress.visibility =View.VISIBLE
        Log.e("likePost", "onResponse: " + postId)
        Log.e("likePost", "onResponse: " + Config.getUserID())

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DeletePostResponse> = apiService.likeRemove(
            ApiConstant.APIKEY_VALUE,
            Config.getAccessToken(),
            Config.getUserID(),
            postId
        )

        call.enqueue(object : Callback<DeletePostResponse?> {
            override fun onResponse(
                call: Call<DeletePostResponse?>,
                response: Response<DeletePostResponse?>
            ) {
                Log.e("likePost", "onResponse: " + response.body()?.message)
                //  binding.progress.visibility =View.GONE
                if (response.body()!!.status) {


                } else {
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {}
        })
    }

    private fun deletePost(postId: String, pos: Int) {

        binding.progress.visibility = View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DeletePostResponse> =
            apiService.deletePost(ApiConstant.APIKEY_VALUE, Config.getAccessToken(), postId)

        call.enqueue(object : Callback<DeletePostResponse?> {
            override fun onResponse(
                call: Call<DeletePostResponse?>,
                response: Response<DeletePostResponse?>
            ) {
                Log.e("FollowResponse", "onResponse: " + response.body()?.message)
                binding.progress.visibility = View.GONE
                if (response.body()!!.status) {

                    adapter!!.removeItem(pos)
                    adapter!!.notifyDataSetChanged()

                } else {
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {}
        })
    }

    override fun onClickListener(pos: Int, id: Int, mlist: DashBoardUserMainResponse.Alluser) {

        if (id == R.id.tvFollow) {

            listSuggestUSer.removeAt(pos)
            adapterSuggest!!.notifyItemRemoved(pos)
            adapterSuggest!!.notifyDataSetChanged()

            addFollow(Config.getUserID(), mlist.id.toString())

            if (listSuggestUSer.size == 0)
                getUserProfileFirstPage()

        } else if (id == R.id.ic1) {

            val vv = Bundle()
            vv.putString("key", mlist.id.toString())
            findNavController().navigate(R.id.action_ProfileFragment_to_ProfileFragment, vv)

        } else if (id == R.id.icCross) {

            listSuggestUSer.removeAt(pos)
            adapterSuggest!!.notifyItemRemoved(pos)
            adapterSuggest!!.notifyDataSetChanged()

            if (listSuggestUSer.size == 0)
                getUserProfileFirstPage()

        }

    }

    private fun addFollow(userId: String, receiverId: String) {

        binding.progress.visibility = View.VISIBLE
        Log.e("onResponse", "onResponse: " + Config.getAccessToken())

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<AddFollowResponse> = apiService.addFollow(
            ApiConstant.APIKEY_VALUE,
            Config.getAccessToken(),
            userId,
            receiverId
        )

        call.enqueue(object : Callback<AddFollowResponse?> {
            override fun onResponse(
                call: Call<AddFollowResponse?>,
                response: Response<AddFollowResponse?>
            ) {

                Log.e("FollowResponse", "onResponse: " + response.body()?.message)
                binding.progress.visibility = View.GONE
                if (response.body()!!.status) {

                } else {
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<AddFollowResponse?>, t: Throwable) {}
        })
    }

    private fun followRemove(userId: String, receiverId: String) {

        binding.progress.visibility = View.VISIBLE
        Log.e("onResponse", "onResponse: " + Config.getAccessToken())

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<AddFollowResponse> = apiService.followRemove(
            ApiConstant.APIKEY_VALUE,
            Config.getAccessToken(),
            userId,
            receiverId
        )

        call.enqueue(object : Callback<AddFollowResponse?> {
            override fun onResponse(
                call: Call<AddFollowResponse?>,
                response: Response<AddFollowResponse?>
            ) {

                Log.e("FollowResponse", "onResponse: " + response.body()?.message)
                binding.progress.visibility = View.GONE
                if (response.body()!!.status) {
                    binding.tvProfileFollow.setText("Follow")
                } else {
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<AddFollowResponse?>, t: Throwable) {}
        })
    }

    override fun onClickListener(pos: Int, id: Int, list: DashBoardResponse.Randomuser) {


    }

    override fun onItemClick(pos: Int, id: Int, mlist: DashBoardUserMainResponse.Post?) {

        if (id == R.id.avtard) {

            if (!Config.getUserID().equals(userId)) {
                val vv = Bundle()
                vv.putString("key", mlist!!.userId.toString())
                findNavController().navigate(R.id.action_ProfileFragment_to_ProfileFragment, vv)
            }

        } else if (id == R.id.item_report) {

            Log.e("item_report", "onClickListener: " + "report")
            val vv = Bundle()
            vv.putString("postId", mlist!!.postId.toString())
            findNavController().navigate(R.id.action_ProfileFragment_to_RepoartFragment, vv)

        } else if (id == R.id.item_delete) {

            deletePost(mlist!!.postId.toString(), pos)
            Log.e("item_report", "onClickListener: " + "item_delete")

        } else if (id == R.id.item_sharePost) {

            sharePost(Config.getUserID(), mlist!!.postId.toString())

        } else if (id == R.id.pager) {

            Log.e("item_report", "onClickListener: " + "item_delete")
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
            vv.putString("videoCount", mlist.is_video.toString())

            findNavController().navigate(R.id.action_ProfileFragment_to_DetailFragment, vv)

        } else if (id == R.id.tvAutoFit) {

            val vv = Bundle()
            vv.putString("image", "")
            vv.putString("desc", mlist!!.description)
            vv.putString("type", "text")
            vv.putString("postID", mlist.postId.toString())
            vv.putString("videoCount", mlist.is_video.toString())
            findNavController().navigate(R.id.action_ProfileFragment_to_DetailFragment, vv)

        } else if (id == R.id.icLike) {
            try {
                if (mlist!!.is_like == 0) {
                    likePost(mlist.postId.toString())
                } else {
                    likeRemove(mlist.postId.toString())
                }
            } catch (e: Exception) {
            }
        } else if (id == R.id.icShare) {

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
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    """Shared frome Dream Media${mlist.description}  $img"""
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)

            } catch (e: Exception) {
            }

        } else if (id == R.id.tvDownload) {
            // DownloadImageFromPath(mlist?.postImage?.get(0))

            var path: String? = ""

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Util.hasReadPermissions(activity) && Util.hasWritePermissions(activity) && Util.hasCameraPermissions(
                        activity
                    )
                ) {
                    if (mlist!!.type == "video") {
                        path = mlist.video_url
                        val r = DownloadManager.Request(Uri.parse(path))
                        r.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            "fileName"
                        )
                        r.allowScanningByMediaScanner()
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        val dm = this.requireContext()
                            .getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        dm.enqueue(r)
                    } else {
                        for (i in mlist.postImage.indices) {
                            path = mlist.postImage[i]
                            val r = DownloadManager.Request(Uri.parse(path))
                            r.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                "fileName"
                            )
                            r.allowScanningByMediaScanner()
                            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            val dm = this.requireContext()
                                .getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            dm.enqueue(r)
                        }
                    }
                } else {
                    requestAppPermissions()
                }
            } else {
                if (mlist!!.type == "video") {
                    path = mlist.video_url
                    val r = DownloadManager.Request(Uri.parse(path))
                    r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName")
                    r.allowScanningByMediaScanner()
                    r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    val dm = this.requireContext()
                        .getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    dm.enqueue(r)
                } else {
                    for (i in mlist.postImage.indices) {
                        path = mlist.postImage[i]
                        val r = DownloadManager.Request(Uri.parse(path))
                        r.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            "fileName"
                        )
                        r.allowScanningByMediaScanner()
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        val dm = this.requireContext()
                            .getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        dm.enqueue(r)
                    }
                }
            }

        }
    }


    private fun requestAppPermissions() {
        ActivityCompat.requestPermissions(
            this.requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ), 100
        ) // your request code
    }

    private fun sharePost(userId: String, postIds: String) {

        // binding.progress.visibility =View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<PostAddResponse> =
            apiService.sharePost(ApiConstant.APIKEY_VALUE, Config.getAccessToken(), userId, postIds)

        call.enqueue(object : Callback<PostAddResponse?> {
            override fun onResponse(
                call: Call<PostAddResponse?>,
                response: Response<PostAddResponse?>
            ) {
                Log.e("likePost", "onResponse: " + response.body()?.message)
                //  binding.progress.visibility =View.GONE
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(call: Call<PostAddResponse?>, t: Throwable) {}
        })
    }

}
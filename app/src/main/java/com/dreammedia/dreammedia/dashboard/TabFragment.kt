package com.dreammedia.dreammedia.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.adapter.FollowersAdapter
import com.dreammedia.dreammedia.adapter.FollowingAdapter
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.dashboard.dashboard.utils.PaginationScrollListener
import com.dreammedia.dreammedia.databinding.FragmentTabBinding
import com.dreammedia.dreammedia.model.AddFollowResponse
import com.dreammedia.dreammedia.model.GetFollowingResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TabFragment : Fragment() , FollowersAdapter.ClickListener , FollowingAdapter.ClickListener {

    lateinit var binding : FragmentTabBinding

    var bundle: Bundle? = null
    var id :String ="";
    var followrsCOunt :String ="";
    var followingCOunt :String ="";
    var userId :String ="";
    var userName :String ="";

    var PAGE_START  = 1
    var isLoading   = false
    var isLastPage  = false
    var TOTAL_PAGES = 1
    public var PAGE_SIZE   = 10
    var currentPage = PAGE_START
    var linearLayoutManagerFollowrs: LinearLayoutManager? = null

    var PAGE_START_f         = 1
    var isLoading_f          = false
    var isLastPage_f         = false
    var TOTAL_PAGES_f        = 1
    public var PAGE_SIZE_f   = 10
    var currentPage_f        = PAGE_START_f
    var linearLayoutManagerFollowrs_f: LinearLayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_tab, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     // (activity as DashboardActivity?)!!.appbar!!.visibility    = View.GONE
        bundle = this.arguments
        id = bundle!!.getString("id").toString()
        followrsCOunt = bundle!!.getString("followrsCOunt").toString()
        followingCOunt = bundle!!.getString("followingCOunt").toString()
        userId   = bundle!!.getString("userid").toString()
        userName = bundle!!.getString("userName").toString()

        binding.tvFollowingTab.setText(""+followingCOunt+ " Following")
        binding.tvFollowrTxt.setText(""+followrsCOunt + " Followers")

        Log.e("useriduserid", "onViewCreated: " + userId )
        Config.init(activity)

        if (id.equals("follow")){
            binding.v1.setBackgroundColor(resources.getColor(R.color.black))
            binding.v2.setBackgroundColor(resources.getColor(R.color.gray_6))
            binding.recyclerFollowing.visibility =View.GONE
            binding.followRrecycler.visibility =View.VISIBLE
        }else{
            binding.v2.setBackgroundColor(resources.getColor(R.color.black))
            binding.v1.setBackgroundColor(resources.getColor(R.color.gray_6))
            binding.recyclerFollowing.visibility =View.VISIBLE
            binding.followRrecycler.visibility =View.GONE
        }

        binding.ly1.setOnClickListener {
            binding.v1.setBackgroundColor(resources.getColor(R.color.black))
            binding.v2.setBackgroundColor(resources.getColor(R.color.gray_6))

            binding.recyclerFollowing.visibility =View.GONE
            binding.followRrecycler.visibility =View.VISIBLE
        }

        binding.ly2.setOnClickListener {
            binding.v2.setBackgroundColor(resources.getColor(R.color.black))
            binding.v1.setBackgroundColor(resources.getColor(R.color.gray_6))
            binding.recyclerFollowing.visibility =View.VISIBLE
            binding.followRrecycler.visibility =View.GONE

        }

        linearLayoutManagerFollowrs = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.followRrecycler.setLayoutManager(linearLayoutManagerFollowrs)
        binding.followRrecycler.setHasFixedSize(true)

        linearLayoutManagerFollowrs_f = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerFollowing.setLayoutManager(linearLayoutManagerFollowrs_f)
        binding.recyclerFollowing.setHasFixedSize(true)


        binding.followRrecycler.addOnScrollListener(object : PaginationScrollListener(linearLayoutManagerFollowrs) {

            override fun loadMoreItems() {

                this@TabFragment.isLoading = true
                currentPage += 1
                getFollowrs2()
                Log.e("currentPage", "loadMoreItems: $currentPage")
            }
            override fun getTotalPageCount(): Int { return TOTAL_PAGES }
            override fun isLastPage(): Boolean { return  this@TabFragment.isLastPage }
            override fun isLoading(): Boolean { return this@TabFragment.isLoading }
        })

        binding.recyclerFollowing.addOnScrollListener(object : PaginationScrollListener(linearLayoutManagerFollowrs_f) {

            override fun loadMoreItems() {

                this@TabFragment.isLoading_f = true
                currentPage_f += 1
                getFollowing2()
                Log.e("currentPage", "loadMoreItems: $currentPage_f")
            }
            override fun getTotalPageCount(): Int { return TOTAL_PAGES_f }
            override fun isLastPage(): Boolean { return  this@TabFragment.isLastPage_f }
            override fun isLoading(): Boolean { return this@TabFragment.isLoading_f }
        })


    }

    var list: MutableList<GetFollowingResponse.Responce>   = ArrayList()
    var listFollowing: List<GetFollowingResponse.Responce> = ArrayList()

    var adapter: FollowersAdapter?   = null
    var adapterFlowing: FollowingAdapter?   = null

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText(userName)

        currentPage = 1
        TOTAL_PAGES = 1
        isLastPage = false


        currentPage_f = 1
        TOTAL_PAGES_f = 1
        isLastPage_f = false

        getFollowrs1()

        getFollowing1()

    }

    private fun getFollowing1() {

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<GetFollowingResponse> = apiService.getFollowing(ApiConstant.APIKEY_VALUE,
                Config.getAccessToken(), currentPage_f.toString() , userId , Config.getUserID())

        call.enqueue(object : Callback<GetFollowingResponse?> {
            override fun onResponse(call: Call<GetFollowingResponse?>, response: Response<GetFollowingResponse?>) {

                if (response.body()!!.status) {

                    val totalPost: Int = response.body()!!.totalfollowing.toInt()

                    TOTAL_PAGES_f = totalPost / PAGE_SIZE_f
                    if (totalPost % PAGE_SIZE_f == 0) {
                    } else {
                        TOTAL_PAGES_f = TOTAL_PAGES_f + 1
                    }

                    listFollowing = response.body()!!.responce
                    for (i in listFollowing?.indices) {

                        Log.e("TOTAL_PAGES_is_follow", "onResponse: " + listFollowing.get(i).is_follow)

                        if(listFollowing.get(i).is_follow > 0){
                            listFollowing.get(i).setmFllowing(true)
                        }else{
                            listFollowing.get(i).setmFllowing(false)
                        }
                    }

                    Log.e("TOTAL_PAGES_f", "onResponse: " + TOTAL_PAGES_f)

                    adapterFlowing = FollowingAdapter(activity, Config.getUserID(), userId,this@TabFragment)
                    binding.recyclerFollowing.adapter = adapterFlowing

                    adapterFlowing!!.addAll(listFollowing)

                    if (currentPage_f < TOTAL_PAGES_f) {
                        adapterFlowing!!.addLoadingFooter()
                    } else {
                        isLastPage_f = true
                    }

                    // adapter!!.notifyDataSetChanged()
                    Log.e("onResponse", "onResponse: " + response.body()?.responce!!.size)

                } else {  Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()  }

            }override fun onFailure(call: Call<GetFollowingResponse?>, t: Throwable) { }
        })
    }

    private fun getFollowing2() {

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<GetFollowingResponse> = apiService.getFollowing(ApiConstant.APIKEY_VALUE,
                Config.getAccessToken(), currentPage_f.toString() , userId , Config.getUserID())

        call.enqueue(object : Callback<GetFollowingResponse?> {
            override fun onResponse(call: Call<GetFollowingResponse?>, response: Response<GetFollowingResponse?>) {


                    adapterFlowing!!.removeLoadingFooter()
                    isLoading_f = false

                    if (response.body()!!.status){

                        listFollowing = response.body()!!.responce

                        for (i in listFollowing?.indices) {

                            if(listFollowing.get(i).is_follow > 0){
                                listFollowing.get(i).setmFllowing(true)
                            }else{
                                listFollowing.get(i).setmFllowing(false)
                            }
                        }

                        adapterFlowing!!.addAll(listFollowing)

                        if (currentPage_f != TOTAL_PAGES_f) adapterFlowing!!.addLoadingFooter() else isLastPage_f = true

                    }else{
                        Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }

            }override fun onFailure(call: Call<GetFollowingResponse?>, t: Throwable) { }
        })
    }


    private fun getFollowrs1() {

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<GetFollowingResponse> = apiService.getFollowrs(ApiConstant.APIKEY_VALUE,
                Config.getAccessToken(), currentPage.toString(), userId ,Config.getUserID())

        call.enqueue(object : Callback<GetFollowingResponse?> {
            override fun onResponse(call: Call<GetFollowingResponse?>, response: Response<GetFollowingResponse?>) {

                binding.progress.visibility = View.GONE

              if (response.body()!!.status) {

                  val totalPost: Int = response.body()!!.totalfollower.toInt()

                  TOTAL_PAGES = totalPost / PAGE_SIZE

                  if (totalPost % PAGE_SIZE == 0) {
                  } else {
                      TOTAL_PAGES = TOTAL_PAGES + 1
                  }

                    list    = response.body()!!.responce

                  for (i in list?.indices) {

                      if (list.get(i).is_follow == 0){
                          list.get(i).setmFllowing(false)
                      }else{
                          list.get(i).setmFllowing(true)
                      }

                  }

                    adapter = FollowersAdapter(activity, Config.getUserID(),userId, this@TabFragment)
                    binding.followRrecycler.adapter = adapter

                    adapter!!.addAll(list)

                  if (currentPage < TOTAL_PAGES) {
                      adapter!!.addLoadingFooter()
                  } else {
                      isLastPage = true
                  }

                 // adapter!!.notifyDataSetChanged()

                Log.e("onResponse", "onResponse: " + response.body()?.responce!!.size)

                } else {  Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()  }

            }override fun onFailure(call: Call<GetFollowingResponse?>, t: Throwable) { }
        })
    }

    private fun getFollowrs2() {

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<GetFollowingResponse> = apiService.getFollowrs(ApiConstant.APIKEY_VALUE,
                Config.getAccessToken(), currentPage.toString() , userId , Config.getUserID())

        call.enqueue(object : Callback<GetFollowingResponse?> {
            override fun onResponse(call: Call<GetFollowingResponse?>, response: Response<GetFollowingResponse?>) {

                try {

                    if (response.body()!!.status){

                        adapter!!.removeLoadingFooter()
                        isLoading = false

                        list = response.body()!!.responce

                        for (i in list?.indices) {

                            Log.e("onResponseasdas", "onResponse: "+ list.get(i).is_follow)

                            if (list.get(i).is_follow == 0){
                                list.get(i).setmFllowing(false)
                            }else{
                                list.get(i).setmFllowing(true)
                            }


                        }

                        adapter!!.addAll(list)

                        if (currentPage != TOTAL_PAGES) adapter!!.addLoadingFooter() else isLastPage = true

                    }else{
                        Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) { }

            }override fun onFailure(call: Call<GetFollowingResponse?>, t: Throwable) { }
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
                 //   Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<AddFollowResponse?>, t: Throwable) {}
        })
    }

    override fun onItemClick(pos: Int, id: Int, mlist: GetFollowingResponse.Responce?) {

        if (id ==  R.id.imgCard){

            val vv = Bundle()
            vv.putString("key", mlist!!.userId.toString())
            findNavController().navigate(R.id.action_TabFragment_to_ProfileFragment, vv)

        }else if (id == R.id.tvFollow){

            if (mlist!!.getmFllowing()){

                mlist?.setmFllowing(false)
                adapter?.notifyDataSetChanged()
                followRemove( Config.getUserID() , mlist!!.userId.toString())

            }else{
                mlist?.setmFllowing(true)
                adapter?.notifyDataSetChanged()
                addFollow( Config.getUserID() , mlist!!.userId.toString())
            }

        }

    }

    override fun onFollowingItemClick(pos: Int, id: Int, mlist: GetFollowingResponse.Responce?) {

            if (id ==  R.id.imgCard){

                val vv = Bundle()
                vv.putString("key", mlist!!.userId.toString())
                findNavController().navigate(R.id.action_TabFragment_to_ProfileFragment, vv)

            }else if (id == R.id.tvFollow){

                if (mlist!!.getmFllowing()){

                    mlist?.setmFllowing(false)
                    adapterFlowing?.notifyDataSetChanged()
                    followRemove( Config.getUserID() , mlist!!.userId.toString())

                }else{
                    mlist?.setmFllowing(true)
                    adapterFlowing?.notifyDataSetChanged()
                    addFollow( Config.getUserID() , mlist!!.userId.toString())
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


}
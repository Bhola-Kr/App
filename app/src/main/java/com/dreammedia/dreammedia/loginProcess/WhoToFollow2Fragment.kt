package com.dreammedia.dreammedia.loginProcess

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
import com.dreammedia.dreammedia.adapter.WhoToFollowAdapter
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.dashboard.dashboard.utils.PaginationScrollListener
import com.dreammedia.dreammedia.databinding.FragmentWhoToFollow2Binding
import com.dreammedia.dreammedia.model.AddFollowResponse
import com.dreammedia.dreammedia.model.WhoToFollowResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WhoToFollow2Fragment : Fragment() , WhoToFollowAdapter.ClickListener{

     lateinit var binding : FragmentWhoToFollow2Binding
     var list: MutableList<WhoToFollowResponse.Responce>        = ArrayList()
     var adapter: WhoToFollowAdapter?   = null

     var PAGE_START      = 1
     var isLoading       = false
     var isLastPage      = false
     var TOTAL_PAGES     = 1
     var PAGE_SIZE       = 10
     var currentPage     = PAGE_START
     val bundle: Bundle? = null
     var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_who_to_follow2, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recycler.setLayoutManager(linearLayoutManager)
        binding.recycler.setHasFixedSize(true)
     // adapter = WhoToFollowAdapter(list ,this@WhoToFollow2Fragment, this.requireContext())

        binding.recycler.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {

               this@WhoToFollow2Fragment.isLoading = true
               currentPage += 1
               getDashBoardNextPage()
               Log.e("currentPage", "loadMoreItems: $currentPage")

            }override fun getTotalPageCount(): Int { return TOTAL_PAGES }
            override fun isLastPage(): Boolean { return  this@WhoToFollow2Fragment.isLastPage }
            override fun isLoading(): Boolean { return this@WhoToFollow2Fragment.isLoading }
        })

    try {
            val string: String? = bundle?.getString("key")
            Log.e("string", "onViewCreated: " + string)

        } catch (e: Exception) {}

        binding.tvSkip.setOnClickListener {
            findNavController().navigate(R.id.action_WhoToFollowFragment_to_DashboardFragment)
        }

        firstPageLoading()

    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("Follow")
    }

    private fun firstPageLoading() {

        binding.progress.visibility =View.VISIBLE
        Log.e("onResponse111111", "onResponse: "+ Config.getAccessToken())
        Log.e("onResponse111111", "onResponse: "+ Config.getUserID())

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<WhoToFollowResponse> = apiService.getListUsers(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , Config.getUserID() , currentPage.toString())

        call.enqueue(object : Callback<WhoToFollowResponse?> {
             override fun onResponse(call: Call<WhoToFollowResponse?>, response: Response<WhoToFollowResponse?>) {

                    binding.progress.visibility = View.GONE
          try {

                  val totalPost: Int = response.body()!!.totaluser.toInt()
                  TOTAL_PAGES = totalPost / PAGE_SIZE

                  if (totalPost % PAGE_SIZE == 0) {
                  } else {
                      TOTAL_PAGES = TOTAL_PAGES + 1
                  }

                    Log.e("onResponse", "onResponse: "+TOTAL_PAGES)

                    if (response.body()!!.status){

                        list = response.body()!!.responce

                        Log.e("onResponse555", "onResponse: "+list.size)

                        for (i in 0 until list.size) {
                            list.get(i).setmFllowing(false)
                        }

                        adapter = WhoToFollowAdapter(list ,this@WhoToFollow2Fragment, activity!!)
                        binding.recycler.adapter = adapter
                        adapter!!.addAll(list)

                        if (currentPage < TOTAL_PAGES) {
                        } else {
                            isLastPage = true
                        }

                    }else{  Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show() }

                } catch (e: Exception) { }

            }override fun onFailure(call: Call<WhoToFollowResponse?>, t: Throwable) {}
        })
    }

    private fun getDashBoardNextPage() {

        binding.progress.visibility =View.VISIBLE
        Log.e("onResponse", "onResponse: "+ Config.getAccessToken())
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<WhoToFollowResponse> = apiService.getListUsers(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , Config.getUserID() ,currentPage.toString())

        call.enqueue(object : Callback<WhoToFollowResponse?> {
            override fun onResponse(call: Call<WhoToFollowResponse?>, response: Response<WhoToFollowResponse?>) {

                    binding.progress.visibility = View.GONE

                try {

                    isLoading = false

                    if (response.body()!!.responce.size != 0){

                    val results: List<WhoToFollowResponse.Responce> = response.body()!!.responce
                    for (i in 0 until results.size) {
                        results.get(i).setmFllowing(false)
                    }
                    adapter!!.addAll(results)

                    Log.e("TOTAL_PAGES", "onResponse: "+ TOTAL_PAGES)

                    if (currentPage != TOTAL_PAGES) adapter!!.addLoadingFooter() else isLastPage = true

                    }

                } catch (e: java.lang.Exception) { e.printStackTrace() }

            }override fun onFailure(call: Call<WhoToFollowResponse?>, t: Throwable) {}
        })
    }

    private fun addFollow(userId : String , receiverId : String) {

        //  binding.progress.visibility =View.VISIBLE
        Log.e("onResponse", "onResponse: "+ Config.getAccessToken())

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

    override fun onClickListener(pos: Int, id: Int, mlist: WhoToFollowResponse.Responce) {

        if (id ==  R.id.imgCard){
            val vv = Bundle()
            vv.putString("key", mlist.id.toString())
            findNavController().navigate(R.id.action_WhoToFollow2Fragment_to_ProfileFragment, vv)

        }else if (id == R.id.tvFollow){

            Log.e("mlist", "onClickListener: "+ mlist!!.getmFllowing())

            if (mlist!!.getmFllowing()){

                mlist?.setmFllowing(false)
                adapter?.notifyDataSetChanged()
                followRemove( Config.getUserID() , mlist!!.id.toString())
                Log.e("mlist", "onClickListener: "+ "remove")

            }else{
                mlist.setmFllowing(true)
                adapter?.notifyDataSetChanged()
                addFollow( Config.getUserID() , mlist.id.toString())
                Log.e("mlist", "onClickListener: "+ "follow")

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
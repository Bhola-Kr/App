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
import com.dreammedia.dreammedia.databinding.FragmentWhoToFollowBinding
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
class WhoToFollowFragment : Fragment() , WhoToFollowAdapter.ClickListener{

    lateinit var binding : FragmentWhoToFollowBinding
    var list: MutableList<WhoToFollowResponse.Responce>        = ArrayList()
    var adapter: WhoToFollowAdapter?   = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_who_to_follow, container, false)
        return binding.root
    }

    val bundle: Bundle? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Config.init(activity)

        binding.recycler.setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false))
        binding.recycler.setHasFixedSize(true)

      try {
            val string: String? = bundle?.getString("key")
            Log.e("string", "onViewCreated: " +string )
        } catch (e: Exception) { }

        binding.tvSkip.setOnClickListener {
            Config.isWhoFollow(true)
            Config.savePreferences()
            findNavController().navigate(R.id.action_WhoToFollowFragment_to_DashboardFragment)
        }

        doWhoToFollow()

    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.hide()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE
    }

    override fun onClickListener(pos: Int, id: Int, mlist: WhoToFollowResponse.Responce) {

        if (id ==  R.id.imgCard){
/*
            val vv = Bundle()
            vv.putString("key", mlist.id.toString())
            findNavController().navigate(R.id.action_WhoToFollow2Fragment_to_ProfileFragment, vv)
*/

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

    private fun doWhoToFollow() {

        binding.progress.visibility =View.VISIBLE

        Log.e("onResponse", "onResponse: "+Config.getAccessToken())
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<WhoToFollowResponse> = apiService.getListUsers(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , Config.getUserID() , "1")

        call.enqueue(object : Callback<WhoToFollowResponse?> {
            override fun onResponse(call: Call<WhoToFollowResponse?>, response: Response<WhoToFollowResponse?>) {

                try {
                    Log.e("onResponse", "onResponse: "+response.body()?.status)
                    binding.progress.visibility =View.GONE
                    if (response.body()!!.status){

                        list = response.body()!!.responce

                        for (i in 0 until list.size) {
                            list.get(i).setmFllowing(false)
                        }

                        adapter = WhoToFollowAdapter(list ,this@WhoToFollowFragment, activity!!)
                        binding.recycler.adapter = adapter

                    }else{
                        Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                }

            }override fun onFailure(call: Call<WhoToFollowResponse?>, t: Throwable) {}
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


}
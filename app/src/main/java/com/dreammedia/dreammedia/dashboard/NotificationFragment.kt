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
import com.dreammedia.dreammedia.adapter.NotificationAdapter
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.databinding.FragmentNotificationBinding
import com.dreammedia.dreammedia.model.NotificationResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NotificationFragment : Fragment()  , NotificationAdapter.ClickListener {

    lateinit var binding : FragmentNotificationBinding
    var list: List<NotificationResponse.Responce>        = ArrayList()
    var adapter: NotificationAdapter?   = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_notification, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //(activity as DashboardActivity?)!!.appbar!!.visibility    = View.GONE
        binding.recycler.setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false))
        binding.recycler.setHasFixedSize(true)
        binding.recycler.isNestedScrollingEnabled = false

        binding.mySwipeRefreshLayout.setOnRefreshListener {

            list.toMutableList().clear()
            adapter!!.notifyDataSetChanged()
            getNotification()
            binding.mySwipeRefreshLayout.isRefreshing = false
        }

        getNotification()

    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("Notification")
    }

    override fun onClickListener(pos: Int, id: Int, mlist: NotificationResponse.Responce) {

        val vv = Bundle()
        vv.putString("key", mlist.userId.toString())
        findNavController().navigate(R.id.action_NotificationFragment_to_ProfileFragment, vv)


    }

    private fun getNotification() {

        Log.e("token",Config.getAccessToken())

        Config.init(activity)
        binding.progress.visibility =View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<NotificationResponse> = apiService.getNotification(ApiConstant.APIKEY_VALUE, Config.getAccessToken() ,  Config.getUserID() )

        call.enqueue(object : Callback<NotificationResponse?> {
            override fun onResponse(call: Call<NotificationResponse?>, response: Response<NotificationResponse?>) {

                Log.e("NotificationResponse", "onResponse: "+response.body()?.responce!!.size)
                binding.progress.visibility = View.GONE

                if (response.body()!!.status){

                    list    = response.body()!!.responce
                    adapter = NotificationAdapter(list ,this@NotificationFragment, this@NotificationFragment)
                    binding.recycler.adapter = adapter

                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<NotificationResponse?>, t: Throwable) {}
        })
    }

}
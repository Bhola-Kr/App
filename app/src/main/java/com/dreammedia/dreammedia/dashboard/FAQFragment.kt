package com.dreammedia.dreammedia.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.adapter.FAQAdapter
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.databinding.FragmentDetailBinding
import com.dreammedia.dreammedia.databinding.FragmentEditProfileBinding
import com.dreammedia.dreammedia.databinding.FragmentFaqBinding
import com.dreammedia.dreammedia.databinding.FragmentFollowingBinding
import com.dreammedia.dreammedia.model.FaqResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FAQFragment : Fragment() ,FAQAdapter.ClickListener{

    lateinit var binding : FragmentFaqBinding

    var flag : Int = 0 ;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_faq, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.faqRecycler.setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false))
        binding.faqRecycler.setHasFixedSize(true)

        Config.init(activity)
        getFaq()

    }

    var adapter: FAQAdapter?   = null
    var list: List<FaqResponse.Responce> = ArrayList()

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("Faq")
    }

    private fun getFaq() {

        binding.progress.visibility =View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<FaqResponse> = apiService.addFaq(ApiConstant.APIKEY_VALUE,Config.getAccessToken())

        call.enqueue(object : Callback<FaqResponse?> {
            override fun onResponse(call: Call<FaqResponse?>, response: Response<FaqResponse?>) {

                Log.e("onResponse", "onResponse: "+response.body()?.status)
                binding.progress.visibility =View.GONE
                if (response.body()!!.status){
                    list    = response.body()!!.responce
                    adapter = FAQAdapter(list ,this@FAQFragment, activity!!)
                    for (i in 0 until list.size) {
                        list.get(i).setmVisable(false)
                    }
                    binding.faqRecycler.adapter = adapter
                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<FaqResponse?>, t: Throwable) {}
        })
    }

    override fun onClickListener(pos: Int, id: Int, mlist: FaqResponse.Responce) {

        if (list.get(pos).getmVisable()){
            list.get(pos).setmVisable(false)
        }else{
            list.get(pos).setmVisable(true)
        }

        adapter!!.notifyDataSetChanged()

    }

}
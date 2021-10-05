package com.dreammedia.dreammedia.dashboard.wallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.customWigits.viewPager.SliderIndicator
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.databinding.FragmentWalletBinding
import com.dreammedia.dreammedia.model.WalletAmountResponse
import com.dreammedia.dreammedia.model.WalletResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class WalletFragment : Fragment() {

    val fragments: MutableList<Fragment> = ArrayList()
    lateinit var binding : FragmentWalletBinding

    protected val CONTENT2 = arrayOf("BASIC", "STANDARD", "PREMIUM")
    var list: List<WalletResponse.Responce>        = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_wallet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
             super.onViewCreated(view, savedInstanceState)

        initKKViewPager()

    }

    private fun initKKViewPager() {

        getWalletPlan()
        getWalletAmount()
    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility   = View.GONE

        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTitle.setText("Wallet")
    }

    private fun getWalletPlan() {

        binding.progress.visibility    = View.VISIBLE
        val apiService: ApiInterface   = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<WalletResponse> = apiService.getPlan(ApiConstant.APIKEY_VALUE, Config.getAccessToken())

        call.enqueue(object : Callback<WalletResponse?> {
            override fun onResponse(call: Call<WalletResponse?>, response: Response<WalletResponse?>) {

                Log.e("onResponse", "onResponse: "+response.body()?.status)
            //    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                binding.progress.visibility = View.GONE
                if (response.body()!!.status){

                    list = response.body()!!.responce
                    binding.viewPager.setAdapter(TestFragmentAdapter(activity?.getSupportFragmentManager(),activity ,list))
                    binding.viewPager.setAnimationEnabled(true)
                    binding.viewPager.setFadeEnabled(true)
                    binding.viewPager.setFadeFactor(0.6f)

                    var mIndicator = SliderIndicator(activity!!, binding.pagesContainer, binding.viewPager, R.drawable.indicator_circle)
                    mIndicator.setPageCount(response.body()!!.responce.size.toInt())
                    mIndicator.show()

                    binding.viewPager.setCurrentItem(1)

                }else{
                    //  Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<WalletResponse?>, t: Throwable) {}
        })
    }

    private fun getWalletAmount() {

        val apiService: ApiInterface         = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<WalletAmountResponse> = apiService.getWalletAmount(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),"26")

        call.enqueue(object : Callback<WalletAmountResponse?> {
            override fun onResponse(call: Call<WalletAmountResponse?>, response: Response<WalletAmountResponse?>) {

                Log.e("onResponse", "onResponse: "+response.body()?.status)
                if (response.body()!!.status){

                    if (response.body()!!.currentplan.size != 0){
                        binding.tvAmount.setText(response.body()!!.responce.get(0).totalwalletcoin.toString())
                        binding.tvAvailableDay.setText("Validity"+"\n"+response.body()!!.currentplan.get(0).month.toString()+" Month")
                    }else{
                        binding.tvAmount.setText("Wallet"+"\n"+"0")
                        binding.tvAvailableDay.setText("Validity"+"\n"+"0"+"Month")
                    }

                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<WalletAmountResponse?>, t: Throwable) {}
        })
    }

}
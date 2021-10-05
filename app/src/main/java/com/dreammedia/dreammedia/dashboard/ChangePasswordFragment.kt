package com.dreammedia.dreammedia.dashboard

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.databinding.FragmentChangePasswordBinding
import com.dreammedia.dreammedia.model.ChangePassword
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import com.dreammedia.dreammedia.utlis.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordFragment : Fragment()   {

    lateinit var binding : FragmentChangePasswordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_change_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUpdate.setOnClickListener {

                 binding.editOldPasswordly.error         = null
                 binding.editInputPassword.error         = null
                 binding.editInputConfirmPassword.error  = null

           if (TextUtils.isEmpty(binding.editOldPassword.text.toString().trim())) {
                 binding.editOldPasswordly.setError(resources.getString(R.string.passwordLeftEmpty))
                 return@setOnClickListener
           }else if (TextUtils.isEmpty(binding.editPassword.text.toString().trim())) {
                 binding.editInputPassword.setError(resources.getString(R.string.passwordLeftEmpty))
                 return@setOnClickListener
           }else if (TextUtils.isEmpty(binding.editConfirmPassword.text.toString().trim())) {
                 binding.editInputConfirmPassword.setError(resources.getString(R.string.passwordLeftEmpty))
                 return@setOnClickListener
           }else if (TextUtils.isEmpty(binding.editPassword.text.toString().trim()) && TextUtils.isEmpty(binding.editConfirmPassword.text.toString().trim())) {
                 binding.editInputConfirmPassword.setError(resources.getString(R.string.ConfirmpasswordNotMatch))
                 return@setOnClickListener
           }
            changePassward(Config.getUserID() , binding.editOldPassword.text.toString() , binding.editPassword.text.toString())

        }

    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("Change Password")
    }

    private fun changePassward(userId :String ,oldPass :String , newPass:String) {

        binding.progress.visibility =View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<ChangePassword> = apiService.addChangePasswrd(ApiConstant.APIKEY_VALUE, Config.getAccessToken() ,userId, oldPass , newPass)

        call.enqueue(object : Callback<ChangePassword?> {
            override fun onResponse(call: Call<ChangePassword?>, response: Response<ChangePassword?>) {

                Log.e("onResponse", "onResponse: "+response.body()?.status)
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_LONG).show()
                binding.progress.visibility = View.GONE
                if (response.body()!!.status){
                  activity!!.onBackPressed()
                }else{
                  //  Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<ChangePassword?>, t: Throwable) {}
        })
    }

}
package com.dreammedia.dreammedia.loginProcess

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.databinding.FragmentForgotPasswordBinding
import com.dreammedia.dreammedia.databinding.FragmentLoginBinding
import com.dreammedia.dreammedia.databinding.FragmentVerifyOtpBinding
import com.dreammedia.dreammedia.model.OtpVerifyModel
import com.dreammedia.dreammedia.model.UpdatePasswordResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import com.dreammedia.dreammedia.requestModel.VerifyOtpRequest
import com.dreammedia.dreammedia.utlis.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class VerifyOtpFragment : Fragment() {

    lateinit var binding : FragmentVerifyOtpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_verify_otp, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }

        Config.init(activity)

        val userId: String   = requireArguments().getString("userId").toString()
        val email: String    = requireArguments().getString("email").toString()
        val otp: String      = requireArguments().getString("otp").toString()
       // val password: String = requireArguments().getString("password").toString()
        val key: String      = requireArguments().getString(ApiConstant.KEY).toString()

        Log.e("userId", "onViewCreated: " +userId +" "+email+"  "+otp )
       // binding.editUserName.setText(otp)

        binding.tvVerify.setOnClickListener {

        binding.editInputUserName.error = null

         if (! Util.isValidPhoneNumber(binding.editUserName.text.toString().trim())) {
            binding.editInputUserName.setError(resources.getString(R.string.otpNotLeftEmpty))
            return@setOnClickListener
         }

            if (key.equals(ApiConstant.SIGNUP)){
                val vv = VerifyOtpRequest()
                vv.userid   = userId
                vv.username = email
                vv.otp      = binding.editUserName.text.toString()
                vv.password = userId
                doVerifyOtp(vv)
            }else{
                updatePassword(binding.editUserName.text.toString(),userId,email);
            }

        }
    }

    private fun doVerifyOtp(vv : VerifyOtpRequest) {

        binding.progress.visibility =View.VISIBLE

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<OtpVerifyModel> = apiService.doVerifyOtp(ApiConstant.APIKEY_VALUE,vv)

        call.enqueue(object : Callback<OtpVerifyModel?> {
            override fun onResponse(call: Call<OtpVerifyModel?>, response: Response<OtpVerifyModel?>) {

                Log.e("onResponsestatus", "onResponse: "+response.body()?.status)

                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                binding.progress.visibility =View.GONE
                if (response.body()!!.status){

                    Config.setAccessToken(response.body()!!.token.toString())
                    Config.setUserEmail(response.body()!!.responce.get(0).email)
                    Config.setUserName(response.body()!!.responce.get(0).username)
                    Config.setName(response.body()!!.responce.get(0).fullname)
                    Config.setUserID(response.body()!!.responce.get(0).id)
                    Config.setUserPhoneNumber(response.body()!!.responce.get(0).mobile)

                 try {
                        Config.setRefralCode(response.body()!!.responce.get(0).referral_code)
                     } catch (e: Exception) { }

                    Config.setisLogin(true)
                    Config.savePreferences()

                    val intent = Intent(activity, DashboardActivity::class.java)
                    startActivity(intent)

                }

            }override fun onFailure(call: Call<OtpVerifyModel?>, t: Throwable) {}
        })
    }

    private fun updatePassword(otp : String , userId : String , email : String) {

        binding.progress.visibility =View.VISIBLE

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<UpdatePasswordResponse> = apiService.doUpdatePassword(ApiConstant.APIKEY_VALUE,otp,userId,email)

        call.enqueue(object : Callback<UpdatePasswordResponse?> {
            override fun onResponse(call: Call<UpdatePasswordResponse?>, response: Response<UpdatePasswordResponse?>) {
                Log.e("onResponse", "onResponse: "+response.body()?.status)
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_LONG).show()
                binding.progress.visibility =View.GONE
                if (response.body()!!.status){

                    findNavController().navigate(R.id.action_VerifyOtpFragment_to_LoginFragment )

                }

            }override fun onFailure(call: Call<UpdatePasswordResponse?>, t: Throwable) {}
        })
    }

}
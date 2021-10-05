package com.dreammedia.dreammedia.loginProcess

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.databinding.FragmentForgotPasswordBinding
import com.dreammedia.dreammedia.databinding.FragmentLoginBinding
import com.dreammedia.dreammedia.model.ForgotPasswordResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiConstant.FORGOTPASSWORD
import com.dreammedia.dreammedia.network.ApiConstant.KEY
import com.dreammedia.dreammedia.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ForgotPasswordFragment : Fragment() {


    lateinit var binding : FragmentForgotPasswordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_forgot_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvContinue.setOnClickListener {

            binding.editInputUserName.error = null

           if (TextUtils.isEmpty(binding.editUserName.text.toString().trim())) {
                binding.editInputUserName.setError(resources.getString(R.string.usernameLeftEmpty))
                return@setOnClickListener
            }

            doForgotPassword(binding.editUserName.text.toString())

        }

        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun doForgotPassword(name : String) {

        binding.progress.visibility =View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.progress.indeterminateTintList = ColorStateList.valueOf(Color.CYAN)
        }

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<ForgotPasswordResponse>  = apiService.doForgotPassword(ApiConstant.APIKEY_VALUE,name)

        call.enqueue(object : Callback<ForgotPasswordResponse?> {
            override fun onResponse(call: Call<ForgotPasswordResponse?>, response: Response<ForgotPasswordResponse?>) {

                Log.e("onResponse", "onResponse: " + response.body()!!.responce.get(0).id)
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()

                binding.progress.visibility = View.GONE
                if (response.body()!!.status) {
                    val bb = Bundle()
                    bb.putString("userId"   , response.body()!!.responce.get(0).id)
                    bb.putString("email"    , response.body()!!.responce.get(0).email)
                    bb.putString("otp"      , response.body()!!.otp)
                    bb.putString("password" , "")
                    bb.putString(KEY        , FORGOTPASSWORD)
                    findNavController().navigate(R.id.action_ForgotPasswordFragment_to_VerifyOtpFragment,bb)
                } else {
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<ForgotPasswordResponse?>, t: Throwable) {}
        })
    }



}
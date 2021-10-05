package com.dreammedia.dreammedia.loginProcess

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.R.color.colorAccent
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.databinding.FragmentSignupBinding
import com.dreammedia.dreammedia.model.SignUpModel
import com.dreammedia.dreammedia.model.SocialLoginModel
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiConstant.APIKEY_VALUE
import com.dreammedia.dreammedia.network.ApiInterface
import com.dreammedia.dreammedia.network.Constants
import com.dreammedia.dreammedia.utlis.Util
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SignUpFragment : Fragment() {

    lateinit var binding : FragmentSignupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_signup, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_SignUpFragment)
        }

        binding.tvLogin.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tvTermsConditions.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://dreammedia.in/term-condition"))
            startActivity(browserIntent)
        }

        binding.cardsignUpGoogleSignIN.setOnClickListener {
            val intent = Intent(requireActivity(), GoogleSignInActivity::class.java)
            startActivityForResult(intent, Constants.RC_SIGN_IN)
        }

        binding.cardSignUpFacebookSignIN.setOnClickListener {
            val intent = Intent(requireActivity(), FacebookSignInActivity::class.java)
            startActivityForResult(intent, Constants.FB_SIGN_IN)
        }

        binding.tvSignUp.setOnClickListener {

            binding.editInputFullName.setError(null)
            binding.editInputEmail.setError(null)
            binding.editInputMobile.setError(null)
            binding.editInputPassword.setError(null)
            binding.editInputConfirmPassword.setError(null)

            if (TextUtils.isEmpty(binding.editFullName.text.toString().trim())) {
                binding.editInputFullName.setError(resources.getString(R.string.nameLeftEmpty))
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.editEmail.text.toString().trim())) {
                binding.editInputEmail.setError(resources.getString(R.string.mailLeftEmpty))
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.editMobile.text.toString().trim())) {
                binding.editInputMobile.setError(resources.getString(R.string.mobileLeftEmpty))
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.editPassword.text.toString().trim())) {
                  binding.editInputPassword.setError(resources.getString(R.string.passwordLeftEmpty))
               // Toast.makeText(activity,resources.getString(R.string.passwordLeftEmpty)+"",Toast.LENGTH_LONG)
                return@setOnClickListener
            }/*else if (! Util.isValidPassword(binding.editPassword.text.toString().trim())) {
                 binding.editInputPassword.setError(resources.getString(R.string.passwordEnvlid))
              //  Toast.makeText(activity,resources.getString(R.string.passwordEnvlid),Toast.LENGTH_LONG)
                return@setOnClickListener
            }*/else if (TextUtils.isEmpty(binding.editConfirmPassword.text.toString().trim())) {
                 binding.editInputConfirmPassword.setError(resources.getString(R.string.ConfirmpasswordLeftEmpty))
              //  Toast.makeText(activity,resources.getString(R.string.passwordEnvlid),Toast.LENGTH_LONG)
                return@setOnClickListener
            }else if (!binding.editPassword.text.toString().trim().equals(binding.editConfirmPassword.text.toString().trim())) {
                  binding.editInputConfirmPassword.setError(resources.getString(R.string.ConfirmpasswordNotMatch))
               // Toast.makeText(activity,resources.getString(R.string.ConfirmpasswordNotMatch),Toast.LENGTH_LONG)
                return@setOnClickListener
            }

            doRegister(binding.editFullName.text.toString() ,
                       binding.editEmail.text.toString()    ,
                       binding.editMobile.text.toString()   ,
                       binding.editRefralCode.text.toString()   ,
                       binding.editPassword.text.toString() )
        }
    }

    private fun doRegister(name : String, email : String, mobileNo : String, refral : String, password : String) {

        binding.progress.visibility =View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.progress.indeterminateTintList = ColorStateList.valueOf(CYAN)
        }



        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<SignUpModel>  = apiService.doSignUp(APIKEY_VALUE,name,email,mobileNo,refral,password)

        call.enqueue(object : Callback<SignUpModel?> {
            override fun onResponse(call: Call<SignUpModel?>, response: Response<SignUpModel?>) {

                try {
                    Log.e("onResponse", "onResponse: "+response.body()?.status)
                    binding.progress.visibility =View.GONE
                    if (response.body()!!.status){
                        val bb = Bundle()
                        bb.putString("userId"   , response.body()!!.responce)
                        bb.putString("email"    , email)
                        bb.putString("otp"      , response.body()!!.otp)
                        bb.putString("password" , password)
                        bb.putString(ApiConstant.KEY, ApiConstant.SIGNUP)

                        findNavController().navigate(R.id.action_SignUpFragment_to_VerifyOtpFragment , bb)
                    }else{
                        Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                }

            }override fun onFailure(call: Call<SignUpModel?>, t: Throwable) {}
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {

            if (data != null) {

                try {

                    val jsonObject = JSONObject(data.getStringExtra("account"))
                    // Log.v(TAG, data.getStringExtra("account"))
                    Log.e("jsonObject", "onActivityResult: " + Gson().toJson(jsonObject))

                    socialId = jsonObject.getString("id")
                    socialUserEmail = jsonObject.getString("email")
                    socialUserName = jsonObject.getString("name")
                    socialType = Constants.GOOGLE

                    doLoginSosial()

/*
                    val socialLoginModel        = SocialLoginModel()
                    socialLoginModel.socialId   = socialId
                    socialLoginModel.socialType = socialType
                    doSocialLogin(socialLoginModel)
*/

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == Constants.FB_SIGN_IN) {

            if (data != null) {

                try {

                    val jsonObject = JSONObject(data.getStringExtra("account"))
                    // Log.v(TAG, data.getStringExtra("account"))

                    //  Log.e("jsonObject", "onActivityResult: " + Gson().toJson(jsonObject) )
                    socialId = jsonObject.getString("id")
                    socialUserEmail = jsonObject.getString("email")
                    socialUserName = jsonObject.getString("name")
                    socialType = Constants.FACEBOOK

                    Log.e("jsonObject", "onActivityResult: " + socialId)
                    Log.e("jsonObject", "onActivityResult: " + socialUserName)
                    Log.e("jsonObject", "onActivityResult: " + socialUserEmail)
                    Log.e("jsonObject", "onActivityResult: " + socialType)

                    doLoginSosial()

                } catch (e: JSONException) { e.printStackTrace() }

            }
        }
    }

    private lateinit var socialUserName: String
    private lateinit var socialUserEmail: String
    private lateinit var socialId: String
    private lateinit var socialType: String

    private fun doLoginSosial() {

        binding.progress.visibility =View.VISIBLE

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<SocialLoginModel> = apiService.doSocialLogin(ApiConstant.APIKEY_VALUE,socialId,socialType,socialUserName, socialUserEmail,"")

        call.enqueue(object : Callback<SocialLoginModel?> {
            override fun onResponse(call: Call<SocialLoginModel?>, response: Response<SocialLoginModel?>) {
                try {
                    Log.e("onResponse", "onResponse: "+response.body()?.status)
                    binding.progress.visibility =View.GONE
                    if (response.body()!!.status){

                        Config.setAccessToken(response.body()!!.token.toString())
                        Config.setUserEmail(response.body()!!.responce.get(0).email)
                        Config.setUserName(response.body()!!.responce.get(0).username)
                        Config.setName(response.body()!!.responce.get(0).fullname)
                        Config.setUserID(response.body()!!.responce.get(0).id.toString())
                        Config.setUserPhoneNumber(response.body()!!.responce.get(0).mobile)

                        Config.setisLogin(true)
                        Config.isWhoFollow(true)

                        Config.savePreferences()

                        val intent = Intent(activity, DashboardActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) { }
            }override fun onFailure(call: Call<SocialLoginModel?>, t: Throwable) {}
        })
    }


}


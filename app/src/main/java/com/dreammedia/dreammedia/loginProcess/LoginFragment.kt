package com.dreammedia.dreammedia.loginProcess

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.databinding.FragmentLoginBinding
import com.dreammedia.dreammedia.model.LoginModel
import com.dreammedia.dreammedia.model.SocialLoginModel
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import com.dreammedia.dreammedia.network.Constants
import com.dreammedia.dreammedia.utlis.AuthenticationListener
import com.dreammedia.dreammedia.utlis.InstagramApp
import com.facebook.login.LoginManager
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment(), AuthenticationListener {

    lateinit var binding : FragmentLoginBinding

    private  var socialUserName: String =""
    private  var picture: String =""
    private  var socialUserEmail: String =""
    private  var socialId: String =""
    private  var socialType: String =""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_ForgotPasswordFragment)
        }

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_SignUpFragment)
        }

        binding.icGoogle.setOnClickListener {

            val intent = Intent(requireActivity(), GoogleSignInActivity::class.java)
            startActivityForResult(intent, Constants.RC_SIGN_IN)
        }

        binding.cardFacebookSignIN.setOnClickListener {

            val intent = Intent(requireActivity(), FacebookSignInActivity::class.java)
            startActivityForResult(intent, Constants.FB_SIGN_IN)

            /*   try {
                if (App.getInstance().googleSignInClient != null) {
                    App.getInstance().googleSignInClient.signOut()
                    App.getInstance().googleSignInClient.asGoogleApiClient().clearDefaultAccountAndReconnect()
                    App.getInstance().googleSignInClient.asGoogleApiClient().disconnect()
                    App.getInstance().googleSignInClient.asGoogleApiClient().connect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }*/
       }

        binding.cardInstagramSignIN.setOnClickListener {

           connectOrDisconnectUser()

        }

        binding.tvTermsConditions.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://dreammedia.in/term-condition"))
            startActivity(browserIntent)

        }

        binding.tvLlogin.setOnClickListener {

            binding.editInputUserName.error = null
            binding.editInputPassword.error = null

            if (TextUtils.isEmpty(binding.editUserName.text.toString().trim())) {
                binding.editInputUserName.setError(resources.getString(R.string.usernameLeftEmpty))
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.editPassword.text.toString().trim())) {
                binding.editInputPassword.setError(resources.getString(R.string.passwordLeftEmpty))
                return@setOnClickListener
            }else{
                doLogin(binding.editUserName.text.toString(),binding.editPassword.text.toString())
            }

        }

        Insta()

    }

     lateinit var mApp: InstagramApp

    private fun Insta() {
        mApp = InstagramApp(
            activity,
            Constants.CLIENT_ID,
            Constants.CLIENT_SECRET,
            Constants.CALLBACK_URL,
            this
        )
        mApp.setListener(object : InstagramApp.OAuthAuthenticationListener {
            override fun onSuccess() {
                Log.e("InstagramApp", "onSuccess: " + mApp.userName)
                Log.e("InstagramApp", "onSuccess: " + mApp.userInfo)
                Log.e("InstagramApp", "onSuccess: " + mApp.id)

                // tvSummary.setText("Connected as " + mApp.getUserName());
                // btnConnect.setText("Disconnect");
                // llAfterLoginView.setVisibility(View.VISIBLE);
                // userInfoHashmap = mApp.
                // mApp.fetchUserName(handler);
            }

            override fun onFail(error: String?) {
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            }
        })
        if (mApp.hasAccessToken()) {
            // tvSummary.setText("Connected as " + mApp.getUserName());
            //  btnConnect.setText("Disconnect");
            //  llAfterLoginView.setVisibility(View.VISIBLE);
            //  mApp.fetchUserName(handler);
        }
        mApp.myInstaListner(object : AuthenticationListener {
            override fun onTokenReceived(auth_token: String) {
                Log.e("myInstaListner", "onTokenReceived: $auth_token")
                val queue: RequestQueue = Volley
                    .newRequestQueue(activity)
                val request = StringRequest(
                    Request.Method.GET,
                    "https://graph.instagram.com/me?fields=id,username&access_token=$auth_token",
                    object : com.android.volley.Response.Listener<String?> {
                        override fun onResponse(response: String?) {
                            Log.e("InstaAccessToken", "onResponse: $response")
                            try {
                                val obj = JSONObject(response)
                                val name = obj.getString("username")
                                val id = obj.getString("id")

                                socialId        = id
                                socialUserEmail = ""
                                socialUserName  = name
                                socialType      = Constants.INSTA

                                doLoginSosial()

                            } catch (e: JSONException) { e.printStackTrace() }
                        }
                    }, object : com.android.volley.Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError) {

                        }
                    })
                queue.add(request)
            }
        })
    }


    private fun connectOrDisconnectUser() {
        if (mApp.hasAccessToken()) {
            val builder =
                AlertDialog.Builder(activity)
            builder.setMessage("Disconnect from Instagram?")
                .setCancelable(false)
                .setPositiveButton(
                    "Yes"
                ) { dialog, id ->
                    Log.e("InstagramApp", "onSuccess: " + mApp.getUserName())
                    Log.e("InstagramApp", "onSuccess: " + mApp.getUserInfo())
                    Log.e("InstagramApp", "onSuccess: " + mApp.getId())
                    mApp.resetAccessToken()

                    // btnConnect.setVisibility(View.VISIBLE);
                    // llAfterLoginView.setVisibility(View.GONE);
                    // btnConnect.setText("Connect");
                    // tvSummary.setText("Not connected");
                    /*Log.e(
                        `in`.alphonic.pohkeh.view.loginSignProcess.LoginFragment.TAG,
                        "onSuccess: " + "CONNECT"
                    )*/
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        } else {
            mApp.authorize()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun doLogin(vv : String, pass : String) {

        binding.progress.visibility =View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.progress.indeterminateTintList = ColorStateList.valueOf(Color.CYAN)
        }

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<LoginModel> = apiService.doLogin(ApiConstant.APIKEY_VALUE,vv,pass)

        call.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                try {
                    Log.e("onResponse", "onResponse: "+response.body()?.status)


                    Log.e("onResponse", "onResponse: "+response.body()!!.responce.get(0).username)
                    Log.e("onResponse", "onResponse: "+response.body()!!.responce.get(0).fullname)

                    binding.progress.visibility =View.GONE
                    if (response.body()!!.status){

                        Config.setAccessToken(response.body()!!.token.toString())
                        Config.setUserEmail(response.body()!!.responce.get(0).email)
                        Config.setUserName(response.body()!!.responce.get(0).username)
                        Config.setName(response.body()!!.responce.get(0).fullname)
                        Config.setUserID(response.body()!!.responce.get(0).id)
                        Config.setUserPhoneNumber(response.body()!!.responce.get(0).mobile)

                        Config.setRefralCode(response.body()!!.responce.get(0).referral_code)

                        Config.setisLogin(true)
                        Config.isWhoFollow(true)

                        Config.savePreferences()

                        val intent = Intent(activity, DashboardActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }else{
                        Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) { }
            }override fun onFailure(call: Call<LoginModel?>, t: Throwable) {}
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

                    socialId        = jsonObject.getString("id")
                    socialUserEmail = jsonObject.getString("email")
                    socialUserName  = jsonObject.getString("name")
                    socialType      = Constants.GOOGLE

                    doLoginSosial()

                  /*val socialLoginModel        = SocialLoginModel()
                    socialLoginModel.socialId   = socialId
                    socialLoginModel.socialType = socialType
                    doSocialLogin(socialLoginModel)*/

                } catch (e: JSONException) { e.printStackTrace() }

            }
        } else if (requestCode == Constants.FB_SIGN_IN) {

            if (data != null) {

                try {

                    LoginManager.getInstance().logOut();

                     val jsonObject = JSONObject(data.getStringExtra("account"))
                  // Log.v(TAG, data.getStringExtra("account"))
                  // Log.e("jsonObject", "onActivityResult: " + Gson().toJson(jsonObject) )

                    try {
                        socialId        = jsonObject.getString("id")
                    } catch (e: Exception) { }

                    try {
                        socialUserEmail = jsonObject.getString("email")
                    } catch (e: Exception) { }

                    try {
                        socialUserName  = jsonObject.getString("name")
                    } catch (e: Exception) { }

                    try {
                        picture         = jsonObject.getString("picture")
                    } catch (e: Exception) { }

                    socialType      = Constants.FACEBOOK

                    Log.e("jsonObject", "onActivityResult: " + socialId)
                    Log.e("jsonObject", "onActivityResult: " + socialUserName)
                    Log.e("jsonObject", "onActivityResult: " + socialUserEmail)
                    Log.e("jsonObject", "onActivityResult: " + socialType)

                    doLoginSosial()

                } catch (e: JSONException) { e.printStackTrace() }

            }
        }
    }

    private fun doLoginSosial() {

        binding.progress.visibility =View.VISIBLE

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<SocialLoginModel> = apiService.doSocialLogin(ApiConstant.APIKEY_VALUE,socialId,socialType,socialUserName ,socialUserEmail,picture)

        call.enqueue(object : Callback<SocialLoginModel?> {
            override fun onResponse(call: Call<SocialLoginModel?>, response: Response<SocialLoginModel?>) {

                try {
                    Log.e("onResponse", "onResponse: "+response.body()?.status)
                    Log.e("onResponse", "onResponse: "+response.body()!!.responce.get(0).username)
                    Log.e("onResponse", "onResponse: "+response.body()!!.responce.get(0).fullname)
                    binding.progress.visibility =View.GONE
                    if (response.body()!!.status){

                        Config.setAccessToken(response.body()!!.token.toString())
                        Config.setUserEmail(response.body()!!.responce.get(0).email)
                        Config.setUserName(response.body()!!.responce.get(0).username)
                        Config.setName(response.body()!!.responce.get(0).fullname)
                        Config.setUserID(response.body()!!.responce.get(0).id.toString())
                        Config.setUserPhoneNumber(response.body()!!.responce.get(0).mobile)

                        Config.setRefralCode(response.body()!!.responce.get(0).referral_code)

                        Config.setisLogin(true)
                        Config.isWhoFollow(true)

                        Config.savePreferences()

                        val intent = Intent(activity, DashboardActivity::class.java)
                        startActivity(intent)
                        activity!!.finish()

                    }else{
                        Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) { }
            }override fun onFailure(call: Call<SocialLoginModel?>, t: Throwable) {
                Log.e("onResponse", "onResponse: "+t.message)
            }
        })

    }

    override fun onTokenReceived(auth_token: String?) {}

}



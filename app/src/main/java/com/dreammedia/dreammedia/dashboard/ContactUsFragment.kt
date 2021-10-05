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
import com.dreammedia.dreammedia.databinding.FragmentContactUsBinding
import com.dreammedia.dreammedia.databinding.FragmentDetailBinding
import com.dreammedia.dreammedia.databinding.FragmentEditProfileBinding
import com.dreammedia.dreammedia.databinding.FragmentFollowingBinding
import com.dreammedia.dreammedia.model.ContactUsResponce
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import com.dreammedia.dreammedia.utlis.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ContactUsFragment : Fragment() {

    lateinit var binding : FragmentContactUsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_contact_us, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as DashboardActivity?)!!.appbar!!.visibility    = View.GONE

        Config.init(activity)
        binding.tvSend.setOnClickListener {

              binding.tvFirstName.error = null
              binding.tvLastName.error  = null
              binding.contact.error     = null
              binding.tvEmail.error     = null

            if (TextUtils.isEmpty(binding.tvFirstName.text.toString().trim())) {
                binding.tvFirstName.setError(resources.getString(R.string.firnameLeft))
                return@setOnClickListener
            } else if (TextUtils.isEmpty(binding.tvLastName.text.toString().trim())) {
                binding.tvLastName.setError(resources.getString(R.string.lastame))
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.contact.text.toString().trim())) {
                binding.contact.setError(resources.getString(R.string.contactNo))
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.tvEmail.text.toString().trim())) {
                binding.tvEmail.setError(resources.getString(R.string.mailLeftEmpty))
                return@setOnClickListener
            }else if (Util.validateEmail(binding.tvEmail.text)) {
                binding.tvEmail.setError(resources.getString(R.string.mailEnvlid))
                return@setOnClickListener
            }

            Log.e("asas", "onViewCreated: " +Config.getUserID()  )
            Log.e("asas", "onViewCreated: " +binding.tvFirstName.text.toString()  )
            Log.e("asas", "onViewCreated: " +binding.tvLastName.text.toString()  )
            Log.e("asas", "onViewCreated: " +binding.tvEmail.text.toString()  )
            Log.e("asas", "onViewCreated: " +binding.contact.text.toString()  )
            Log.e("asas", "onViewCreated: " +binding.tvMessage.text.toString()  )

                contactUs( Config.getUserID() ,
                        binding.tvFirstName.text.toString() ,
                        binding.tvLastName.text.toString() ,
                        binding.tvEmail.text.toString() ,
                        binding.contact.text.toString()  ,
                        binding.tvMessage.text.toString()
                )
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("Contact Us")
    }

    private fun contactUs(userId :String ,fname :String , lname:String, email:String, contact:String, message:String) {

        binding.progress.visibility =View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<ContactUsResponce> = apiService.Inguiry(ApiConstant.APIKEY_VALUE, Config.getAccessToken() ,userId, fname , lname,email,contact,message)

        call.enqueue(object : Callback<ContactUsResponce?> {
            override fun onResponse(call: Call<ContactUsResponce?>, response: Response<ContactUsResponce?>) {

                try {
                    Log.e("onResponse", "onResponse: "+response.body()?.status)
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    binding.progress.visibility = View.GONE

                    if (response.body()!!.status){
                        activity!!.onBackPressed()

                    }else{
                       Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, "Inquiry request send successfully.", Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<ContactUsResponce?>, t: Throwable) {}
        })
    }


}
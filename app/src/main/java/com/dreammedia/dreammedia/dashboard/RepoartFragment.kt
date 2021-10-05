package com.dreammedia.dreammedia.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.databinding.FragmentReportBinding
import com.dreammedia.dreammedia.model.DeletePostResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoartFragment : Fragment()   {

    lateinit var binding : FragmentReportBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_report, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNuditySexual.setText("Nudity or sexual activity")
        binding.tvVoilent.setText("Violent and repulsive content")
        binding.tvBulling.setText("Bullying or harassment")
        binding.tvHarmFull.setText("Harmful or dangerous act")
        binding.tvSucide.setText("Suicide, self-injury or eating disorders")
        binding.tvFalse.setText("False/misleading information")
        binding.tvDontLike.setText("I just don't like it")

        val postId: String = requireArguments().getString("postId").toString()


        binding.tvNuditySexual.setOnClickListener {

            repoartPost( postId ,  binding.tvNuditySexual.text.toString() )
        }
        binding.tvVoilent.setOnClickListener {

            repoartPost( postId ,  binding.tvVoilent.text.toString() )
        }

        binding.tvBulling.setOnClickListener {
            repoartPost( postId ,  binding.tvBulling.text.toString() )
        }
        binding.tvHarmFull.setOnClickListener {
            repoartPost( postId ,  binding.tvHarmFull.text.toString() )
        }
        binding.tvSucide.setOnClickListener {
            repoartPost( postId ,  binding.tvSucide.text.toString() )
        }
        binding.tvFalse.setOnClickListener {
            repoartPost( postId ,  binding.tvFalse.text.toString() )
        }
        binding.tvDontLike.setOnClickListener {
            repoartPost( postId ,  binding.tvDontLike.text.toString() )
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("Report")
    }


    private fun repoartPost(postId : String , remark : String) {


        binding.progress.visibility =View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<DeletePostResponse> = apiService.repoartPost(
            ApiConstant.APIKEY_VALUE,
            Config.getAccessToken(),
            Config.getUserID() ,
                postId,
                remark)
        call.enqueue(object : Callback<DeletePostResponse?> {
            override fun onResponse(call: Call<DeletePostResponse?>, response: Response<DeletePostResponse?>) {
                Log.e("FollowResponse", "onResponse: "+response.body()?.message)
                binding.progress.visibility =View.GONE
                Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                activity!!.onBackPressed()

                if (response.body()!!.status){
                    activity!!.onBackPressed()
                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {}
        })
    }



}
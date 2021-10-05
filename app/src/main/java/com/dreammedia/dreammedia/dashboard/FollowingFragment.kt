package com.dreammedia.dreammedia.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.databinding.FragmentDetailBinding
import com.dreammedia.dreammedia.databinding.FragmentEditProfileBinding
import com.dreammedia.dreammedia.databinding.FragmentFollowingBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FollowingFragment : Fragment() {

    lateinit var binding : FragmentFollowingBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_following, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //(activity as DashboardActivity?)!!.appbar!!.visibility    = View.GONE

        binding.icCheck.setOnClickListener {

            activity?.onBackPressed()
        }
    }
}
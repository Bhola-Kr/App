package com.dreammedia.dreammedia.dashboard

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.databinding.FragmentRateUsBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RateUSFragment : Fragment() {

    lateinit var binding : FragmentRateUsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_rate_us, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //(activity as DashboardActivity?)!!.appbar!!.visibility    = View.GONE

        binding.tvRate.setOnClickListener {

            launchMarket()
        }

    }

    private fun launchMarket() {
        val uri: Uri = Uri.parse("market://details?id=" + activity?.getPackageName())
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
           // Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
        }
    }


    override fun onResume() {
        super.onResume()

        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility   = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("Rate US")


    }

}
package com.dreammedia.dreammedia.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.adapter.CategoryLISTAdapter
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.customWigits.viewPager.FragmentSliderStatus
import com.dreammedia.dreammedia.customWigits.viewPager.SliderIndicator
import com.dreammedia.dreammedia.customWigits.viewPager.SliderPagerAdapterStatus
import com.dreammedia.dreammedia.dashboard.dashboard.utils.PaginationScrollListener
import com.dreammedia.dreammedia.databinding.FragmentHomeBinding
import com.dreammedia.dreammedia.model.CategoryModel
import com.dreammedia.dreammedia.model.CategorySliderModel
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HomeFragment : Fragment() , CategoryLISTAdapter.ClickListener {

    var PAGE_START  = 1
    var isLoading   = false
    var isLastPage  = false
    var TOTAL_PAGES = 0
    var PAGE_SIZE   = 10
    var currentPage = PAGE_START
    var linearLayoutManager: GridLayoutManager? = null

    val fragments: MutableList<Fragment> = ArrayList()
    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
             super.onViewCreated(view, savedInstanceState)



        linearLayoutManager = GridLayoutManager(activity , 2)
        binding.recycler.setLayoutManager(linearLayoutManager)
        binding.recycler.setHasFixedSize(true)
        binding.recycler.isNestedScrollingEnabled = false

     // adapter = WhoToFollowAdapter(list ,this@WhoToFollow2Fragment, this.requireContext())
        binding.recycler.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {

                this@HomeFragment.isLoading = true
                currentPage += 1
                getCategoriesNextPage()
            }
            override fun getTotalPageCount(): Int { return TOTAL_PAGES }
            override fun isLastPage(): Boolean { return this@HomeFragment.isLastPage }
            override fun isLoading(): Boolean { return this@HomeFragment.isLoading }
        })

        getCategorySlider()
    }

    var adapter: CategoryLISTAdapter?   = null

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility   = View.VISIBLE
        (activity as DashboardActivity?)!!.mainLy.visibility     = View.GONE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.VISIBLE

        currentPage = 1
        TOTAL_PAGES = 1
        isLastPage  = false

        getCategoriesFirstPage()
    }

    var list: MutableList<CategoryModel.Responce> = java.util.ArrayList()

    private fun getCategoriesFirstPage() {

        Config.init(activity)

        Log.e("tempid", "layoutGraber: " + Config.getAccessToken() )


        binding.progress.visibility   = View.VISIBLE
        val apiService: ApiInterface  = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<CategoryModel> = apiService.getCategories(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , currentPage.toString() )

        call.enqueue(object : Callback<CategoryModel?> {
            override fun onResponse(call: Call<CategoryModel?>, response: Response<CategoryModel?>)  {

                binding.progress.visibility = View.GONE

                if (response.body()!!.status){

                    try {

                        val totalPost: Int = response.body()!!.totalcount
                        PAGE_SIZE          = response.body()!!.perpage

                        TOTAL_PAGES = totalPost / PAGE_SIZE

                        if (totalPost % PAGE_SIZE == 0) {
                        } else {
                            TOTAL_PAGES = TOTAL_PAGES + 1
                        }

                        if (response.body()!!.status) {

                            adapter = CategoryLISTAdapter(response.body()!!.responce , this@HomeFragment ,activity!! )
                            binding.recycler.adapter = adapter
                            adapter!!.addAll(list)

                            if (currentPage < TOTAL_PAGES) {
                            } else {
                                isLastPage = true
                            }

                        } else {
                            Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) { e.printStackTrace() }

                }else{ Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show() }

            }override fun onFailure(call: Call<CategoryModel?>, t: Throwable) {}
        })
    }

    private fun getCategoriesNextPage() {

        Config.init(activity)

        val apiService: ApiInterface  = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<CategoryModel> = apiService.getCategories(ApiConstant.APIKEY_VALUE, Config.getAccessToken(), currentPage.toString())

        call.enqueue(object : Callback<CategoryModel?> {
            override fun onResponse(call: Call<CategoryModel?>, response: Response<CategoryModel?>) {

                adapter!!.removeLoadingFooter()

                if (response.body()!!.status){

                    list = response.body()!!.responce
                    adapter!!.addAll(list)

                    if (currentPage < TOTAL_PAGES) {
                    } else {
                        isLastPage = true
                    }

                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<CategoryModel?>, t: Throwable) {}
        })
    }

    private fun getCategorySlider() {

        Config.init(activity)

        val apiService: ApiInterface  = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<CategorySliderModel> = apiService.getCategorySlider(ApiConstant.APIKEY_VALUE, Config.getAccessToken())

        call.enqueue(object : Callback<CategorySliderModel?> {
            override fun onResponse(call: Call<CategorySliderModel?>, response: Response<CategorySliderModel?>) {

                if (response.body()!!.status){

                    try {
                        if (fragments != null)
                            fragments.clear()

                        for (i in 0 until response.body()!!.responce.get(0).images.size) {
                            fragments.add(FragmentSliderStatus.newInstance( response.body()!!.responce.get(0).images.get(i)  , R.drawable.status ))
                        }

                        var mAdapter = SliderPagerAdapterStatus(activity?.supportFragmentManager, fragments)
                        binding.sliderViewStatus.setAdapter(mAdapter)
                        binding.sliderViewStatus.setSaveFromParentEnabled(false)

                        var mIndicator = SliderIndicator(activity!!, binding.pagesContainer, binding.sliderViewStatus, R.drawable.indicator_circle)
                        mIndicator.setPageCount(fragments.size)
                        mIndicator.show()
                    } catch (e: Exception) {
                    }


                }else{
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            }override fun onFailure(call: Call<CategorySliderModel?>, t: Throwable) {}
        })
    }

    override fun onClickListener(pos: Int, id: Int, list: CategoryModel.Responce) {

        val vv = Bundle()
        vv.putString("title", list.title.toString())
        vv.putString("catId", list.id.toString())

        Log.e("catId", "getCategoriesFirstPage: " + list.id.toString())

        findNavController().navigate(R.id.action_HomeFragment_to_CategoryFragment , vv)

    }

}
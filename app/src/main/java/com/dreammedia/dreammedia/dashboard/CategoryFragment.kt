package com.dreammedia.dreammedia.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.adapter.CategoryAdapter
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.dashboard.dashboard.utils.PaginationScrollListener
import com.dreammedia.dreammedia.databinding.FragmentCategoryBinding
import com.dreammedia.dreammedia.model.TempletsListModel
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CategoryFragment : Fragment() ,CategoryAdapter.ClickListener {

    val fragments: MutableList<Fragment> = ArrayList()
    lateinit var binding : FragmentCategoryBinding

    var PAGE_START  = 1
    var isLoading   = false
    var isLastPage  = false
    var TOTAL_PAGES = 0
    var PAGE_SIZE   = 10
    var currentPage = PAGE_START
    var linearLayoutManager: GridLayoutManager? = null

    var title: String?  = ""
    var catId: String?  = ""
    val bundle :Bundle ?=arguments

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_category, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
             super.onViewCreated(view, savedInstanceState)

        mview = view

        linearLayoutManager = GridLayoutManager(activity , 3)
        binding.mRecyclerView.setLayoutManager(linearLayoutManager)
        binding.mRecyclerView.setHasFixedSize(true)
        binding.mRecyclerView.isNestedScrollingEnabled = false

        binding.mRecyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                this@CategoryFragment.isLoading = true
                currentPage += 1
                getCategoriesNextPage()
            }
            override fun getTotalPageCount(): Int { return TOTAL_PAGES }
            override fun isLastPage(): Boolean { return this@CategoryFragment.isLastPage }
            override fun isLoading(): Boolean { return this@CategoryFragment.isLoading }
        })

        val myValue = this.requireArguments().getString("title")

      //  title = bundle?.getString("myValue")

        catId = requireArguments().getString("catId").toString()

       // catId = bundle?.getString("catId")
        binding.tvTitle.setText(myValue)

        getCategoriesFirstPage()

    }

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.show()
        (activity as DashboardActivity?)!!.mainMenu.visibility   = View.GONE

        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.mainLy.visibility     = View.VISIBLE
        (activity as DashboardActivity?)!!.tvTitle.setText("")
    }

    var adapter: CategoryAdapter?   = null
    var list: MutableList<TempletsListModel.Responce> = java.util.ArrayList()

    private fun getCategoriesFirstPage() {

        Config.init(activity)

        binding.progress.visibility   = View.VISIBLE
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<TempletsListModel> = apiService.getTemplets(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , currentPage.toString() , catId)

        Log.e("catId", "getCategoriesFirstPage: " + catId)
        Log.e("catId", "getCategoriesFirstPage: " +  Config.getAccessToken())

        call.enqueue(object : Callback<TempletsListModel?> {
            override fun onResponse(call: Call<TempletsListModel?>, response: Response<TempletsListModel?>)  {

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

                            adapter = CategoryAdapter(response.body()!!.responce , this@CategoryFragment , activity!!)
                            binding.mRecyclerView.setAdapter(adapter)

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

            }override fun onFailure(call: Call<TempletsListModel?>, t: Throwable) {}
        })
    }


    private fun getCategoriesNextPage() {

        Config.init(activity)

        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<TempletsListModel> = apiService.getTemplets(ApiConstant.APIKEY_VALUE, Config.getAccessToken() , currentPage.toString() , catId)

        call.enqueue(object : Callback<TempletsListModel?> {
            override fun onResponse(call: Call<TempletsListModel?>, response: Response<TempletsListModel?>)  {

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

            }override fun onFailure(call: Call<TempletsListModel?>, t: Throwable) {}
        })
    }

    lateinit var mview: View

    override fun onClickListener(pos: Int, id: Int, mlist: TempletsListModel.Responce) {

      //  if (pos == 0){

        Log.e("tempid", "layoutGraber: " + mlist.tempid )

            val vv = Bundle()

            vv.putString("tempid", mlist.tempid)

            Navigation.findNavController(mview).navigate(R.id.action_CategoryFragment_to_EditorFragment,vv)

        /*}else{
            val vv = Bundle()

            vv.putString("image", mlist.image)
            vv.putString("type", "image_template")

            vv.putString("desc", "")
            vv.putString("postID", mlist.id.toString())
            vv.putString("videoCount", "")

            Navigation.findNavController(mview).navigate(R.id.action_CategoryFragment_to_DetailFragment, vv)
        }*/

    }

}
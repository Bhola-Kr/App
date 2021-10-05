 package com.dreammedia.dreammedia.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.dreammedia.dreammedia.R;
import com.dreammedia.dreammedia.activity.DashboardActivity;
import com.dreammedia.dreammedia.adapter.WhotoFollowDashBoardAdapter;
import com.dreammedia.dreammedia.config.Config;
import com.dreammedia.dreammedia.dashboard.dashboard.utils.PaginationScrollListener;
import com.dreammedia.dreammedia.databinding.FragmentWhoToFollowDashboardBinding;
import com.dreammedia.dreammedia.model.AddFollowResponse;
import com.dreammedia.dreammedia.model.FontModel;
import com.dreammedia.dreammedia.model.WhoToFollowResponse;
import com.dreammedia.dreammedia.network.ApiClient;
import com.dreammedia.dreammedia.network.ApiConstant;
import com.dreammedia.dreammedia.network.ApiInterface;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WhoToFollowDashBoardFragment extends Fragment {

    FragmentWhoToFollowDashboardBinding binding;
    List<WhoToFollowResponse.Responce>  list    = new ArrayList<>();
    WhotoFollowDashBoardAdapter adapter;

    int PAGE_START      = 1;
    Boolean isLoading   = false;
    Boolean isLastPage  = false;
    int TOTAL_PAGES     = 0;
    int PAGE_SIZE       = 10;
    int currentPage     = PAGE_START;
    LinearLayoutManager linearLayoutManager ;
    View mview;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_who_to_follow_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mview = view;
        Config.init(getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recycler.setLayoutManager(linearLayoutManager);
        binding.recycler.setHasFixedSize(true);
        // adapter = WhoToFollowAdapter(list ,this@WhoToFollow2Fragment, this.requireContext())

        binding.recycler.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true ;
                currentPage += 1;
                getDashBoardNextPage();

            }
            @Override
            public int getTotalPageCount() { return TOTAL_PAGES; }
            @Override
            public boolean isLastPage() { return isLastPage; }
            @Override
            public boolean isLoading() { return isLoading; }
        });


        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(mview).navigate(R.id.action_WhoToFollowFragment_to_DashboardFragment);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        ((DashboardActivity) getActivity()).getSupportActionBar().show();
        ((DashboardActivity) getActivity()).mainMenu.setVisibility(View.GONE);
        ((DashboardActivity) getActivity()).mainLy.setVisibility(View.VISIBLE);
        ((DashboardActivity) getActivity()).tvTileMain.setVisibility(View.GONE);
        ((DashboardActivity) getActivity()).tvTitle.setText("Follow");

        currentPage = 1;
        TOTAL_PAGES = 1;
        isLastPage  = false;

        firstPageLoading();


    }

    private Call<WhoToFollowResponse> callFollowApi() {

        Log.e("onResponse", "onResponse: "+Config.getAccessToken());
        Log.e("onResponse", "onResponse: "+Config.getUserID());
        Log.e("onResponse", "onResponse: "+String.valueOf(currentPage));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.getListUsers(ApiConstant.APIKEY_VALUE, Config.getAccessToken(), Config.getUserID() , String.valueOf(currentPage));

    }

    private void firstPageLoading() {

        binding.progress.setVisibility(View.VISIBLE);

        callFollowApi().enqueue(new Callback<WhoToFollowResponse>() {
            @Override
            public void onResponse(Call<WhoToFollowResponse> call, Response<WhoToFollowResponse> response) {
                Log.e("onResponse", "onResponse: "+response.body().getMessage());

                binding.progress.setVisibility(View.GONE);

                try {

                    int totalPost = Integer.parseInt(response.body().getTotaluser());
                    TOTAL_PAGES = totalPost / PAGE_SIZE ;

                    if (totalPost % PAGE_SIZE == 0) {
                    } else {
                        TOTAL_PAGES = TOTAL_PAGES + 1;
                    }

                    if (response.body().getStatus()){

                        list = response.body().getResponce();

                        for (int i = 0; i < list.size(); i++) {

                            if (list.get(i).getIs_follow() >0){
                                list.get(i).setmFllowing(true);
                            }else {
                                list.get(i).setmFllowing(false);
                            }
                        }

                        adapter = new WhotoFollowDashBoardAdapter(getActivity());
                        binding.recycler.setAdapter(adapter);
                        adapter.addAll(list);

                        adapter.OnItemClickListner(new WhotoFollowDashBoardAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, int id, WhoToFollowResponse.Responce mlist) {


                                if (id == R.id.imgCard){

                                    Bundle vv =new  Bundle();
                                    vv.putString("key", mlist.getId().toString());
                                    Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_ProfileFragment, vv);

                                }else if (id == R.id.tvDashFollow){

                                    if (!mlist.getmFllowing()){
                                        mlist.setmFllowing(true);
                                        adapter.notifyItemChanged(position);
                                        addFollow( Config.getUserID() , mlist.getId().toString());
                                    }else{
                                        mlist.setmFllowing(false);
                                        adapter.notifyItemChanged(position);
                                        followRemove( Config.getUserID() , mlist.getId().toString());
                                    }

                                }

                            }
                        });

                        if (currentPage < TOTAL_PAGES) {
                        } else {
                            isLastPage = true;
                        }

                    }else{

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) { e.printStackTrace(); }

            }@Override
            public void onFailure(Call<WhoToFollowResponse> call, Throwable t) {
            }
        });

    }


    private void getDashBoardNextPage() {

        callFollowApi().enqueue(new Callback<WhoToFollowResponse>() {
            @Override
            public void onResponse(Call<WhoToFollowResponse> call, Response<WhoToFollowResponse> response) {
                Log.e("onResponse", "onResponse: "+response.body().getMessage());

                try {

                    adapter.removeLoadingFooter();
                    isLoading = false;

                    if (response.body().getResponce().size() != 0){

                        List<WhoToFollowResponse.Responce> result = response.body().getResponce();

                        for (int i = 0; i < result.size(); i++) {

                            result.get(i).setmFllowing(false);

                        }

                        adapter.addAll(result);

                        if (currentPage != TOTAL_PAGES){
                            adapter.addLoadingFooter();
                        } else isLastPage = true ;

                    }

                } catch (Exception e) { e.printStackTrace(); }
            }@Override
            public void onFailure(Call<WhoToFollowResponse> call, Throwable t) { }
        });

    }

    private Call<AddFollowResponse> callFollowUser(String userId , String receiverId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.addFollow(ApiConstant.APIKEY_VALUE, Config.getAccessToken(), userId ,receiverId);
    }

    private void addFollow(String userId,String reciverId) {
        // binding.progress.setVisibility(View.VISIBLE);
        Log.e("onResponse", "onResponse: "+Config.getAccessToken());
        callFollowUser(userId ,reciverId).enqueue(new Callback<AddFollowResponse>() {
            @Override
            public void onResponse(Call<AddFollowResponse> call, Response<AddFollowResponse> response) {
                //binding.progress.setVisibility(View.GONE);
                Log.e("onResponse", "onResponse: "+response.body().getMessage());

            }@Override
            public void onFailure(Call<AddFollowResponse> call, Throwable t) {
                //   adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private Call<AddFollowResponse> callRemoveFollow(String userId , String receiverId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.followRemove(ApiConstant.APIKEY_VALUE, Config.getAccessToken(), userId ,receiverId);
    }

    private void followRemove(String userId,String reciverId) {
        // binding.progress.setVisibility(View.VISIBLE);
        Log.e("onResponse", "onResponse: "+Config.getAccessToken());
        callRemoveFollow(userId ,reciverId).enqueue(new Callback<AddFollowResponse>() {
            @Override
            public void onResponse(Call<AddFollowResponse> call, Response<AddFollowResponse> response) {
                //binding.progress.setVisibility(View.GONE);
                Log.e("onResponse", "onResponse: "+response.body().getMessage());

            }@Override
            public void onFailure(Call<AddFollowResponse> call, Throwable t) {
                //   adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }


/*    @Override
    public void onItemClick(int position, int id, WhoToFollowResponse.Responce mlist) {

        if (id == R.id.imgCard){

            Bundle vv =new  Bundle();
            vv.putString("key", mlist.getId().toString());
            Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_ProfileFragment, vv);

        }else if (id == R.id.tvDashFollow){

            Log.e("lund", "onItemClick: " +"click" );
            if (!mlist.getmFllowing()){
                mlist.setmFllowing(true);
                adapter.notifyItemChanged(position);
                addFollow( Config.getUserID() , mlist.getId().toString());
            }
        }
    }*/

}


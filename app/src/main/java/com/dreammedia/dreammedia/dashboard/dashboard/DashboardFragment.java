package com.dreammedia.dreammedia.dashboard.dashboard;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dreammedia.dreammedia.R;
import com.dreammedia.dreammedia.adapter.WhoToFollow2Adapter;
import com.dreammedia.dreammedia.config.Config;
import com.dreammedia.dreammedia.customWigits.viewPager.FragmentSlider;
import com.dreammedia.dreammedia.customWigits.viewPager.SliderIndicator;
import com.dreammedia.dreammedia.customWigits.viewPager.SliderPagerAdapter;
import com.dreammedia.dreammedia.activity.DashboardActivity;
import com.dreammedia.dreammedia.dashboard.dashboard.utils.PaginationNestedScrollListner;
import com.dreammedia.dreammedia.databinding.FragmentDashboardBinding;
import com.dreammedia.dreammedia.model.AddFollowResponse;
import com.dreammedia.dreammedia.model.DashBoardResponse;
import com.dreammedia.dreammedia.model.DeletePostResponse;
import com.dreammedia.dreammedia.model.PostAddResponse;
import com.dreammedia.dreammedia.network.ApiClient;
import com.dreammedia.dreammedia.network.ApiConstant;
import com.dreammedia.dreammedia.network.ApiInterface;
import com.dreammedia.dreammedia.network.Constants;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.dreammedia.dreammedia.utlis.Util.hasCameraPermissions;
import static com.dreammedia.dreammedia.utlis.Util.hasReadPermissions;
import static com.dreammedia.dreammedia.utlis.Util.hasWritePermissions;

public class DashboardFragment extends Fragment implements MyPostLIstAdapter.ClickListener  ,WhoToFollow2Adapter.ClickListener {

    FragmentDashboardBinding binding ;
    MyPostLIstAdapter adapter;
    private LinearLayoutManager linearLayoutManager ;

    private static final int PAGE_START = 1;
    private boolean isLoading  = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES    = 1  ;
    private int PAGE_SIZE      = 6 ;
    private int currentPage    = PAGE_START;


    public CircleImageView cardAvtar;


    RecyclerView recycler ;
    List<Fragment> fragments = new ArrayList<>();
    View mview;

    NestedScrollView nestedScroll ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mview = view;
        Config.init(getActivity());

        cardAvtar = view.findViewById(R.id.cardAvtarHomFrg);

        cardAvtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle vv =new  Bundle();
                vv.putString("key", Config.getUserID());
                Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_ProfileFragment, vv);
            }
        });

        nestedScroll = view.findViewById(R.id.nestedScroll);

        recycler = view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(linearLayoutManager);
     //  recycler.setNestedScrollingEnabled(false);

     // ViewCompat.setNestedScrollingEnabled(recycler , false);
        adapter = new MyPostLIstAdapter(getActivity(), Config.getUserID() , this ,this);

//        Glide.with(getActivity()).load(Config.getUserProfile())
//                .placeholder(R.drawable.ic_user_)
//                .into(cardAvtar);


        Glide.with(getActivity()).load(Config.getUserProfile())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_user_)
                .into(cardAvtar);

       /* binding.recycler.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                getDashBoardNextPage();
                Log.e("currentPage", "loadMoreItems: " +currentPage );
            }@Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });*/

        nestedScroll.setOnScrollChangeListener(new PaginationNestedScrollListner(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                currentPage += 1;
                getDashBoardNextPage();
                Log.e("currentPage", "loadMoreItems: " +currentPage );

            }
            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        }) ;


        binding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_CreatePostFragment);
            }
        });

        binding.caerdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_CreatePostFragment);
            }
        });

        binding.mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        currentPage = 1;
                        TOTAL_PAGES = 1;
                        isLastPage  = false;

                        adapter.clear();

                        getDashBoardFirstPage();


                        binding.mySwipeRefreshLayout.setRefreshing(false);
                    }
                });

            if (!TextUtils.isEmpty(Config.getSlider())){

                DashBoardResponse response = new Gson().fromJson(Config.getSlider(), DashBoardResponse.class);
                if (fragments != null)
                    fragments.clear();

                if (response.getResponce().getSlider().size() != 0){

                    for (int i = 0; i < response.getResponce().getSlider().size(); i++) {
                        String String  = response.getResponce().getSlider().get(i).get(0);
                        String String1 = response.getResponce().getSlider().get(i).get(1);
                        String String2 = response.getResponce().getSlider().get(i).get(2);
                        fragments.add(FragmentSlider.newInstance( "" , String, String1, String2 , R.string.app_name));
                    }

                    SliderPagerAdapter mAdapter = new SliderPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
                    binding.sliderView.setAdapter(mAdapter);
                    binding.sliderView.setSaveFromParentEnabled(false);

                    SliderIndicator mIndicator = new SliderIndicator(getActivity(), binding.pagesContainer, binding.sliderView, R.drawable.indicator_circle);
                    mIndicator.setPageCount(fragments.size());
                    mIndicator.show();

                   /* //-------set post
                     DashBoardResponse responsePost = new Gson().fromJson(Config.getPost(), DashBoardResponse.class);
                    // Got data. Send it to adapter

                    int totalPost = Integer.parseInt(responsePost.getResponce().getTotalposts());
                    TOTAL_PAGES = (totalPost / PAGE_SIZE);

                    Log.e("onResponse", "onResponse: "+TOTAL_PAGES);

                    if (totalPost % PAGE_SIZE == 0) {
                    } else {
                        TOTAL_PAGES = TOTAL_PAGES + 1;
                    }
                    List<DashBoardResponse.Post> results = responsePost.getResponce().getPosts();
                    for (int i = 0; i < results.size(); i++) {
                        results.get(i).setFollowVisiblity(false);
                    }

                    adapter.addAll(results);
                    recycler.setAdapter(adapter);

                    if (currentPage <= TOTAL_PAGES){
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }*/

                }
            }

        getDashBoardFirstPage();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Call<DashBoardResponse> callGetPostApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.getDashBoard(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),String.valueOf(currentPage),Config.getUserID());
    }

    private Call<DashBoardResponse> callSliderApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.getGetSlidwr(ApiConstant.APIKEY_VALUE, Config.getAccessToken());
    }

    private Call<DeletePostResponse> callDeletePost(String postId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.deletePost(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),postId);
    }

    private Call<PostAddResponse> callsharePost(String userId , String postId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.sharePost(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),userId,postId);
    }

    private Call<DeletePostResponse> callLikePost(String postId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.likePost(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),Config.getUserID() ,postId);
    }

    private Call<DeletePostResponse> callLikeRemove(String postId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.likeRemove(ApiConstant.APIKEY_VALUE, Config.getAccessToken(),Config.getUserID() ,postId);
    }

    private Call<AddFollowResponse> callFollowUser(String userId , String receiverId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.addFollow(ApiConstant.APIKEY_VALUE, Config.getAccessToken(), userId ,receiverId);
    }
    private Call<AddFollowResponse> callRemoveFollow(String userId , String receiverId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        return apiService.followRemove(ApiConstant.APIKEY_VALUE, Config.getAccessToken(), userId ,receiverId);
    }

    // set broadcast receiver when post upload successfully then update feed -----------------------------------------------
    private List<DashBoardResponse.Post> fetchResults(Response<DashBoardResponse> response) {
        DashBoardResponse response1 = response.body();
        return response1.getResponce().getPosts();
    }

    private void getDashBoardFirstPage() {

        binding.progress.setVisibility(View.VISIBLE);

        callGetPostApi().enqueue(new Callback<DashBoardResponse>() {
            @Override
            public void onResponse(Call<DashBoardResponse> call, Response<DashBoardResponse> response) {

                try {

                    binding.progress.setVisibility(View.GONE);

                    String dashResp = new Gson().toJson(response.body());
                    Config.setPost(dashResp);
                    Config.savePreferences();

                    Log.e("onResponse", "onResponse: "+response.body().getMessage());
                    try {

                        try {
                            adapter.clear();
                        } catch (Exception e) { e.printStackTrace(); }

                        int totalPost = Integer.parseInt(response.body().getResponce().getTotalposts());
                        TOTAL_PAGES = (totalPost / PAGE_SIZE);

                        Log.e("onResponse", "onResponse: "+TOTAL_PAGES);

                        if (totalPost % PAGE_SIZE == 0) {
                        } else {
                            TOTAL_PAGES = TOTAL_PAGES + 1;
                        }

                    } catch (Exception e) { e.printStackTrace(); }

                    // Got data. Send it to adapter
                    List<DashBoardResponse.Post> results = fetchResults(response);
                    for (int i = 0; i < results.size(); i++) {
                        results.get(i).setFollowVisiblity(false);
                    }

                    adapter.addAll(results);
                    recycler.setAdapter(adapter);

                    if (currentPage < TOTAL_PAGES){
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }

                    getGetSlider();

                } catch (Exception e) { e.printStackTrace(); }

            }@Override
            public void onFailure(Call<DashBoardResponse> call, Throwable t) {
             //   adapter.showRetry(true, fetchErrorMessage(t));
                binding.progress.setVisibility(View.GONE);
            }
        });

    }

    private void getDashBoardNextPage() {

        Log.e("onResponse", "onResponse: "+Config.getAccessToken());
        callGetPostApi().enqueue(new Callback<DashBoardResponse>() {
            @Override
            public void onResponse(Call<DashBoardResponse> call, Response<DashBoardResponse> response) {

                try {
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    List<DashBoardResponse.Post> results = fetchResults(response);
                    for (int i = 0; i < results.size(); i++) {
                        results.get(i).setFollowVisiblity(false);
                    }

                    adapter.addAll(results);

                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }@Override
            public void onFailure(Call<DashBoardResponse> call, Throwable t) {
             //   adapter.showRetry(true, fetchErrorMessage(t));
                binding.progress.setVisibility(View.GONE);
            }
        });
    }

    private void getGetSlider() {

     //  binding.progress.setVisibility(View.VISIBLE);
        Log.e("onResponse", "onResponse: "+Config.getAccessToken());
        Log.e("onResponse", "onResponse: "+Config.getUserID());
        callSliderApi().enqueue(new Callback<DashBoardResponse>() {
            @Override
            public void onResponse(Call<DashBoardResponse> call, Response<DashBoardResponse> response) {

                if (response.body().getResponce().getSlider().size() == 0){
                    binding.crdSlider.setVisibility(View.GONE);
                }else{
                    binding.crdSlider.setVisibility(View.VISIBLE);
                }

                String dashResp = new Gson().toJson(response.body());
                Config.setSlider(dashResp);
                Config.savePreferences();

                Log.e("onResponse", "onResponse: "+dashResp);

                if (fragments != null)
                    fragments.clear();

                if (response.body().getResponce().getSlider().size() != 0){

                    for (int i = 0; i < response.body().getResponce().getSlider().size(); i++) {
                        String String  = response.body().getResponce().getSlider().get(i).get(0);
                        String String1 = response.body().getResponce().getSlider().get(i).get(1);
                        String String2 = response.body().getResponce().getSlider().get(i).get(2);
                        fragments.add(FragmentSlider.newInstance( "" , String, String1, String2 , R.string.app_name));
                    }

                    SliderPagerAdapter mAdapter = new SliderPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
                    binding.sliderView.setAdapter(mAdapter);
                    binding.sliderView.setSaveFromParentEnabled(false);

//                    SliderIndicator mIndicator = new SliderIndicator(getActivity(), binding.pagesContainer, binding.sliderView, R.drawable.indicator_circle);
//                    mIndicator.setPageCount(fragments.size());
//                    mIndicator.show();

                }


            }@Override
            public void onFailure(Call<DashBoardResponse> call, Throwable t) {
                // adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private void deletePost(String userId ,int pos) {

        binding.progress.setVisibility(View.VISIBLE);
        Log.e("onResponse", "onResponse: "+Config.getAccessToken());
        callDeletePost(userId).enqueue(new Callback<DeletePostResponse>() {
            @Override
            public void onResponse(Call<DeletePostResponse> call, Response<DeletePostResponse> response) {

                binding.progress.setVisibility(View.GONE);

                if (response.body().getStatus()){
                    adapter.removeItem(pos);
                }else{
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }@Override
            public void onFailure(Call<DeletePostResponse> call, Throwable t) {
             //   adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private void sharePost(String userId ,String postId) {

        binding.progress.setVisibility(View.VISIBLE);
        Log.e("onResponse", "onResponse: "+Config.getAccessToken());
        callsharePost(userId , postId ).enqueue(new Callback<PostAddResponse>() {
            @Override
            public void onResponse(Call<PostAddResponse> call, Response<PostAddResponse> response) {

                binding.progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

            }@Override
            public void onFailure(Call<PostAddResponse> call, Throwable t) {
             //   adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private void likePost(String userId) {
       // binding.progress.setVisibility(View.VISIBLE);
        Log.e("onResponse", "onResponse: "+Config.getAccessToken());
        callLikePost(userId).enqueue(new Callback<DeletePostResponse>() {
            @Override
            public void onResponse(Call<DeletePostResponse> call, Response<DeletePostResponse> response) {
                //binding.progress.setVisibility(View.GONE);
            }@Override
            public void onFailure(Call<DeletePostResponse> call, Throwable t) {
             //   adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private void likeRemove(String userId) {
       // binding.progress.setVisibility(View.VISIBLE);
        Log.e("onResponse", "onResponse: "+Config.getAccessToken());
        callLikeRemove(userId).enqueue(new Callback<DeletePostResponse>() {
            @Override
            public void onResponse(Call<DeletePostResponse> call, Response<DeletePostResponse> response) {
                //binding.progress.setVisibility(View.GONE);
            }@Override
            public void onFailure(Call<DeletePostResponse> call, Throwable t) {
             //adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    @Override
    public void onItemClick(int pos, int id, DashBoardResponse.Post mlist) {

        if (id == R.id.tvSeeAll) {

            Bundle vv =new  Bundle();
            vv.putString("key", "home");
            Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_WhoToFollowDashBoardFragment, vv);

        } else if (id == R.id.avtard) {

         // feedDialog()
            Bundle vv =new  Bundle();
            vv.putString("key", mlist.getUserId().toString());
            Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_ProfileFragment, vv);

        } else if (id == R.id.item_report) {

            Bundle vv =new  Bundle();
            vv.putString("postId", mlist.getPostId().toString());
            Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_RepoartFragment,vv);
      
        } else if (id == R.id.item_delete) {

           deletePost(mlist.getPostId().toString() , pos);
          //  Log.e("item_report", "onClickListener: " + "item_delete" )

        }else if (id == R.id.item_sharePost) {

          //  sharePost(mlist.getUserId().toString() , mlist.getPostId().toString());
            sharePost(Config.getUserID() , mlist.getPostId().toString());

        }else if (id == R.id.pager) {

            Bundle vv = new  Bundle();

            if (mlist.getType().equals("video")){
                vv.putString("image", mlist.getVideo_url());
                vv.putString("type", "video");
            }else{
                vv.putString("image", mlist.getPostImage().get(pos));
                vv.putString("type", "image");
            }
            vv.putString("desc", "");
            vv.putString("postID", mlist.getPostId().toString());
            vv.putString("videoCount", mlist.getIs_video().toString());

            Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_DetailFragment,vv);

        } else if (id == R.id.tvAutoFit) {

            Bundle vv = new  Bundle();
            vv.putString("image", "");
            vv.putString("desc",mlist.getDescription());
            vv.putString("type", "text");
            vv.putString("postID", mlist.getPostId().toString());
            vv.putString("videoCount", mlist.getIs_video().toString());
            Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_DetailFragment,vv);

        } else if (id == R.id.icLike) {

            if (mlist.getIs_like() == 0){

                likePost(mlist.getPostId().toString());
               // adapter.updateLike(pos , likes , 1);

            }else{

                likeRemove(mlist.getPostId().toString());
               // int likes = (mlist.getTotalLike() - 1);
               // adapter.updateLike(pos , likes ,0);

            }
        }else if (id == R.id.icShare) {

            try {

                String img = "" ;

                StringBuilder sb = new StringBuilder();
                for(int  i =0; i < mlist.getPostImage().size(); i++) {
                        sb.append( mlist.getPostImage().get(i));
                        sb.append("\n");
                }

                img = sb.toString();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Shared frome Dream Media " +"\n"+mlist.getDescription()+"  "+ img);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            } catch (Exception e) { }

        }else if (id == R.id.tvDownload) {

                String path = "" ;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (hasReadPermissions(getActivity()) && hasWritePermissions(getActivity()) && hasCameraPermissions(getActivity())) {

                    if (mlist.getType().equals("video")){

                      /*  DownloadManager.Request r = new DownloadManager.Request(Uri.parse(path));
                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName");
                        r.allowScanningByMediaScanner();
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(r);*/

                        path = mlist.getVideo_url();
                        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                        String fileName = "dream_media" + new Date().getTime()+"."+getMimeType(getContext(),Uri.parse(path));
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));

                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(true).setTitle(fileName)
                                .setDescription("Downloading file")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + "DreamMedia" + File.separator + fileName);
                        downloadManager.enqueue(request);


                    }else{

                        for (int i = 0; i < mlist.getPostImage().size(); i++) {

                          /*  path = mlist.getPostImage().get(i);

                            DownloadManager.Request r = new DownloadManager.Request(Uri.parse(path));
                            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName");
                            r.allowScanningByMediaScanner();
                            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(r);*/


                            path = mlist.getPostImage().get(i);
                            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                            String fileName = "dream_media" + new Date().getTime()+"."+getMimeType(getContext(),Uri.parse(path));
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));

                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                    .setAllowedOverRoaming(true).setTitle(fileName)
                                    .setDescription("Downloading file")
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + "DreamMedia" + File.separator + fileName);
                            downloadManager.enqueue(request);


                        }
                    }

                } else {
                    requestAppPermissions();
                }


            } else {

                if (mlist.getType().equals("video")){

                    path = mlist.getVideo_url();
                    DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                    String fileName = "dream_media" + new Date().getTime()+"."+getMimeType(getContext(),Uri.parse(path));
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));

                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(true).setTitle(fileName)
                            .setDescription("Downloading file")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + "DreamMedia" + File.separator + fileName);
                    downloadManager.enqueue(request);

                }else{

                    for (int i = 0; i < mlist.getPostImage().size(); i++) {

                        path = mlist.getPostImage().get(i);
                        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                        String fileName = "dream_media" + new Date().getTime()+"."+getMimeType(getContext(),Uri.parse(path));
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));

                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(true).setTitle(fileName)
                                .setDescription("Downloading file")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + "DreamMedia" + File.separator + fileName);
                        downloadManager.enqueue(request);

                    }
                }

            }


            //  Intent browserIntent = new Intent(Intent.ACTION_QUICK_VIEW, Uri.parse(mlist.getPostImage().get(0)));
          //   startActivity(browserIntent);

       /*// DownloadImageFromPath(mlist.postImage.get(0))
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle("DreamMedia");
            mProgressDialog.setMessage("Please wait, we are downloading your image file...");

            mMyTask = new DownloadTask().execute(stringToURL(mlist.getPostImage().get(0)));
          */
        }else if (id == R.id.lyadd) {

            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mlist.getRandoads().get(0).getLink()));
                startActivity(browserIntent);
            } catch (Exception e) { e.printStackTrace();

                Toast.makeText(getActivity(), e.getMessage()+"", Toast.LENGTH_SHORT).show();
            }

        }
   }

    private void requestAppPermissions() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                        Manifest.permission.CAMERA
                }, 100); // your request code
    }


    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("getUserProfile", "onResume: "+Config.getUserProfile() );
        Glide.with(getActivity()).load(Config.getUserProfile())
                .placeholder(R.drawable.ic_user_)
                .into(cardAvtar);

        ((DashboardActivity) getActivity()).getSupportActionBar().show();
        ((DashboardActivity) getActivity()).mainMenu.setVisibility(View.VISIBLE);
        ((DashboardActivity) getActivity()).mainLy.setVisibility(View.GONE);
        ((DashboardActivity) getActivity()).tvTileMain.setVisibility(View.VISIBLE);

        currentPage = 1;
        TOTAL_PAGES = 1;
        isLastPage  = false;

    }

    @Override
    public void onPause() {
        super.onPause();

        callGetPostApi().cancel();
        callSliderApi().cancel();

    }

    @Override
    public void onClickListener(int pos, int id, @NotNull DashBoardResponse.Randomuser mlist) {

        if (id == R.id.imgCard){

            Bundle vv =new  Bundle();
            vv.putString("key", mlist.getId().toString());
            Navigation.findNavController(mview).navigate(R.id.action_DashboardFragment_to_ProfileFragment, vv);

        }else if (id == R.id.tvFollow){

            if (!mlist.getmFllowing()){
                mlist.setmFllowing(true);
                adapter.notifyDataSetChanged();
                addFollow( Config.getUserID() , mlist.getId().toString());
            }else {
                mlist.setmFllowing(false);
                followRemove( Config.getUserID() , mlist.getId().toString());
                adapter.notifyDataSetChanged();

            }
        }
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


    private class DownloadTask extends AsyncTask<URL,Void,Bitmap>{
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            mProgressDialog.show();
        }
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;
            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                return bmp;
            }catch(IOException e){ e.printStackTrace();
            }finally{
                connection.disconnect();
            }
            return null;
        }
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            mProgressDialog.dismiss();

            if(result!=null){
              //  mImageView.setImageBitmap(result);
                Uri imageInternalUri = saveImageToInternalStorage(result);
                getPhotoFileUri(String.valueOf(imageInternalUri));
              // mImageViewInternal.setImageURI(imageInternalUri);
                 Toast.makeText(getActivity(), "Successfully Download", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getActivity());
        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images",MODE_PRIVATE);
        // Create a file to save the image

        Log.e("savedImageURI", "saveImageToInternalStorage: "+file.getName() );
        String uid = UUID.randomUUID().toString();
        file = new File(file, uid + ".jpg");

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
        }catch (IOException e)   { e.printStackTrace(); }

        Uri savedImageURI = Uri.parse(file.getAbsolutePath());
     // Return the saved image Uri

        Log.e("savedImageURI", "saveImageToInternalStorage: "+savedImageURI );
        MediaScannerConnection.scanFile(getActivity(), new String[] { file.getAbsolutePath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            MediaScannerConnection.scanFile(getActivity(), new String[]{file.toString()}, new String[]{file.getName()},null);

         /* Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);*/

        } else {
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getRootDirectory())));
        }
        return savedImageURI;
    }

    private ProgressDialog mProgressDialog;

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    public final String APP_TAG = "MyCustomApp";

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
           // Log.d(APP_TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

}

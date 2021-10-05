package com.dreammedia.dreammedia.dashboard.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dreammedia.dreammedia.R;
import com.dreammedia.dreammedia.adapter.WhoToFollow2Adapter;
import com.dreammedia.dreammedia.customWigits.viewPager.MyBounceInterpolator;
import com.dreammedia.dreammedia.customWigits.viewPager.SliderIndicator;
import com.dreammedia.dreammedia.customWigits.viewPager.SliderView;
import com.dreammedia.dreammedia.customWigits.viewPager.readMore.ReadMoreTextView;
import com.dreammedia.dreammedia.dashboard.dashboard.utils.PaginationAdapterCallback;
import com.dreammedia.dreammedia.model.DashBoardResponse;
import com.dreammedia.dreammedia.model.DashBoardUserMainResponse;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;

import static com.dreammedia.dreammedia.utlis.Util.DateTime;

public class MyPostLIstAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ClickListener { void onItemClick(int position, int id , DashBoardUserMainResponse.Post mValues);}

    ClickListener listener ;
    Context context ;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private PaginationAdapterCallback mCallback;
    private String errorMsg;

    List<DashBoardUserMainResponse.Post> list = new ArrayList<>();
    ProgressBar translateProgress ;

    String userId ;

    WhoToFollow2Adapter.ClickListener followClickListner ;

    public MyPostLIstAdapter2(Context mCtx , String muserId  , ClickListener mlistener  , WhoToFollow2Adapter.ClickListener mfollowClickListner ) {
        this.context            = mCtx;
        this.userId             = muserId;
        this.listener           = mlistener;
        this.followClickListner = mfollowClickListner;
//      this.mCallback          = (PaginationAdapterCallback) context;
        list                    = new ArrayList<>();
    }

    public List<DashBoardUserMainResponse.Post> getPosts() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.list_dashboard2, parent, false);
                viewHolder = new ImagViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)) {

            case ITEM:
                initLayoutIMAGE((ImagViewHolder)holder , position);
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {

                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(errorMsg != null ? errorMsg : context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {  return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM; }

    @Override
    public int getItemCount() { return list.size(); }


    private void initLayoutIMAGE(ImagViewHolder holder, int position) {

        try {

            DashBoardUserMainResponse.Post list = getItem(position);

            holder.name.setText(list.getFullname());
            holder.totalIkes.setText(list.getTotalLike().toString());
//          holder.tvTotalShare.setText(list.get(position).totalShare.toString())
            holder.tvTime.setText(list.getAdded());

         // holder.tvTime.setText(DateTime(list.getCreated()));

            Log.e("initLayoutIMAGEisss", "initLayoutIMAGE: " +list.getAdded());

//            holder.tvTime.setText(list.getAdded());

            if (list.getIs_like() == 0){
                holder.icLike.setImageResource(R.drawable.ic_like);
            }else{
                holder.icLike.setImageResource(R.drawable.ic_like_selected);
            }

            Glide.with(context).load(list.getProfileImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.avtard);

            holder.icLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(position,holder.icLike.getId(),list);

                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
                    myAnim.setInterpolator(interpolator);
                    v.startAnimation(myAnim);

                   /* holder.icLike.setImageResource(R.drawable.ic_like_selected);
                      list.setisLiked(true);
                      holder.totalIkes.setText(list.getTotalLike().toString() + ""); */

                    if (list.getIs_like() == 0){
                        int likes = (list.getTotalLike() + 1);
                        list.setTotalLike(likes);
                        list.setIs_like(1);
                        holder.totalIkes.setText(list.getTotalLike().toString());
                        holder.icLike.setImageResource(R.drawable.ic_like_selected);
                    }else{
                        int likes = (list.getTotalLike() - 1);
                        list.setTotalLike(likes);
                        list.setIs_like(0);
                        holder.totalIkes.setText(list.getTotalLike().toString());
                        holder.icLike.setImageResource(R.drawable.ic_like);
                    }

                }
            });

            holder.icShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
                    myAnim.setInterpolator(interpolator);
                    v.startAnimation(myAnim);

                    listener.onItemClick(position,holder.icShare.getId(),list);

                }
            });

            holder.tvDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
                    myAnim.setInterpolator(interpolator);
                    v.startAnimation(myAnim);

                    listener.onItemClick(position,holder.tvDownload.getId(),list);

                }
            });

            if (list.getPostImage().size() != 0) {

                holder.tvDescription.setVisibility(View.VISIBLE);
                holder.pager.setVisibility(View.VISIBLE);
                holder.tvAutoFit.setVisibility(View.GONE);

                holder.tvDescription.setText(list.getDescription());

                List<String> IMAGES = new ArrayList<>();

                for (int i = 0; i < list.getPostImage().size(); i++) {
                    IMAGES.add(list.getPostImage().get(i));
                }

                holder.pager.setAdapter(new SlidingImage_Adapter(context, IMAGES , list.getType()));

                holder.pager.setOnItemClickListener(new SliderView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int mposition) {
                        listener.onItemClick(mposition,holder.pager.getId(),list);
                    }
                });

                if (IMAGES.size() > 1){
                    SliderIndicator mIndicator = new SliderIndicator(holder.itemView.getContext(), holder.pagesContainer,
                    holder.pager, R.drawable.indicator_circle_dashboard );
                    mIndicator.setPageCount(IMAGES.size());
                    mIndicator.show();
                }

                holder.tvDownload.setVisibility(View.VISIBLE);

            }else{

                holder.tvDownload.setVisibility(View.GONE);
                holder.tvDescription.setVisibility(View.GONE);
                holder.pager.setVisibility(View.GONE);

                holder.tvAutoFit.setVisibility(View.VISIBLE);
                holder.tvAutoFit.setVisibility(View.VISIBLE);
                holder.tvAutoFit.setText(list.getDescription());
            }

            holder.icMenue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.icMenue);
                    popup.inflate(R.menu.listing_options_menu);

                    //hide popup menue item
                    Menu popupMenu = popup.getMenu();
                    if (userId.equals(list.getUserId().toString())){

                        Log.e("popupMenu", "onClick: " + "user" );
                        popupMenu.findItem(R.id.item_report).setVisible(false);
                        popupMenu.findItem(R.id.item_delete).setVisible(true);
                        popupMenu.findItem(R.id.item_sharePost).setVisible(true);

/*
                        popupMenu.findItem(R.id.item_report).setEnabled(false);
                        popupMenu.findItem(R.id.item_delete).setEnabled(true);
*/
                    }else{
                        Log.e("popupMenu", "onClick: " + "otherr " );
                        popupMenu.findItem(R.id.item_report).setVisible(true);
                        popupMenu.findItem(R.id.item_delete).setVisible(false);
                        popupMenu.findItem(R.id.item_sharePost).setVisible(true);

/*
                        popupMenu.findItem(R.id.item_delete).setEnabled(false);
                        popupMenu.findItem(R.id.item_report).setEnabled(true);
*/
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getItemId() == R.id.item_report){

                                listener.onItemClick(position,item.getItemId(),list);

                            }else if (item.getItemId() == R.id.item_delete){

                                listener.onItemClick(position,item.getItemId(),list);

                            }else if (item.getItemId() == R.id.item_sharePost){

                                listener.onItemClick(position,item.getItemId(),list);

                            }
                            return false;
                        }
                    });
                    popup.show();

                }
            });

            holder.tvAutoFit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,holder.tvAutoFit.getId(),list);
                }
            });

            holder.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,holder.tvSeeAll.getId(),list);
                }
            });

            holder.avtard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,holder.avtard.getId(),list);
                }
            });

        } catch (Exception e) { e.printStackTrace(); }

    }

    //______________________________________________________________________________________________
    //view Holders

    public void removePosition(int position){
        notifyItemRemoved(position);
        // getCurrentList().remove(position);
        Log.e("listlist", "removePosition: " +list.size() );
    }

    public  void trProgress(ProgressBar translateProgress ){ this .translateProgress = translateProgress; }

    protected class ImagViewHolder  extends RecyclerView.ViewHolder{

        LinearLayout lyPost;
        CircleImageView avtard ;
        TextView name , tvTime;
        ImageView icMenue;
        ReadMoreTextView tvDescription ;
        SliderView pager;
        LinearLayout pagesContainer;
        AutofitTextView tvAutoFit;
        ImageView icLike ,icShare;
        TextView totalIkes , tvDownload  ,tvTotalShare ;

        LinearLayout lyWhoFollow ;
        RecyclerView WhoToFollowrecycler;
        TextView tvSeeAll;

        ImageView lyadd ;
        ImageView icFollowVisiblity ;

        View v1 ,v2, v3, v4 ;

        public ImagViewHolder( View view) {
            super(view);

            lyPost         = view.findViewById(R.id.lyPost);
            avtard         = view.findViewById(R.id.avtard);
            name           = view.findViewById(R.id.name);
            tvTime         = view.findViewById(R.id.tvTime);
            icMenue        = view.findViewById(R.id.icMenue);
            tvDescription  = view.findViewById(R.id.tvDescription);
            pager          = view.findViewById(R.id.pager);
            pagesContainer = view.findViewById(R.id.pagesContainer);
            tvAutoFit      = view.findViewById(R.id.tvAutoFit);
            icLike         = view.findViewById(R.id.icLike);
            icShare        = view.findViewById(R.id.icShare);
            totalIkes      = view.findViewById(R.id.totalIkes);
            tvDownload     = view.findViewById(R.id.tvDownload);
            tvTotalShare   = view.findViewById(R.id.tvTotalShare);

            lyWhoFollow         = view.findViewById(R.id.lyWhoFollow);
            WhoToFollowrecycler = view.findViewById(R.id.WhoToFollowrecycler);
            tvSeeAll            = view.findViewById(R.id.tvSeeAll);

            lyadd             = view.findViewById(R.id.lyadd);
            icFollowVisiblity = view.findViewById(R.id.icFollowVisiblity);

            v1 = view.findViewById(R.id.v1);
            v2 = view.findViewById(R.id.v2);
            v3 = view.findViewById(R.id.v3);
            v4 = view.findViewById(R.id.v4);


        }
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(list.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt ,tvTaptoLoad;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn    = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt    = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);
            tvTaptoLoad  = itemView.findViewById(R.id.tvTaptoLoad);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);

            mErrorTxt.setText(R.string.whatWentWrong);
            tvTaptoLoad.setText(R.string.tap_to_reload);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }
    }

    /*
       Helpers - Pagination  _________________________________________________________________________________________________
   */
    public void add(DashBoardUserMainResponse.Post r) {
        list.add(r);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<DashBoardUserMainResponse.Post> moveResults) {
        for (DashBoardUserMainResponse.Post result : moveResults) {
            add(result);
        }
    }

    public void remove(DashBoardUserMainResponse.Post r) {
        int position = list.indexOf(r);
        if (position > -1) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(int positon) {
        list.remove(positon);
        notifyDataSetChanged();
    }

    public void updateLike(int pos, int like , int isLike) {
        list.get(pos).setTotalLike(like);
        list.get(pos).setIs_like(isLike);
        notifyDataSetChanged();
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new DashBoardUserMainResponse.Post());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list.size() - 1;
        DashBoardUserMainResponse.Post result = getItem(position);

        if (result != null) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public DashBoardUserMainResponse.Post getItem(int position) { return list.get(position);  }

}
package com.dreammedia.dreammedia.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dreammedia.dreammedia.R;
import com.dreammedia.dreammedia.dashboard.dashboard.utils.PaginationAdapterCallback;
import com.dreammedia.dreammedia.model.GetFollowingResponse;
import com.dreammedia.dreammedia.model.WhoToFollowResponse;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WhotoFollowDashBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ClickListener { void onItemClick(int position, int id , WhoToFollowResponse.Responce mValues);}

    ClickListener listener ;
    Context context ;

    private static final int ITEM    = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad  = false;

    List<WhoToFollowResponse.Responce> list = new ArrayList<>();
    ProgressBar translateProgress ;


    public WhotoFollowDashBoardAdapter(Context mCtx  ) {
        this.context            = mCtx;
        list                    = new ArrayList<>();
    }

    public List<WhoToFollowResponse.Responce> getPosts() {
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
            View viewItem = inflater.inflate(R.layout.list_follow_dash, parent, false);
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

                    loadingVH.mErrorTxt.setText("" != null ? "" : context.getString(R.string.error_msg_unknown));

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
            if (list.get(position).getmFllowing()){
                holder.tvDashFollow.setBackgroundResource(R.drawable.drawable_round_corner_solid);
                holder.tvDashFollow.setTextColor(context.getResources().getColor( R.color.white));
                holder.tvDashFollow.setText("UnFollow");
            }else{
                holder.tvDashFollow.setBackgroundResource(R.drawable.drawable_round_corner_boarder);
                holder.tvDashFollow.setTextColor(context.getResources().getColor( R.color.colorAccent));
                holder.tvDashFollow.setText("Follow");
            }

            holder.tvName.setText(list.get(position).getFullname());
            holder.tvDesignation.setText(list.get(position).getUsername().toString());

            Glide.with(context).load(list.get(position).getProfileImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_user_)
                    .into(holder.imgCard);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


        try {
            holder.imgCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position , holder.imgCard.getId() , list.get(position));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            holder.tvDashFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listener.onItemClick(position , holder.tvDashFollow.getId() , list.get(position));


                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }


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

        TextView tvName , tvDesignation  ,tvDashFollow ;
        ImageView icMenue ;
        CircleImageView imgCard;

        public ImagViewHolder( View view) {
            super(view);

             tvName            = itemView.findViewById(R.id.tvName) ;
             tvDesignation     = itemView.findViewById(R.id.tvDesignation)  ;
             imgCard           = itemView.findViewById(R.id.imgCard) ;
             icMenue           = itemView.findViewById(R.id.icMenue) ;
            tvDashFollow          = itemView.findViewById(R.id.tvDashFollow);

        }
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(list.size() - 1);

       // if (errorMsg != null) "errorMsg" = errorMsg;

    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt ,tvTaptoLoad ;
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
                    //mCallback.retryPageLoad();
                    break;
            }
        }
    }

    /*
       Helpers - Pagination  _________________________________________________________________________________________________
   */
    public void add(WhoToFollowResponse.Responce r) {
        list.add(r);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<WhoToFollowResponse.Responce> moveResults) {
        for (WhoToFollowResponse.Responce result : moveResults) {
            add(result);
        }
    }

    public void remove(WhoToFollowResponse.Responce r) {
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
        add(new WhoToFollowResponse.Responce());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list.size() - 1;
        WhoToFollowResponse.Responce result = getItem(position);

        if (result != null) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public WhoToFollowResponse.Responce getItem(int position) { return list.get(position);  }

    private void feedDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_follow);

        TextView tvReport = dialog.findViewById(R.id.tvReport);
        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void  OnItemClickListner(ClickListener mlistener){
        this.listener = mlistener;
    }

}
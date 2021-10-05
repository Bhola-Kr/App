package com.dreammedia.dreammedia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.model.DashBoardResponse
import com.dreammedia.dreammedia.model.FollowModel
import com.dreammedia.dreammedia.model.WhoToFollowResponse
import de.hdodenhof.circleimageview.CircleImageView

class WhoToFollow2Adapter(val list: List<DashBoardResponse.Randomuser>, val listener: WhoToFollow2Adapter.ClickListener, val contex: Context)
                                  : RecyclerView.Adapter<WhoToFollow2Adapter.ViewHolder>() {

    interface ClickListener { fun onClickListener(pos: Int , id : Int , list :DashBoardResponse.Randomuser  ) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhoToFollow2Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_follow , parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: WhoToFollow2Adapter.ViewHolder, position: Int) {

        try {

            holder.tvName.setText(list.get(position).fullname)
            holder.tvDesignation.setText(list.get(position).username)

            Glide.with(contex).load(list.get(position).profileImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_user_)
                .into(holder.imgCard)

            if (list.get(position).getmFllowing()){
                holder.tvFollow.setBackgroundResource(R.drawable.drawable_round_corner_solid)
                holder.tvFollow.setTextColor(contex.resources.getColor( R.color.white))
                holder.tvFollow.setText("UnFollow")
            }else{
                holder.tvFollow.setBackgroundResource(R.drawable.drawable_round_corner_boarder)
                holder.tvFollow.setTextColor(contex.resources.getColor( R.color.colorAccent))
                holder.tvFollow.setText("Follow")
            }

            holder.tvFollow.setOnClickListener { listener.onClickListener(position ,holder.tvFollow.id , list.get(position)) }

            holder.imgCard.setOnClickListener { listener.onClickListener(position ,holder.imgCard.id , list.get(position)) }

        } catch (e: Exception) { }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return 3}

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgCard        = itemView.findViewById(R.id.imgCard) as CircleImageView
        val tvName         = itemView.findViewById(R.id.tvName) as TextView
        val tvDesignation  = itemView.findViewById(R.id.tvDesignation) as TextView
        val tvFollow       = itemView.findViewById(R.id.tvFollow) as TextView

    }



}
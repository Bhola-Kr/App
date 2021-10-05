package com.dreammedia.dreammedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.dashboard.NotificationFragment
import com.dreammedia.dreammedia.model.NotificationResponse
import com.dreammedia.dreammedia.utlis.Util.DateTime
import com.makeramen.roundedimageview.RoundedImageView

class NotificationAdapter(val list: List<NotificationResponse.Responce>, val listener: ClickListener, val contex: NotificationFragment) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    interface ClickListener { fun onClickListener(pos: Int , id : Int , list :NotificationResponse.Responce  ) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_notification , parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    try {
            holder.tvName.setText(list.get(position).fullname)
            holder.tvDesignation.setText(list.get(position).msg)
            holder.tvTime.setText(list.get(position).added)

            Glide.with(contex).load(list.get(position).profileImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_user_)
                    .into(holder.imgcard)

        holder.itemView.setOnClickListener { listener.onClickListener(position ,holder.itemView.id , list.get(position)) }


    } catch (e: Exception) { }

    }
    //this method is giving the size of the list
    override fun getItemCount(): Int { return list.size}

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgcard              = itemView.findViewById(R.id.imgcard) as RoundedImageView
        val tvName               = itemView.findViewById(R.id.tvName) as TextView
        val tvDesignation        = itemView.findViewById(R.id.tvDesignation) as TextView
        val tvTime               = itemView.findViewById(R.id.tvTime) as TextView

    }

}
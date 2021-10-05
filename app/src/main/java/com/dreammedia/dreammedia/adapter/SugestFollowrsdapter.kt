package com.dreammedia.dreammedia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.model.DashBoardUserMainResponse
import de.hdodenhof.circleimageview.CircleImageView

class SugestFollowrsdapter(val list: List<DashBoardUserMainResponse.Alluser>, val listener: SugestFollowrsdapter.ClickListener, val contex: Context) : RecyclerView.Adapter<SugestFollowrsdapter.ViewHolder>() {

    interface ClickListener { fun onClickListener(pos: Int , id : Int , list :DashBoardUserMainResponse.Alluser ) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugestFollowrsdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_suggesst , parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: SugestFollowrsdapter.ViewHolder, position: Int) {

        try {

            holder.tvName.setText(list.get(position).fullname)
            holder.tvEmail.setText(list.get(position).email)

            Glide.with(contex).load(list.get(position).profileImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_user_)
                    .into(holder.ic1)

            if (list.get(position).getmFllowing()){
                holder.tvFollow.setBackgroundResource(R.drawable.drawable_rectangle_corner_solid)
                holder.tvFollow.setTextColor(contex.resources.getColor( R.color.white))
                holder.tvFollow.setText("Following")

            }else{
                holder.tvFollow.setBackgroundResource(R.drawable.drawable_rectangle_corner_boarder)
                holder.tvFollow.setTextColor(contex.resources.getColor( R.color.white))
                holder.tvFollow.setText("Follow")
            }

            holder.tvFollow.setOnClickListener { listener.onClickListener(position ,holder.tvFollow.id , list.get(position)) }

            holder.ic1.setOnClickListener { listener.onClickListener(position ,holder.ic1.id , list.get(position)) }

            holder.icCross.setOnClickListener { listener.onClickListener(position ,holder.icCross.id , list.get(position)) }

        } catch (e: Exception) { }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return list.size}

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ic1        = itemView.findViewById(R.id.ic1)     as CircleImageView
        val icCross    = itemView.findViewById(R.id.icCross)     as ImageView
        val tvName     = itemView.findViewById(R.id.tvName)  as TextView
        val tvEmail    = itemView.findViewById(R.id.tvEmail) as TextView
        val tvFollow   = itemView.findViewById(R.id.tvFollow) as TextView
    }

}
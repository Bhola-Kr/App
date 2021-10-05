package com.dreammedia.dreammedia.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.model.*
import com.makeramen.roundedimageview.RoundedImageView

class ColorAdapter(val list: MutableList<ColorModel>, val listener: ClickListener, var contex : Context) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {


    interface ClickListener { fun onClickListener(pos: Int , id : Int , list :ColorModel  ) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_color , parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ColorAdapter.ViewHolder, position: Int) {

        holder.icColor.setBackgroundResource(list.get(position).color)


        if (list.get(position).getmSelected()){
            holder.icSelect.setBackgroundResource(R.drawable.drawable_rectangle_corner_boarder_color)
            holder.icSelect.visibility =View.VISIBLE
        }else{
            holder.icSelect.visibility =View.GONE
        }


        holder.itemView.setOnClickListener {
            listener.onClickListener(position , holder.itemView.id , list.get(position))
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return list.size}

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icColor        = itemView.findViewById(R.id.icColor) as ImageView
        val icSelect       = itemView.findViewById(R.id.icSelect) as ImageView
    }

    /*
         Helpers - Pagination  _____________________________________________________________________
    */

}
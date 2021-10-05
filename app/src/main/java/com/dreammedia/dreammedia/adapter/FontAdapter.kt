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
import com.dreammedia.dreammedia.model.CategoryModel
import com.dreammedia.dreammedia.model.FollowModel
import com.dreammedia.dreammedia.model.FontModel
import com.dreammedia.dreammedia.model.TempletsListModel
import com.makeramen.roundedimageview.RoundedImageView

class FontAdapter(val list: MutableList<FontModel>, val listener: ClickListener, var contex : Context) : RecyclerView.Adapter<FontAdapter.ViewHolder>() {


    interface ClickListener { fun onClickListener(pos: Int , id : Int , list :FontModel  ) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_fonts , parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FontAdapter.ViewHolder, position: Int) {

      //  Log.e("myImagelist", "onBindViewHolder: " + list.get(position).image  )
        holder.tvFont.setTypeface(list.get(position).face)

        if (list.get(position).getmSelectd()){
            holder.tvFont.setBackgroundResource(R.drawable.drawable_rectangle_corner_solid_font_selected)
            holder.tvFont.setTextColor(Color.parseColor("#000000"))
        }else{
            holder.tvFont.setBackgroundResource(R.drawable.drawable_rectangle_corner_solid_font)
            holder.tvFont.setTextColor(Color.parseColor("#ffffff"))
        }

        holder.itemView.setOnClickListener {
            listener.onClickListener(position , holder.itemView.id , list.get(position))
        }


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return list.size}

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFont        = itemView.findViewById(R.id.tvFont) as TextView
    }

    /*
         Helpers - Pagination  _____________________________________________________________________
    */
    fun add(r: FontModel) {
        list.add(r)
        notifyItemInserted(list.size - 1)
    }

    fun addAll(moveResults: MutableList<FontModel>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: FontModel) {
        val position = list.indexOf(r)
        if (position > -1) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    fun isEmpty(): Boolean { return itemCount == 0 }

    fun getItem(position: Int): FontModel? { return list[position] }


    fun removeLoadingFooter() {
        val position = list.size - 1
        val result: FontModel? = getItem(position)
        if (result != null) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}
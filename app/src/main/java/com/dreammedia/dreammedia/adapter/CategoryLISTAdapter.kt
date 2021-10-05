package com.dreammedia.dreammedia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.model.CategoryModel
import com.dreammedia.dreammedia.model.FollowModel
import com.dreammedia.dreammedia.model.GetFollowingResponse

class CategoryLISTAdapter(val list: MutableList<CategoryModel.Responce> , val listener: ClickListener ,var contex : Context) : RecyclerView.Adapter<CategoryLISTAdapter.ViewHolder>() {


    interface ClickListener { fun onClickListener(pos: Int , id : Int , list :CategoryModel.Responce  ) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryLISTAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_category_items , parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryLISTAdapter.ViewHolder, position: Int) {

     holder.tvName.setText(list.get(position).title)

     Glide.with(contex).load(list.get(position).icon)
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .into(holder.imgIc)

        holder.lyCard.setOnClickListener {
          listener.onClickListener(position,holder.lyCard.id,list.get(position))
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return list.size}

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIc         = itemView.findViewById(R.id.imgIc)  as ImageView
        val tvName        = itemView.findViewById(R.id.tvName) as TextView
        val lyCard        = itemView.findViewById(R.id.lyCard) as LinearLayout
    }

    /*
      Helpers - Pagination  _________________________________________________________________________________________________
    */
    fun add(r: CategoryModel.Responce) {
        list.add(r)
        notifyItemInserted(list.size - 1)
    }

    fun addAll(moveResults: MutableList<CategoryModel.Responce>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: CategoryModel.Responce?) {
        val position = list.indexOf(r)
        if (position > -1) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean { return itemCount == 0 }

    fun addLoadingFooter() {
        add(CategoryModel.Responce())
    }

    fun getItem(position: Int): CategoryModel.Responce? { return list[position] }


    fun removeLoadingFooter() {
        val position = list.size - 1
        val result: CategoryModel.Responce? = getItem(position)
        if (result != null) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}
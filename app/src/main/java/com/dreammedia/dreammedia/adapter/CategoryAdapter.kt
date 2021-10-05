package com.dreammedia.dreammedia.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.model.CategoryModel
import com.dreammedia.dreammedia.model.FollowModel
import com.dreammedia.dreammedia.model.TempletsListModel
import com.makeramen.roundedimageview.RoundedImageView

class CategoryAdapter(val list: MutableList<TempletsListModel.Responce>, val listener: ClickListener, var contex : Context) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    interface ClickListener { fun onClickListener(pos: Int , id : Int , list :TempletsListModel.Responce  ) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_category , parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {

        Log.e("myImagelist", "onBindViewHolder: " + list.get(position).image  )

            Glide.with(contex).load(list.get(position).image)
                .into(holder.imageVIew)

        holder.imageVIew.setOnClickListener {
         listener.onClickListener(position , holder.imageVIew.id , list.get(position))
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return list.size}

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageVIew        = itemView.findViewById(R.id.imageVIew) as RoundedImageView
    }

    /*
         Helpers - Pagination  _____________________________________________________________________
    */
    fun add(r: TempletsListModel.Responce) {
        list.add(r)
        notifyItemInserted(list.size - 1)
    }

    fun addAll(moveResults: MutableList<TempletsListModel.Responce>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: TempletsListModel.Responce?) {
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
        add(TempletsListModel.Responce())
    }

    fun getItem(position: Int): TempletsListModel.Responce? { return list[position] }


    fun removeLoadingFooter() {
        val position = list.size - 1
        val result: TempletsListModel.Responce? = getItem(position)
        if (result != null) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}
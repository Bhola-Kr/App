package com.dreammedia.dreammedia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.model.SelectPhotoModel

class SelectedPhotoAdapter(val list: List<SelectPhotoModel>, val listener: SelectedPhotoAdapter.ClickListener, val contex: Context) : RecyclerView.Adapter<SelectedPhotoAdapter.ViewHolder>() {

    interface ClickListener { fun onClickListener(pos: Int , id : Int , list :SelectPhotoModel  ) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedPhotoAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_selected_photo , parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: SelectedPhotoAdapter.ViewHolder, position: Int) {

        try {
            holder.imgcard.setImageURI(list.get(position).path)
            holder.imgcard.setOnClickListener { listener.onClickListener(position ,holder.imgcard.id , list.get(position)) }
            holder.icRemoveImg.setOnClickListener { listener.onClickListener(position ,holder.icRemoveImg.id , list.get(position)) }
        } catch (e: Exception) {
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return list.size}

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgcard        = itemView.findViewById(R.id.imgcard) as ImageView
        val icRemoveImg    = itemView.findViewById(R.id.icRemoveImg) as ImageView
    }

}
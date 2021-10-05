package com.dreammedia.dreammedia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.model.FaqResponse

class FAQAdapter(val list: List<FaqResponse.Responce>, val listener: FAQAdapter.ClickListener, val contex: Context) : RecyclerView.Adapter<FAQAdapter.ViewHolder>() {

    interface ClickListener { fun onClickListener(pos: Int , id : Int , list :FaqResponse.Responce  ) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_faq , parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FAQAdapter.ViewHolder, position: Int) {

        try {
            holder.tvAnswer.setText(list.get(position).description)
            holder.tvQuestion.setText(list.get(position).title)

            if (list.get(position).getmVisable()){
                holder.tvAnswer.visibility = View.VISIBLE
                holder.dropDown.setImageResource(R.drawable.ic_top_arrow)
            }else{
                holder.tvAnswer.visibility = View.GONE
                holder.dropDown.setImageResource(R.drawable.ic_down_2)
            }

            holder.dropDown.setOnClickListener { listener.onClickListener(position ,holder.dropDown.id , list.get(position)) }
        } catch (e: Exception) {
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int { return list.size}

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dropDown        = itemView.findViewById(R.id.dropDown) as ImageView
        val tvQuestion      = itemView.findViewById(R.id.tvQuestion) as TextView
        val tvAnswer        = itemView.findViewById(R.id.tvAnswer) as TextView
    }


}
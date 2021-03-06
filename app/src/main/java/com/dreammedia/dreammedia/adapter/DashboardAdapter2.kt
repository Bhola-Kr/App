package com.dreammedia.dreammedia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.customWigits.viewPager.MyBounceInterpolator
import com.dreammedia.dreammedia.customWigits.viewPager.SliderIndicator
import com.dreammedia.dreammedia.customWigits.viewPager.SliderView
import com.dreammedia.dreammedia.customWigits.viewPager.readMore.ReadMoreTextView
import com.dreammedia.dreammedia.dashboard.ProfileFragment
import com.dreammedia.dreammedia.dashboard.dashboard.SlidingImage_Adapter
import com.dreammedia.dreammedia.model.DashBoardUserMainResponse
import com.dreammedia.dreammedia.utlis.Util
import de.hdodenhof.circleimageview.CircleImageView
import me.grantland.widget.AutofitTextView


class DashboardAdapter2(
    val list: MutableList<DashBoardUserMainResponse.Post>,
    val listener: ClickListener,
    val contex: ProfileFragment
) : RecyclerView.Adapter<DashboardAdapter2.ViewHolder>() {

    interface ClickListener {
        fun onClickListener(pos: Int, id: Int, list: DashBoardUserMainResponse.Post)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): DashboardAdapter2.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_dashboard, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: DashboardAdapter2.ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        if (list != null) {
            try {
                holder.name.setText(list.get(position).fullname)
                holder.tvTime.setText(Util.DateTime(list.get(position).created))
                holder.totalIkes.setText(list.get(position).totalLike.toString())
                // holder.tvTotalShare.setText(list.get(position).totalShare.toString())

                Glide.with(contex).load(list.get(position).profileImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_user_)
                    .into(holder.avtard)

                Glide.with(contex).load(Config.getUserProfile())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_user_)
                    .into(holder.avtard)

                holder.icLike.setOnClickListener { v ->
                    val myAnim =
                        AnimationUtils.loadAnimation(contex.requireContext(), R.anim.bounce)
                    val interpolator = MyBounceInterpolator(0.1, 20.0)
                    myAnim.interpolator = interpolator
                    v.startAnimation(myAnim)

                    holder.icLike.setImageResource(R.drawable.ic_like_selected)
                    //  list.get(position).setisLiked(true)
                    // list.get(position).totalLike() + 1)
                    holder.totalIkes.setText(list.get(position).totalLike.toString() + "")

                    listener.onClickListener(position, holder.icLike.id, list.get(position))

                }

                holder.icShare.setOnClickListener { v ->
                    val myAnim =
                        AnimationUtils.loadAnimation(contex.requireContext(), R.anim.bounce)
                    val interpolator = MyBounceInterpolator(0.1, 20.0)
                    myAnim.interpolator = interpolator
                    v.startAnimation(myAnim)

                    holder.icShare.setBackgroundResource(R.color.colorAccent)
                    listener.onClickListener(position, holder.icShare.id, list.get(position))
                }

                holder.tvDownload.setOnClickListener { v ->
                    holder.tvDownload.setText("loading..")
                    val myAnim =
                        AnimationUtils.loadAnimation(contex.requireContext(), R.anim.bounce)
                    val interpolator = MyBounceInterpolator(0.1, 20.0)
                    myAnim.interpolator = interpolator
                    v.startAnimation(myAnim)
                    holder.tvDownload.setBackgroundResource(R.color.colorAccent)
                    listener.onClickListener(position, holder.tvDownload.id, list.get(position))
                }
                if (list.get(position).description !=null){
                    holder.tvDescription.setText(list.get(position).description)
                    holder.tvDescription.visibility = View.VISIBLE
                }else{
                    holder.tvDescription.visibility = View.GONE
                }

                if (list.get(position).postImage.size != 0) {
                    holder.tvAutoFit.visibility = View.GONE

                    val IMAGES: MutableList<String> = ArrayList()
                    for (i in 0 until list.get(position).postImage.size) {
                        IMAGES.add(list.get(position).postImage.get(i))
                    }

                    // holder.postSliderView.setAdapter(SlidingImage_Adapter(contex.context, IMAGES ,))

                    var mIndicator = SliderIndicator(
                        holder.itemView.context,
                        holder.pagesContainer,
                        holder.postSliderView,
                        R.drawable.indicator_circle_dashboard
                    )
                    mIndicator.setPageCount(IMAGES.size)
                    mIndicator.show()

                    holder.postSliderView.setOnItemClickListener { mposition ->
                        try {
                            listener.onClickListener(
                                mposition,
                                holder.postSliderView.id,
                                list.get(position)
                            )
                        } catch (e: Exception) {
                        }

                    }

                } else {
                    holder.tvDescription.visibility = View.GONE
                    holder.tvAutoFit.visibility = View.VISIBLE
                    holder.tvAutoFit.setText(list.get(position).description)
                }

                holder.icMenue.setOnClickListener {

                    val popup = PopupMenu(holder.itemView.context, holder.icMenue)
                    popup.inflate(R.menu.listing_options_menu)

                    popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(item: MenuItem): Boolean {
                            when (item.itemId) {
                                R.id.item_report -> {
                                    listener.onClickListener(
                                        position,
                                        item.itemId,
                                        list.get(position)
                                    )
                                }
                                R.id.item_delete -> {
                                    listener.onClickListener(
                                        position,
                                        item.itemId,
                                        list.get(position)
                                    )
                                }
                            }
                            return false
                        }
                    })
                    //displaying the popup
                    //displaying the popup
                    popup.show()
                }

                //  holder.imagView.setOnClickListener { listener.onClickListener(holder.imagView.id) }
                holder.tvSeeAll.setOnClickListener {

                    listener.onClickListener(position, holder.tvSeeAll.id, list.get(position))

                }

                holder.avtard.setOnClickListener {
                    listener.onClickListener(position, holder.avtard.id, list.get(position))
                }
            } catch (e: Exception) {
            }
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return list.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lyWhoFollow = itemView.findViewById(R.id.lyWhoFollow) as LinearLayout
        val lyPost = itemView.findViewById(R.id.lyPost) as LinearLayout
        val lyadd = itemView.findViewById(R.id.lyadd) as ImageView
        val recycler = itemView.findViewById(R.id.WhoToFollowrecycler) as RecyclerView
        var icMenue = itemView.findViewById(R.id.icMenue) as ImageView

        // var imagView           = itemView.findViewById(R.id.imagView)      as ImageView
        var tvSeeAll = itemView.findViewById(R.id.tvSeeAll) as TextView
        var avtard = itemView.findViewById(R.id.avtard) as CircleImageView
        var name = itemView.findViewById(R.id.name) as TextView
        var tvDescription = itemView.findViewById(R.id.tvDescription) as ReadMoreTextView
        var tvTime = itemView.findViewById(R.id.tvTime) as TextView

        var postSliderView = itemView.findViewById(R.id.pager) as SliderView
        var pagesContainer = itemView.findViewById(R.id.pagesContainer) as LinearLayout
        var tvAutoFit = itemView.findViewById(R.id.tvAutoFit) as AutofitTextView

        var totalIkes = itemView.findViewById(R.id.totalIkes) as TextView
        var tvDownload = itemView.findViewById(R.id.tvDownload) as TextView
        var tvTotalShare = itemView.findViewById(R.id.tvTotalShare) as TextView
        var icShare = itemView.findViewById(R.id.icShare) as ImageView
        var icLike = itemView.findViewById(R.id.icLike) as ImageView

    }


    /*
        Helpers - Pagination  _________________________________________________________________________________________________
    */
    fun add(r: DashBoardUserMainResponse.Post) {
        list.add(r)
        notifyItemInserted(list.size - 1)
    }

    fun addAll(moveResults: List<DashBoardUserMainResponse.Post>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: DashBoardUserMainResponse.Post?) {
        val position = list.indexOf(r)
        if (position > -1) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeItem(positon: Int) {
        list.removeAt(positon)
        notifyDataSetChanged()
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun addLoadingFooter() {
        //  add(DashBoardUserMainResponse.Post())
    }

    fun removeLoadingFooter() {
        val position = list.size - 1
        val result: DashBoardUserMainResponse.Post? = getItem(position)
        if (result != null) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): DashBoardUserMainResponse.Post? {
        return list[position]
    }


}
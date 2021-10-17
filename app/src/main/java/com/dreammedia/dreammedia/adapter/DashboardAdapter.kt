package com.dreammedia.dreammedia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.customWigits.viewPager.MyBounceInterpolator
import com.dreammedia.dreammedia.customWigits.viewPager.SliderIndicator
import com.dreammedia.dreammedia.customWigits.viewPager.SliderView
import com.dreammedia.dreammedia.customWigits.viewPager.readMore.ReadMoreTextView
import com.dreammedia.dreammedia.dashboard.dashboard.SlidingImage_Adapter
import com.dreammedia.dreammedia.loginProcess.ProgressUtill
import com.dreammedia.dreammedia.model.DashBoardResponse
import com.dreammedia.dreammedia.utlis.Util.DateTime
import de.hdodenhof.circleimageview.CircleImageView
import me.grantland.widget.AutofitTextView

@Suppress("DEPRECATION")
class DashboardAdapter(
    val list: MutableList<DashBoardResponse.Post>,
    val listener: ClickListener,
    val contex: Context
) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    interface ClickListener {
        fun onClickListener(pos: Int, id: Int, list: DashBoardResponse.Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_dashboard, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: DashboardAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        try {
            holder.name.setText(list.get(position).fullname)
            holder.totalIkes.setText(list.get(position).totalLike.toString())
            // holder.tvTotalShare.setText(list.get(position).totalShare.toString())
            // holder.tvTime.setText(list.get(position).ge)

            holder.tvTime.setText(DateTime(list.get(position).created))

            if (list.get(position).getIs_like() == 0) {
                holder.icLike.setImageResource(R.drawable.ic_like)
            } else {
                holder.icLike.setImageResource(R.drawable.ic_like_selected)
            }

            Glide.with(contex).load(list.get(position).profileImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.avtard)

            holder.icLike.setOnClickListener { v ->
                val myAnim = AnimationUtils.loadAnimation(contex, R.anim.bounce)
                val interpolator = MyBounceInterpolator(0.1, 20.0)
                myAnim.interpolator = interpolator
                v.startAnimation(myAnim)

                holder.icLike.setImageResource(R.drawable.ic_like_selected)
                list.get(position).setisLiked(true)
                // list.get(position).totalLike() + 1)
                holder.totalIkes.setText(list.get(position).totalLike.toString() + "")

                listener.onClickListener(position, holder.icLike.id, list.get(position))

            }

            holder.icShare.setOnClickListener { v ->

                val myAnim = AnimationUtils.loadAnimation(contex, R.anim.bounce)
                val interpolator = MyBounceInterpolator(0.1, 20.0)
                myAnim.interpolator = interpolator
                v.startAnimation(myAnim)

                listener.onClickListener(position, holder.icShare.id, list.get(position))
            }

            holder.tvDownload.setOnClickListener { v ->

                holder.tvDownload.setText("loading..")
                val myAnim = AnimationUtils.loadAnimation(contex, R.anim.bounce)
                val interpolator = MyBounceInterpolator(0.1, 20.0)
                myAnim.interpolator = interpolator
                v.startAnimation(myAnim)
                listener.onClickListener(position, holder.tvDownload.id, list.get(position))

            }

            if (list.get(position).postImage.size != 0) {

                holder.tvDescription.visibility = View.VISIBLE

                holder.tvDescription.visibility = View.VISIBLE
                holder.tvAutoFit.visibility = View.GONE
                holder.tvDescription.setText(list.get(position).description)

                val IMAGES: MutableList<String> = ArrayList()
                for (i in 0 until list.get(position).postImage.size) {
                    IMAGES.add(list.get(position).postImage.get(i))
                }

                // holder.postSliderView.setAdapter(SlidingImage_Adapter(contex, IMAGES , list.get))

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


                var mIndicator = SliderIndicator(
                    holder.itemView.context, holder.pagesContainer,
                    holder.postSliderView, R.drawable.indicator_circle_dashboard
                )

                mIndicator.setPageCount(IMAGES.size)
                mIndicator.show()
                holder.tvDownload.visibility = View.VISIBLE

            } else {
                holder.tvDownload.visibility = View.GONE
                holder.tvDescription.visibility = View.GONE
                holder.tvAutoFit.visibility = View.VISIBLE
                holder.tvAutoFit.setText(list.get(position).description)
            }

            holder.icMenue.setOnClickListener {

                val popup = PopupMenu(holder.itemView.context, holder.icMenue)
                popup.inflate(R.menu.listing_options_menu)

                Config.init(contex)

                popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.item_report -> {
                                listener.onClickListener(position, item.itemId, list.get(position))
                            }
                            R.id.item_delete -> {

                                if (Config.getUserID().equals(list.get(position).userId)) {

                                } else {
                                    listener.onClickListener(
                                        position,
                                        item.itemId,
                                        list.get(position)
                                    )

                                }
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

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return list.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lyWhoFollow = itemView.findViewById(R.id.lyWhoFollow) as LinearLayout
        val lyPost = itemView.findViewById(R.id.lyPost) as LinearLayout
        val lyadd = itemView.findViewById(R.id.lyadd) as ImageView
        val recycler = itemView.findViewById(R.id.recycler) as RecyclerView
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
    fun add(r: DashBoardResponse.Post) {
        list.add(r)
        notifyItemInserted(list.size - 1)
    }

    fun addAll(moveResults: List<DashBoardResponse.Post>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: DashBoardResponse.Post?) {
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
        add(DashBoardResponse.Post())
    }

    fun removeLoadingFooter() {
        val position = list.size - 1
        val result: DashBoardResponse.Post? = getItem(position)
        if (result != null) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): DashBoardResponse.Post? {
        return list[position]
    }


}
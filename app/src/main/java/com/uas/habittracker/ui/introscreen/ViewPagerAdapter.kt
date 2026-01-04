package com.uas.habittracker.ui.introscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uas.habittracker.R
import com.uas.habittracker.data.models.IntroView

class ViewPagerAdapter(introViews: List<IntroView>): RecyclerView.Adapter<ViewPagerAdapter.IntroViewHolder>() {
    private val list = introViews
    class IntroViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IntroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.intro_item_page, parent, false)
        return IntroViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: IntroViewHolder,
        position: Int
    ) {
        val currentView = list[position]

        holder.itemView.findViewById<ImageView>(R.id.iv_image_intro).setImageResource(currentView.imageId)
        holder.itemView.findViewById<TextView>(R.id.tv_description_intro).text = currentView.description
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
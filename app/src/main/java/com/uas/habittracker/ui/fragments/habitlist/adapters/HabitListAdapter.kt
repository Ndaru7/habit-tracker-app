package com.uas.habittracker.ui.fragments.habitlist.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.uas.habittracker.R
import com.uas.habittracker.data.models.Habit
import com.uas.habittracker.ui.fragments.habitlist.HabitListDirections
import com.uas.habittracker.utils.Calculations

class HabitListAdapter: RecyclerView.Adapter<HabitListAdapter.MyViewHolder>() {
    var habitList = emptyList<Habit>()
    val TAG = "HabitListAdapter"
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<CardView>(R.id.cv_cardView).setOnClickListener {
                val position = absoluteAdapterPosition
                Log.d(TAG, "Item clicked at: $position")
                Log.d(TAG, "ID: ${habitList[position].id}")

                val action = HabitListDirections.actionHabitListToUpdateHabitItem(habitList[position])
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HabitListAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_habit_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HabitListAdapter.MyViewHolder, position: Int) {
        val currentHabit = habitList[position]

        holder.itemView.findViewById<ImageView>(R.id.iv_habit_icon)
        holder.itemView.findViewById<TextView>(R.id.tv_item_title).text = currentHabit.habitTitle
        holder.itemView.findViewById<TextView>(R.id.tv_item_description).text = currentHabit.habitDescription
        holder.itemView.findViewById<TextView>(R.id.tv_timeElapsed).text = Calculations.calculateTimeBetweenDates(currentHabit.habitStartTime)
        holder.itemView.findViewById<TextView>(R.id.tv_item_createdTimeStamp).text = "Since: ${currentHabit.habitStartTime}"
    }

    override fun getItemCount(): Int {
        return  habitList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(habit: List<Habit>) {
        this.habitList = habit
        notifyDataSetChanged()
    }
}
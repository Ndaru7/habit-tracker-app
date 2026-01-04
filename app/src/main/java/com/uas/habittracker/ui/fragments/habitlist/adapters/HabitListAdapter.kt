package com.uas.habittracker.ui.fragments.habitlist.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.uas.habittracker.data.models.Habit
import com.uas.habittracker.databinding.RecyclerHabitItemBinding
import com.uas.habittracker.ui.fragments.habitlist.HabitListDirections
import com.uas.habittracker.utils.Calculations

class HabitListAdapter(private val onItemClick: (Habit) -> Unit): RecyclerView.Adapter<HabitListAdapter.MyViewHolder>() {

    var habitList = emptyList<Habit>()
    val TAG = "HabitListAdapter"
    inner class MyViewHolder(private val binding: RecyclerHabitItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(habit: Habit) {
            binding.ivHabitIcon.setImageResource(habit.imageId)
            binding.tvItemTitle.text = habit.habitTitle
            binding.tvItemDescription.text = habit.habitDescription
            binding.tvTimeElapsed.text = Calculations.calculateTimeBetweenDates(habit.habitStartTime)
            binding.tvItemCreatedTimeStamp.text = "Since: ${habit.habitStartTime}"

            binding.cvCardView.setOnClickListener {
                // onItemClick(habit)
                // val position = adapterPosition -> Deprecated
                val position = absoluteAdapterPosition
                Log.d(TAG, "Item clicked at: $position")
                Log.d(TAG, "ID ${habitList[position].id}")

                val action = HabitListDirections.actionHabitListToUpdateHabitItem(habitList[position])
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HabitListAdapter.MyViewHolder {
        val binding = RecyclerHabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitListAdapter.MyViewHolder, position: Int) {
        val currentHabit = habitList[position]

        holder.bind(currentHabit)
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
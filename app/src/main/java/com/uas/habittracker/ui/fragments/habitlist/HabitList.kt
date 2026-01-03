package com.uas.habittracker.ui.fragments.habitlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.uas.habittracker.R
import com.uas.habittracker.databinding.FragmentHabitListBinding

class HabitList : Fragment(R.layout.fragment_habit_list) {

    private var _binding: FragmentHabitListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHabitListBinding.bind(view)

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_habitList_to_createHabitItem)
        }
    }
}
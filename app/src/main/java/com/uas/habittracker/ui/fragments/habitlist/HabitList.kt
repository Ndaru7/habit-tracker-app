package com.uas.habittracker.ui.fragments.habitlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.uas.habittracker.R
import com.uas.habittracker.data.models.Habit
import com.uas.habittracker.databinding.FragmentHabitListBinding
import com.uas.habittracker.ui.fragments.habitlist.adapters.HabitListAdapter
import com.uas.habittracker.ui.viewmodels.HabitViewModel
import androidx.core.view.MenuProvider


class HabitList : Fragment(R.layout.fragment_habit_list) {

    private lateinit var habitList: List<Habit>
    private lateinit var habitViewModel: HabitViewModel
    private lateinit var adapter: HabitListAdapter

    private var _binding: FragmentHabitListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHabitListBinding.bind(view)

        // Adapter
        adapter = HabitListAdapter { habit ->
            val action = HabitListDirections.actionHabitListToUpdateHabitItem(habit)
            findNavController().navigate(action)
        }

        binding.rvHabits.adapter = adapter
        binding.rvHabits.layoutManager = LinearLayoutManager(context)

        // View Model
        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)
        habitViewModel.getAllHabits.observe(viewLifecycleOwner, {
            adapter.setData(it)
            habitList = it

            if (it.isEmpty()) {
                binding.rvHabits.visibility = View.GONE
                binding.tvEmptyView.visibility = View.VISIBLE
            } else {
                binding.rvHabits.visibility = View.VISIBLE
                binding.tvEmptyView.visibility = View.GONE
            }
        })

        //setHasOptionsMenu(true) // -> Deprecated
        // Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.nav_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.nav_delete -> {
                        habitViewModel.deleteAllHabits()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.swipeToRefresh.setOnClickListener {
            adapter.setData(habitList)
            binding.swipeToRefresh.isRefreshing = false
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_habitList_to_createHabitItem)
        }
    }
}
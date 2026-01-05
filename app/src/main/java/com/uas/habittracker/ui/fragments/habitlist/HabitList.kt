package com.uas.habittracker.ui.fragments.habitlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.uas.habittracker.R
import com.uas.habittracker.data.models.Habit
import com.uas.habittracker.ui.fragments.habitlist.adapters.HabitListAdapter
import com.uas.habittracker.ui.viewmodels.HabitViewModel
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HabitList : Fragment() {

    private lateinit var habitList: List<Habit>
    private lateinit var habitViewModel: HabitViewModel
    private lateinit var adapter: HabitListAdapter
    private lateinit var view: View
    private lateinit var rvHabits: RecyclerView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var tvEmptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_habit_list, null)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvHabits = view.findViewById(R.id.rv_habits)
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)
        fabAdd = view.findViewById(R.id.fab_add)
        tvEmptyView = view.findViewById(R.id.tv_emptyView)

        // Adapter
        adapter = HabitListAdapter()

        // With findViewById()
        rvHabits.adapter = adapter
        rvHabits.layoutManager = LinearLayoutManager(context)

        // View Model
        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)
        habitViewModel.getAllHabits.observe(viewLifecycleOwner, {
            adapter.setData(it)
            habitList = it

            if (it.isEmpty()) {
                rvHabits.visibility = View.GONE
                tvEmptyView.visibility = View.VISIBLE
            } else {
                rvHabits.visibility = View.VISIBLE
                tvEmptyView.visibility = View.GONE
            }
        })

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

        swipeToRefresh.setOnClickListener {
            adapter.setData(habitList)
            swipeToRefresh.isRefreshing = false
        }

        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_habitList_to_createHabitItem)
        }
    }
}
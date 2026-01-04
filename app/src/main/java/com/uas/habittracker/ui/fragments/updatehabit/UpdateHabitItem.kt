package com.uas.habittracker.ui.fragments.updatehabit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.uas.habittracker.R
import com.uas.habittracker.data.models.Habit
import com.uas.habittracker.databinding.FragmentUpdateHabitItemBinding
import com.uas.habittracker.ui.viewmodels.HabitViewModel
import com.uas.habittracker.utils.Calculations

class UpdateHabitItem : Fragment(R.layout.fragment_update_habit_item), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentUpdateHabitItemBinding? = null
    private val binding get() = _binding!!

    private var title = ""
    private var description = ""
    private var drawableSelected = 0
    private var timeStamp = ""

    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    private var cleanDate = ""
    private var cleanTime = ""

    private lateinit var habitViewModel: HabitViewModel
    private val args by navArgs<UpdateHabitItemArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentUpdateHabitItemBinding.bind(view)

        // Option Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.single_item_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.nav_delete -> {
                        deleteHabit(args.selectedHabit)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)

        binding.etHabitTitleUpdate.setText(args.selectedHabit.habitTitle)
        binding.etHabitDescriptionUpdate.setText(args.selectedHabit.habitDescription)

        drawableSelected()
        pickDateAndTime()

        binding.btnConfirmUpdate.setOnClickListener {
            updateHabit()
        }

    }

    private fun updateHabit() {
        title = binding.etHabitTitleUpdate.text.toString()
        description = binding.etHabitDescriptionUpdate.text.toString()
        timeStamp = "$cleanDate $cleanTime"

        if (!(title.isEmpty() || description.isEmpty() || timeStamp.isEmpty() || drawableSelected == 0)) {
            val habit = Habit(args.selectedHabit.id, title, description, timeStamp, drawableSelected)
            habitViewModel.updateHabit(habit)
            Toast.makeText(context, "Habit updated successfully!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateHabitItem_to_habitList)
        } else {
            Toast.makeText(context, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawableSelected() {
        binding.ivFastFoodSelectedUpdate.setOnClickListener {
            binding.ivFastFoodSelectedUpdate.isSelected = !binding.ivFastFoodSelectedUpdate.isSelected
            drawableSelected = R.drawable.ic_fastfood_filled

            binding.ivSmokingSelectedUpdate.isSelected = false
            binding.ivTeaSelectedUpdate.isSelected = false
        }

        binding.ivSmokingSelectedUpdate.setOnClickListener {
            binding.ivSmokingSelectedUpdate.isSelected = !binding.ivSmokingSelectedUpdate.isSelected
            drawableSelected = R.drawable.ic_smoke_filled

            binding.ivTeaSelectedUpdate.isSelected = false
            binding.ivFastFoodSelectedUpdate.isSelected = false
        }

        binding.ivTeaSelectedUpdate.setOnClickListener {
            binding.ivTeaSelectedUpdate.isSelected = !binding.ivTeaSelectedUpdate.isSelected
            drawableSelected = R.drawable.ic_tea_filled

            binding.ivSmokingSelectedUpdate.isSelected = false
            binding.ivFastFoodSelectedUpdate.isSelected = false
        }
    }

    private fun pickDateAndTime() {
        binding.btnPickDateUpdate.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        binding.btnPickTimeUpdate.setOnClickListener {
            getTimeCalendar()
            TimePickerDialog(context, this, hour, minute, true).show()
        }
    }

    override fun onTimeSet(
        view: TimePicker?,
        hourOfDay: Int,
        minuteX: Int
    ) {
        cleanTime = Calculations.cleanTime(hourOfDay, minuteX)
        binding.tvTimeSelectedUpdate.text = "Time: $cleanTime"
    }

    override fun onDateSet(
        view: DatePicker?,
        yearX: Int,
        monthX: Int,
        dayX: Int
    ) {
        cleanDate = Calculations.cleanDate(dayX, monthX, yearX)
        binding.tvDateSelectedUpdate.text = "Date: $cleanDate"
    }

    private fun getTimeCalendar() {
        val cal: Calendar = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun getDateCalendar() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun deleteHabit(habit: Habit) {
        habitViewModel.deleteHabit(habit)
        Toast.makeText(context, "Habit successfully deleted!", Toast.LENGTH_SHORT).show()

        findNavController().navigate(R.id.action_updateHabitItem_to_habitList)
    }
}
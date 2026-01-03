package com.uas.habittracker.ui.fragments.createhabit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.uas.habittracker.R
import com.uas.habittracker.data.models.Habit
import com.uas.habittracker.databinding.FragmentCreateHabitItemBinding
import com.uas.habittracker.databinding.FragmentHabitListBinding
import com.uas.habittracker.ui.viewmodels.HabitViewModel
import com.uas.habittracker.utils.Calculations


class CreateHabitItem : Fragment(R.layout.fragment_create_habit_item),
TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentCreateHabitItemBinding? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateHabitItemBinding.bind(view)

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)

        binding.btnConfirm.setOnClickListener {
            addHabitToDB()
        }

        pickDateAndTime()

        drawableSelected()
    }

    private fun addHabitToDB() {
        title = binding.etHabitTitle.text.toString()
        description = binding.etHabitDescription.text.toString()

        timeStamp = "$cleanDate $cleanTime"

        if (!(title.isEmpty() || description.isEmpty() || timeStamp.isEmpty() || drawableSelected == 0)) {
            val habit = Habit(0, title, description, timeStamp, drawableSelected)

            habitViewModel.addHabit(habit)
            Toast.makeText(context, "Habit created successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Please fill all field", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawableSelected() {
        binding.ivFastFoodSelected.setOnClickListener {
            binding.ivFastFoodSelected.isSelected = !binding.ivFastFoodSelected.isSelected
            drawableSelected = R.drawable.ic_fastfood_filled

            binding.ivSmokingSelected.isSelected = false
            binding.ivTeaSelected.isSelected = false
        }

        binding.ivSmokingSelected.setOnClickListener {
            binding.ivSmokingSelected.isSelected = !binding.ivSmokingSelected.isSelected
            drawableSelected = R.drawable.ic_smoke_filled

            binding.ivTeaSelected.isSelected = false
            binding.ivFastFoodSelected.isSelected = false
        }

        binding.ivTeaSelected.setOnClickListener {
            binding.ivTeaSelected.isSelected = !binding.ivTeaSelected.isSelected
            drawableSelected = R.drawable.ic_tea_filled

            binding.ivSmokingSelected.isSelected = false
            binding.ivFastFoodSelected.isSelected = false
        }
    }

    private fun pickDateAndTime() {
        binding.btnPickDate.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        binding.btnPickTime.setOnClickListener {
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
        binding.tvTimeSelected.text = "Time: $cleanTime"
    }

    override fun onDateSet(
        view: DatePicker?,
        yearX: Int,
        monthX: Int,
        dayX: Int
    ) {
        cleanDate = Calculations.cleanDate(dayX, monthX, yearX)
        binding.tvDateSelected.text = "Date: $cleanDate"
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
}
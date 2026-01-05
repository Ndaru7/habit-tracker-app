package com.uas.habittracker.ui.fragments.createhabit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.uas.habittracker.R
import com.uas.habittracker.data.models.Habit
import com.uas.habittracker.ui.viewmodels.HabitViewModel
import com.uas.habittracker.utils.Calculations


class CreateHabitItem : Fragment(),
TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private lateinit var view: View
    private lateinit var btnConfirm: Button
    private lateinit var etHabitTitle: EditText
    private lateinit var etHabitDescription: EditText
    private lateinit var ivFastFoodSelected: ImageView
    private lateinit var ivSmokeSelected: ImageView
    private lateinit var ivTeaSelected: ImageView
    private lateinit var btnPickDate: Button
    private lateinit var btnPickTime: Button
    private lateinit var tvDateSelected: TextView
    private lateinit var tvTimeSelected: TextView
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_create_habit_item, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)

        btnConfirm = view.findViewById(R.id.btn_confirm)
        etHabitTitle = view.findViewById(R.id.et_habitTitle)
        etHabitDescription = view.findViewById(R.id.et_habitDescription)
        ivFastFoodSelected = view.findViewById(R.id.iv_fastFoodSelected)
        ivSmokeSelected = view.findViewById(R.id.iv_smokingSelected)
        ivTeaSelected = view.findViewById(R.id.iv_teaSelected)
        btnPickDate = view.findViewById(R.id.btn_pickDate)
        btnPickTime = view.findViewById(R.id.btn_pickTime)
        tvDateSelected = view.findViewById(R.id.tv_dateSelected)
        tvTimeSelected = view.findViewById(R.id.tv_timeSelected)

        btnConfirm.setOnClickListener {
            addHabitToDB()
        }

        pickDateAndTime()

        drawableSelected()
    }

    private fun addHabitToDB() {
        title = etHabitTitle.text.toString()
        description = etHabitDescription.text.toString()

        timeStamp = "$cleanDate $cleanTime"

        if (!(title.isEmpty() || description.isEmpty() || timeStamp.isEmpty() || drawableSelected == 0)) {
            val habit = Habit(0, title, description, timeStamp, drawableSelected)

            habitViewModel.addHabit(habit)
            Toast.makeText(context, "Habit created successfully!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_createHabitItem_to_habitList)
        } else {
            Toast.makeText(context, "Please fill all field", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawableSelected() {
        ivFastFoodSelected.setOnClickListener {
            ivFastFoodSelected.isSelected = !ivFastFoodSelected.isSelected
            drawableSelected = R.drawable.ic_fastfood_filled

            ivSmokeSelected.isSelected = false
            ivTeaSelected.isSelected = false
        }

        ivSmokeSelected.setOnClickListener {
            ivSmokeSelected.isSelected = !ivSmokeSelected.isSelected
            drawableSelected = R.drawable.ic_smoke_filled

            ivTeaSelected.isSelected = false
            ivFastFoodSelected.isSelected = false
        }

        ivTeaSelected.setOnClickListener {
            ivTeaSelected.isSelected = !ivTeaSelected.isSelected
            drawableSelected = R.drawable.ic_tea_filled

            ivSmokeSelected.isSelected = false
            ivFastFoodSelected.isSelected = false
        }
    }

    private fun pickDateAndTime() {
        btnPickDate.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        btnPickTime.setOnClickListener {
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
        tvTimeSelected.text = "Time: $cleanTime"
    }

    override fun onDateSet(
        view: DatePicker?,
        yearX: Int,
        monthX: Int,
        dayX: Int
    ) {
        cleanDate = Calculations.cleanDate(dayX, monthX, yearX)
        tvDateSelected.text = "Date: $cleanDate"
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
package com.uas.habittracker.ui.fragments.updatehabit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.uas.habittracker.ui.viewmodels.HabitViewModel
import com.uas.habittracker.utils.Calculations

class UpdateHabitItem : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private lateinit var view: View
    private lateinit var btnConfirmUpdate: Button
    private lateinit var etHabitTitleUpdate: EditText
    private lateinit var etHabitDescriptionUpdate: EditText
    private lateinit var ivFastFoodSelectedUpdate: ImageView
    private lateinit var ivSmokeSelectedUpdate: ImageView
    private lateinit var ivTeaSelectedUpdate: ImageView
    private lateinit var btnPickDateUpdate: Button
    private lateinit var btnPickTimeUpdate: Button
    private lateinit var tvDateSelectedUpdate: TextView
    private lateinit var tvTimeSelectedUpdate: TextView
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_update_habit_item, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)

        btnConfirmUpdate = view.findViewById(R.id.btn_confirm_update)
        etHabitTitleUpdate = view.findViewById(R.id.et_habitTitle_update)
        etHabitDescriptionUpdate = view.findViewById(R.id.et_habitDescription_update)
        ivFastFoodSelectedUpdate = view.findViewById(R.id.iv_fastFoodSelected_update)
        ivSmokeSelectedUpdate = view.findViewById(R.id.iv_smokingSelected_update)
        ivTeaSelectedUpdate = view.findViewById(R.id.iv_teaSelected_update)
        btnPickDateUpdate = view.findViewById(R.id.btn_pickDate_update)
        btnPickTimeUpdate = view.findViewById(R.id.btn_pickTime_update)
        tvDateSelectedUpdate = view.findViewById(R.id.tv_dateSelected_update)
        tvTimeSelectedUpdate = view.findViewById(R.id.tv_timeSelected_update)

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

        etHabitTitleUpdate.setText(args.selectedHabit.habitTitle)
        etHabitDescriptionUpdate.setText(args.selectedHabit.habitDescription)

        drawableSelected()
        pickDateAndTime()

        btnConfirmUpdate.setOnClickListener {
            updateHabit()
        }

    }

    private fun updateHabit() {
        title = etHabitTitleUpdate.text.toString()
        description = etHabitDescriptionUpdate.text.toString()
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
        ivFastFoodSelectedUpdate.setOnClickListener {
            ivFastFoodSelectedUpdate.isSelected = !ivFastFoodSelectedUpdate.isSelected
            drawableSelected = R.drawable.ic_fastfood_filled

            ivSmokeSelectedUpdate.isSelected = false
            ivTeaSelectedUpdate.isSelected = false
        }

        ivSmokeSelectedUpdate.setOnClickListener {
            ivSmokeSelectedUpdate.isSelected = !ivSmokeSelectedUpdate.isSelected
            drawableSelected = R.drawable.ic_smoke_filled

            ivTeaSelectedUpdate.isSelected = false
            ivFastFoodSelectedUpdate.isSelected = false
        }

        ivTeaSelectedUpdate.setOnClickListener {
            ivTeaSelectedUpdate.isSelected = !ivTeaSelectedUpdate.isSelected
            drawableSelected = R.drawable.ic_tea_filled

            ivSmokeSelectedUpdate.isSelected = false
            ivFastFoodSelectedUpdate.isSelected = false
        }
    }

    private fun pickDateAndTime() {
        btnPickDateUpdate.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        btnPickTimeUpdate.setOnClickListener {
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
        tvTimeSelectedUpdate.text = "Time: $cleanTime"
    }

    override fun onDateSet(
        view: DatePicker?,
        yearX: Int,
        monthX: Int,
        dayX: Int
    ) {
        cleanDate = Calculations.cleanDate(dayX, monthX, yearX)
        tvDateSelectedUpdate.text = "Date: $cleanDate"
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
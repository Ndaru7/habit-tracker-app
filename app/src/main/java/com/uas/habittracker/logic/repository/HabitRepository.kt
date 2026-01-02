package com.uas.habittracker.logic.repository

import androidx.lifecycle.LiveData
import com.uas.habittracker.data.models.Habit
import com.uas.habittracker.logic.dao.HabitDao

class HabitRepository(private val habitDao: HabitDao) {
    val getAllHabits: LiveData<List<Habit>> = habitDao.getAllHabits()

    suspend fun addHabit(habit: Habit) {
        habitDao.addHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun deleteAllHabit() {
        habitDao.deleteAll()
    }

}
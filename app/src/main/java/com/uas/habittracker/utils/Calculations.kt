package com.uas.habittracker.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Calculations {

    fun calculateTimeBetweenDates(startDate: String): String {
        val endDate: String = timeStampToString(System.currentTimeMillis())
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date1: Date? = sdf.parse(startDate)
        val date2: Date? = sdf.parse(endDate)

        var isNegative = false

        var difference: Long = date2!!.time - date1!!.time

        if (difference < 0) {
            difference = -(difference)
            isNegative = true
        }

        val minutes: Long = difference / 60 / 1000
        val hours: Long = difference / 60 / 1000 / 60
        val days: Long = (difference / 60 / 1000 / 60) / 24
        val months: Long = (difference / 60 / 1000 / 60) / 24 / (365 / 12)
        val years: Long = difference / 60 / 1000 / 60 / 24 / 365

        if (isNegative) {
            return when {
                minutes < 240 -> "Starts in $minutes minutes"
                hours < 48 -> "Starts in $hours hours"
                days < 61 -> "Starts in $days days"
                months < 24 -> "Starts in $months months"
                else -> "Starts in $years years"
            }
        }

        return when {
            minutes < 240 -> "$minutes minutes ago"
            hours < 48 -> "$hours hours ago"
            days < 61 -> "$days days ago"
            months < 24 -> "$months months ago"
            else -> "$years years ago"
        }
    }

    private fun timeStampToString(timestamp: Long): String {
        /*val stamp = Timestamp(timestamp)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date: String = sdf.format((Date(stamp.time)))

        return date.toString()*/
        val sdf = SimpleDateFormat("dd/MM/yyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun cleanDate(_day: Int, _month: Int, _year: Int): String {
        var day: String = _day.toString()
        var month: String = _month.toString()

        if (_day < 10) {
            day = "0$_day"
        }

        if (_month < 9) {
            month = "0${_month + 1}"
        }
        return "$day/$month/$_year"
    }

    fun cleanTime(_hour: Int, _minute: Int): String {
        var hour = _hour.toString()
        var minute = _minute.toString()

        if (_hour < 10) {
            hour = "0$_hour"
        }

        if (_minute < 10) {
            minute = "0$_minute"
        }
        return "$hour:$minute"
    }
}
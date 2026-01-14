package com.example.mybuddy.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

object DateUtil {

    fun getStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getEndOfDay(timestamp: Long): Long {
        return getStartOfDay(timestamp) + 24 * 60 * 60 * 1000 - 1
    }

    fun formatDate(timestamp: Long, pattern: String = "dd MMM yyyy"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun formatMonthYear(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun getFirstDayOfWeek(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        return calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0 = Sunday
    }

    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        return getStartOfDay(timestamp1) == getStartOfDay(timestamp2)
    }

    fun isToday(timestamp: Long): Boolean {
        return isSameDay(timestamp, System.currentTimeMillis())
    }

    fun currentWeekDates(): List<LocalDate> {
        val today = LocalDate.now()
        val monday = today.with(java.time.DayOfWeek.MONDAY)

        return (0..6).map { monday.plusDays(it.toLong()) }
    }
}
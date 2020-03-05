package com.myozawoo.mmcalendar.view

import android.content.Context
import android.util.Log
import android.view.View
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.DateListener
import com.myozawoo.mmcalendar.R
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.WeekFields
import java.util.*

class MonthView (context: Context,
                 private var month: CalendarDay,
                 private val listener: DateListener)
    : CalendarView(context, month, DayOfWeek.of(7), listener) {

    override fun buildDayViews(dayViews: Collection<DayView>, calendar: LocalDate) {
        var temp = calendar
        for (r in 0 until DEFAULT_MAX_WEEK) {
            for (i in 0 until DEFAULT_DAYS_IN_WEEK) {
                addDayView(dayViews, temp, i == 0 || i == 6)
                temp = temp.plusDays(1)
            }
        }
    }

    override fun isDayEnabled(day: CalendarDay): Boolean {
        return day.getMonth() == getFirstViewDay().getMonth()
    }

    override fun getRows(): Int {
        return DEFAULT_MAX_WEEK
    }

    fun getMonth(): CalendarDay {
        return getFirstViewDay()
    }

    private fun showLog(message: String) {
        Log.d(javaClass.simpleName, message)
    }


}
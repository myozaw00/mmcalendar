package com.myozawoo.mmcalendar.format

import org.threeten.bp.DayOfWeek

interface WeekDayFormatter {

    companion object {
        val DEFAULT: WeekDayFormatter = CalendarWeekDayFormatter()
    }
    fun format(dayOfWeek: DayOfWeek): CharSequence


}
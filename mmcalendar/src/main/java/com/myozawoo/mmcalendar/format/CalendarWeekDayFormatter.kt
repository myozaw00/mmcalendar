package com.myozawoo.mmcalendar.format

import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.TextStyle
import java.util.*

class CalendarWeekDayFormatter : WeekDayFormatter {

    override fun format(dayOfWeek: DayOfWeek): CharSequence {
        return when(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())) {
            "Sun" -> "SUN\n"+"နွေ"
            "Mon" -> "MON\n"+"လာ"
            "Tue" -> "TUE\n"+"ဂါ"
            "Wed" -> "WED\n"+"ဟူး"
            "Thu" -> "THU\n"+"တေး"
            "Fri" -> "FRI\n"+"သော"
            "Sat" -> "SAT\n"+"နေ"
            else -> ""
        }
    }
}
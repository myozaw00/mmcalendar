package com.myozawoo.mmcalendar.format

import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.TextStyle
import java.util.*

class CalendarWeekDayFormatter : WeekDayFormatter {

    override fun format(dayOfWeek: DayOfWeek): CharSequence {
        return when(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())) {
            "Sun" -> "Sun\n"+"နွေ"
            "Mon" -> "Mon\n"+"လာ"
            "Tue" -> "Tue\n"+"ဂါ"
            "Wed" -> "Wed\n"+"ဟူး"
            "Thu" -> "Thu\n"+"တေး"
            "Fri" -> "Fri\n"+"သော"
            "Sat" -> "Sat\n"+"နေ"
            else -> ""
        }
    }
}
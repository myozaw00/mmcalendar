package com.myozawoo.mmcalendar.format

import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.TextStyle
import java.util.*

class CalendarWeekDayFormatter : WeekDayFormatter {

    override fun format(dayOfWeek: DayOfWeek): CharSequence {
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }
}
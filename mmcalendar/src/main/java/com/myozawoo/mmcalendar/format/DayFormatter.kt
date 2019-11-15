package com.myozawoo.mmcalendar.format

import com.myozawoo.mmcalendar.CalendarDay
import androidx.annotation.NonNull



interface DayFormatter {

    companion object {

        val DEFAULT_FORMAT = "d"

        val DEFAULT: DayFormatter = DateFormatDayFormatter()
    }

    fun format(day: CalendarDay): String

}


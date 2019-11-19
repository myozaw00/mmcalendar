package com.myozawoo.mmcalendar.format

import com.myozawoo.mmcalendar.CalendarDay


interface DayFormatter {

    /**
     * Format a given day into a string
     *
     * @param day the day
     * @return a label for the day
     */
    fun format(day: CalendarDay): String

    companion object {

        /**
         * Default format for displaying the day.
         */
        val DEFAULT_FORMAT = "d"

        /**
         * Default implementation used by [com.prolificinteractive.materialcalendarview.MaterialCalendarView]
         */
        val DEFAULT: DayFormatter = DateFormatDayFormatter()
    }
}


package com.myozawoo.mmcalendar.format

import com.myozawoo.mmcalendar.CalendarDay
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class DateFormatDayFormatter: DayFormatter {


    private lateinit var dateFormat: DateTimeFormatter

    constructor() {
        dateFormat = DateTimeFormatter.ofPattern(DEFAULT_FORMAT, Locale.getDefault())
    }

    constructor(dateFormat: DateTimeFormatter) {
        this.dateFormat = dateFormat
    }

    override fun format(day: CalendarDay): String {
        return dateFormat.format(day.getDate())
    }
}
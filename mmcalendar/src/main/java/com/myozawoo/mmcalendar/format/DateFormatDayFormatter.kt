package com.myozawoo.mmcalendar.format

import android.util.Log
import com.myozawoo.mmcalendar.CalendarDay
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import com.myozawoo.mmcalendar.format.DayFormatter.Companion.DEFAULT_FORMAT
import mmcalendar.*


/**
 * Format using a [DateFormat] instance.
 */
class DateFormatDayFormatter
/**
 * Format using a specific [DateFormat]
 *
 * @param format the format to use
 */
@JvmOverloads constructor(
    private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(
        "d",
        Locale.getDefault()
    )
) : DayFormatter {

    /**
     * {@inheritDoc}
     */
    override fun format(day: CalendarDay): String {
        /**
         * 0 - La San
         * 1 - La Pyae
         * 2 - La Sote
         * 3 - La Kwel
         */
        val mmDate = MyanmarDateConverter.convert(day.getYear(), day.getMonth(), day.getDay())
        showLog(mmDate.toString())
        showLog("${day.getYear()}-${day.getMonth()}-${day.getDay()}")
        var date = dateFormat.format(day.getDate())
        date += "\n${mmDate.fortnightDay}"
        if (mmDate.fortnightDayInt == 1) {
            date += "\n${mmDate.moonPhase}"
        }

        if (mmDate.moonPhrase == 1 || mmDate.moonPhrase == 3) {
            date += "\n${mmDate.moonPhase}"
        }
        return date
    }

    private fun showLog(message: String) {
        Log.d("MyanmarDateConverter", message)
    }
}
/**
 * Format using a default format
 */

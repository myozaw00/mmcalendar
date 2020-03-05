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
    override fun format(day: CalendarDay): DayInfo {
        /**
         * 0 - La San
         * 1 - La Pyae
         * 2 - La Sote
         * 3 - La Kwel
         */
        val mmDate = MyanmarDateConverter.convert(day.getYear(), day.getMonth(), day.getDay())

        val moonPhase = if (mmDate.fortnightDay == "၁" || mmDate.fortnightDay == "" ) {
            mmDate.moonPhraseInt
        }else -1
//        val moonPhase = if (mmDate.fortnightDay == "၁" || mmDate.fortnightDay == "") {
//            "${mmDate.monthName}\n${mmDate.moonPhase}"
//        }else ""

        return DayInfo(moonPhase = moonPhase,
            burmeseDay = mmDate.fortnightDay,
            westernDay = dateFormat.format(day.getDate()))
    }

    private fun showLog(message: String) {
        Log.d("MyanmarDateConverter", message)
    }
}
/**
 * Format using a default format
 */

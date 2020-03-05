package com.myozawoo.mmcalendar.view

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.myozawoo.mmcalendar.format.WeekDayFormatter
import org.threeten.bp.DayOfWeek

class WeekDayView (context: Context,
                   private var dayOfWeek: DayOfWeek) : AppCompatTextView(context) {
    private var formatter = WeekDayFormatter.DEFAULT
    init {
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        gravity = Gravity.END
//        setPadding(0,0,50,0)
        setDayOfWeek(dayOfWeek)
        setTextColor(Color.WHITE)
    }

    fun setWeekDayFormatter(formatter: WeekDayFormatter) {
        this.formatter = formatter
    }

    fun setDayOfWeek(dayOfWeek: DayOfWeek) {
        this.dayOfWeek = dayOfWeek
        text = formatter.format(dayOfWeek)
    }

}
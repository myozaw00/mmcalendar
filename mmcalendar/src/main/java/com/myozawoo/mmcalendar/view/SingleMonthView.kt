package com.myozawoo.mmcalendar.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.util.rangeTo
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.DateListener
import com.myozawoo.mmcalendar.R
import kotlinx.android.synthetic.main.item_month_header.view.*
import mmcalendar.Language
import mmcalendar.LanguageCatalog
import mmcalendar.MyanmarDateConverter
import java.lang.StringBuilder
import java.util.*

class SingleMonthView: LinearLayout {

    private lateinit var listener: DateListener

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {

        rootView.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = LinearLayout.VERTICAL

    }

    fun setListener(listener: DateListener) {
        this.listener = listener
    }

    fun setDate(calendarDay: CalendarDay) {

//        val tmpMmDate = MyanmarDateConverter.convert(2020, 10, 31)
//        showLog(tmpMmDate.toString())

        removeAllViews()
//
        val month = calendarDay
        val startDate = CalendarDay.from(month.getYear(),month.getDate().monthValue,1)
        val maxDate = if (calendarDay.getYear()%4 != 0 && month.getMonth() == 2) {
            28
        }else {
            month.getDate().month.maxLength()
        }
        val endDate = CalendarDay.from(month.getYear(), month.getDate().monthValue, maxDate)
        val tmpBurmeseMonth = arrayListOf<String>()
        val tmpBurmeseYear = arrayListOf<String>()
        for (i in 1 until endDate.getDay()) {
            val mmDate = MyanmarDateConverter.convert(month.getYear(), month.getMonth(), i)
            if (mmDate.monthName.isNotEmpty()) {
                tmpBurmeseMonth.add(mmDate.monthName)
                tmpBurmeseYear.add(mmDate.year)
            }
        }
        val currentMonths = tmpBurmeseMonth.distinct().joinToString(separator = " - ")
//        val headerView = View.inflate(context, R.layout.item_month_header, null)
//        val myanmarDate = MyanmarDateConverter.convert(month.getYear(), month.getMonth(), 1)
//        headerView.tvTitleTwo.text = "မြန်မာနှစ် ${tmpBurmeseYear.distinct().joinToString(separator = "-")}"
//        headerView.tvTitleOne.text = currentMonths

        //addView(headerView)
        showLog(endDate.toString())
        val monthView = MonthView(context, month, listener)
        monthView.setMinimumDate(startDate)
        monthView.setMaximumDate(endDate)
        addView(monthView)
    }


    private fun showLog(message: String) {
        Log.d("SingleMonthView", message)
    }


}
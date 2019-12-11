package com.myozawoo.mmcalendar.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.util.rangeTo
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.R
import kotlinx.android.synthetic.main.item_month_header.view.*
import mmcalendar.MyanmarDateConverter

class SingleMonthView: LinearLayout {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {

        rootView.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = LinearLayout.VERTICAL

    }

    fun setDate(year: Int, month: Int, day: Int) {
        val month = CalendarDay(year,month,day)
        val startDate = CalendarDay.from(month.getYear(),month.getDate().monthValue,1)
        val endDate = CalendarDay.from(month.getYear(), month.getDate().monthValue, month.getDate().month.maxLength())
        val tmpBurmeseMonth = arrayListOf<String>()
        for (i in 1 until endDate.getDay()) {
            val mmDate = MyanmarDateConverter.convert(month.getYear(), month.getMonth(), i)
            if (mmDate.monthName.isNotEmpty()) {
                tmpBurmeseMonth.add(mmDate.monthName)
            }
        }
        val currentMonths = tmpBurmeseMonth.distinct()
        val headerView = View.inflate(context, R.layout.item_month_header, null)
        val myanmarDate = MyanmarDateConverter.convert(month.getYear(), month.getMonth(), 1)
        headerView.tvTitleTwo.text = "သာသနာနှစ် ${myanmarDate.buddhistEra} မြန်မာနှစ် ${myanmarDate.year}"
//        headerView.tvTitleTwo.text = "${myanmarDate.monthName} ${month.getDate().month.name} ${month.getYear()}"
        headerView.tvTitleOne.text = "${currentMonths[0]} - ${currentMonths[1]}"
        headerView.tvTitleThree.text = "${month.getDate().month.name}\n${month.getYear()}"
        addView(headerView)
        val monthView = MonthView(context, month)
        monthView.setMinimumDate(startDate)
        monthView.setMaximumDate(endDate)
        addView(monthView)
    }
}
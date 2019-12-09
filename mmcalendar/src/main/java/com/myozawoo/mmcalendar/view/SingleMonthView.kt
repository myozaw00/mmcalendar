package com.myozawoo.mmcalendar.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.R
import kotlinx.android.synthetic.main.item_month_header.view.*
import mmcalendar.MyanmarDateConverter

class SingleMonthView (context: Context,
                       private var month: CalendarDay): LinearLayout(context) {

    init {
        rootView.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = LinearLayout.VERTICAL
        val headerView = View.inflate(context, R.layout.item_month_header, null)
        val myanmarDate = MyanmarDateConverter.convert(month.getYear(), month.getMonth(), 1)
        headerView.tvTitleOne.text = "သာသနာနှစ် ${myanmarDate.buddhistEra} မြန်မာနှစ် ${myanmarDate.year}"
        headerView.tvTitleTwo.text = "${myanmarDate.monthName} ${month.getDate().month.name} ${month.getYear()}"
        addView(headerView)
        val monthView = MonthView(context, month)
        monthView.setMinimumDate(CalendarDay.from(month.getYear(),month.getDate().monthValue,1))
        monthView.setMaximumDate(CalendarDay.from(month.getYear(), month.getDate().monthValue, month.getDate().month.maxLength()))
        addView(monthView)
    }
}
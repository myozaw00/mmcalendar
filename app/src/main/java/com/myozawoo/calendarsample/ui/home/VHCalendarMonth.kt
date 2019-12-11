package com.myozawoo.calendarsample.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_month_view.view.*

class VHCalendarMonth (view: View): RecyclerView.ViewHolder(view) {

    fun bind(date: CalendarRecyclerAdapter.Date) {
        itemView.calendarView.setDate(date.year, date.month, date.day)
    }
}
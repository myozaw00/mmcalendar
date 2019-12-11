package com.myozawoo.calendarsample.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myozawoo.calendarsample.R

class CalendarRecyclerAdapter : RecyclerView.Adapter<VHCalendarMonth>() {

    private val list = arrayListOf<Date>()

    init {
        list.clear()
        list.add(Date(2020,1,1))
//        list.add(Date(2020,2,1))
//        list.add(Date(2020,3,1))
//        list.add(Date(2020,4,1))
//        list.add(Date(2020,5,1))
//        list.add(Date(2020,6,1))
//        list.add(Date(2020,7,1))
//        list.add(Date(2020,8,1))
//        list.add(Date(2020,9,1))
//        list.add(Date(2020,10,1))
//        list.add(Date(2020,11,1))
//        list.add(Date(2020,12,1))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHCalendarMonth {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month_view, parent, false)
        return VHCalendarMonth(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VHCalendarMonth, position: Int) {
        holder.bind(list[position])
    }

    data class Date(val year: Int,
                    val month: Int,
                    val day: Int)
}
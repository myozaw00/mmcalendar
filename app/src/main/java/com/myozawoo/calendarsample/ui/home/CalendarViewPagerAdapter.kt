package com.myozawoo.calendarsample.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.myozawoo.calendarsample.R
import kotlinx.android.synthetic.main.item_month_view.view.*

class CalendarViewPagerAdapter (private val context: Context,
                                private val list: List<Date>): PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_month_view, null)
        val date = list[position]
        view.calendarView.setDate(date.year, date.month, date.day)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }


    override fun isViewFromObject(view: View, obj : Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return list.size
    }
}
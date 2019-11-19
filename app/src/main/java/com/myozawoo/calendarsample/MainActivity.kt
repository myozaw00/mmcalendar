package com.myozawoo.calendarsample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.view.MonthView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val monthView = MonthView(this, CalendarDay(2019,11,1))
        monthView.setMinimumDate(CalendarDay.from(2019,11,1))
        monthView.setSelectionEnable(true)
        parentLayout.addView(monthView)
    }
}

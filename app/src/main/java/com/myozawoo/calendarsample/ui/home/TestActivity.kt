package com.myozawoo.calendarsample.ui.home

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myozawoo.calendarsample.R
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.DateListener
import com.myozawoo.mmcalendar.picker.DatePickerDialogFragment
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.item_month_view.*

class TestActivity : AppCompatActivity(), DateListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        btnTest.setOnClickListener {
            val datePicker = DatePickerDialogFragment(this)
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null)
            {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            datePicker.show(supportFragmentManager, "dialog")

        }


    }

    override fun onDateClick(calendarDay: CalendarDay) {
        Toast.makeText(this, calendarDay.toString(), Toast.LENGTH_SHORT).show()
    }
}
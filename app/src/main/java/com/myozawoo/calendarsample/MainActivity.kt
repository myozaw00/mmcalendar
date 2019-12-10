package com.myozawoo.calendarsample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.view.MonthView
import kotlinx.android.synthetic.main.activity_main.*
import android.util.TypedValue
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.myozawoo.mmcalendar.view.SingleMonthView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parentLayout.addView(SingleMonthView(this, CalendarDay.from(2019,11,1)))
    }
}

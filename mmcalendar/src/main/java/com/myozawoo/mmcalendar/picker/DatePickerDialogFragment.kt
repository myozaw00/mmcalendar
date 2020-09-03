package com.myozawoo.mmcalendar.picker

import android.app.Dialog
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView

import androidx.coordinatorlayout.widget.CoordinatorLayout

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.DateListener
import com.myozawoo.mmcalendar.R
import kotlinx.android.synthetic.main.dialog_fragment_date_picker.*
import mmcalendar.MyanmarDateConverter
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialogFragment(private val listener: DateListener,
                               private val fromDate: Calendar,
                               private val toDate: Calendar) : BottomSheetDialogFragment(),
    DateListener {

    private val DEFAULT_MONTHS = listOf("JANUARY ပြာသို-တပို့တွဲ", "FEBRUARY တပို့တွဲ-တပေါင်း", "MARCH တပေါင်း-နှောင်းတန်ခူး", "APRIL နှောင်းတန်ခူး-တန်ခူး-ကဆုန်","MAY ကဆုန်-နယုန်",
        "JUNE နယုန်-ပဝါဆို", "JULY ပဝါဆို-ဒုဝါဆို", "AUGUST ဒုဝါဆို-ဝါခေါင်", "SEPTEMBER ဝါခေါင်-တော်သလင်း", "OCTOBER တော်သလင်း-သီတင်းကျွတ်","NOVEMBER သီတင်းကျွတ်-တန်ဆောင်မုန်း",
        "DECEMBER တန်ဆောင်မုန်း-နတ်တော်")


    private var mSelectedDate: CalendarDay? = null
    private val mYearList: List<String> by lazy {
        getYearList(fromDate, toDate)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_date_picker, container, false)
        return view
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = View.inflate(context, R.layout.dialog_fragment_date_picker, null)
        dialog.setContentView(view)
        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        if (behavior != null && behavior is BottomSheetBehavior) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
            behavior.peekHeight = 2000
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSelectedDate = CalendarDay(2020, 1, 1)
        calendarView.setListener(this)
        spYear.adapter = YearAdapter(context!!, mYearList)
        spMonth.adapter = SpinnerAdapter(context!!, DEFAULT_MONTHS)
        spYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (mYearList[position].substring(0, 4).toInt() == 2020) {
                    spMonth.adapter = SpinnerAdapter(context!!, DEFAULT_MONTHS)
                }else {
                    spMonth.adapter = SpinnerAdapter(context!!, getMonthListYear(
                        mYearList[position].substring(0, 4).toInt()
                    ))
                }
            }
        }

        spMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val year = mYearList[spYear.selectedItemPosition].substring(0, 4).toInt()
                val month = position + 1
                val calendarDay = CalendarDay(
                    year,
                    month,
                    1
                )
                mSelectedDate = calendarDay
                calendarView.setDate(
                    calendarDay
                )
                val mmDate = MyanmarDateConverter.convert(year, month, 1)
                val fortNightDay =
                    if (mmDate.fortnightDayInt == 15) "" else "${mmDate.fortnightDay} ရက်"
                tvBurmeseDate.text =
                    "${mmDate.year} ခုနှစ်၊ ${mmDate.monthName} ${mmDate.moonPhase} $fortNightDay၊ ${mmDate.weekDay}"
                tvEngDate.text = convertDateFormat(calendarDay.getDate().toString())


            }
        }

        tvCancel.setOnClickListener { dismiss() }

        tvOk.setOnClickListener {
            listener.onDateClick(mSelectedDate!!)
            dismiss()
        }
    }

    override fun onDateClick(calendarDay: CalendarDay) {
        mSelectedDate = calendarDay
        val mmDate = MyanmarDateConverter.convert(
            calendarDay.getYear(),
            calendarDay.getMonth(),
            calendarDay.getDay()
        )
        val fortNightDay = if (mmDate.fortnightDayInt == 15) "" else "${mmDate.fortnightDay} ရက်"
        tvBurmeseDate.text =
            "${mmDate.year} ခုနှစ်၊ ${mmDate.monthName} ${mmDate.moonPhase} $fortNightDay၊ ${mmDate.weekDay}"
        tvEngDate.text = convertDateFormat(calendarDay.getDate().toString())

    }

    private fun getYearList(fromDate: Calendar, toDate: Calendar): List<String> {
        var counter = fromDate.get(Calendar.YEAR)
        val tmpEngYearList = arrayListOf<Int>()
        for (i in fromDate.get(Calendar.YEAR)..toDate.get(Calendar.YEAR)) {
            tmpEngYearList.add(counter)
            counter++
        }
//        for (i in 0..100) {
//            tmpEngYearList.add(currentYear - i)
//        }
        val tmpMixYearList = arrayListOf<String>()
        tmpEngYearList.forEach {
            val tmpMyanmarYearList = arrayListOf<String>()
            for (i in 1..6) {
                val mmDate = MyanmarDateConverter.convert(it, i, 1)
                tmpMyanmarYearList.add(mmDate.year)
            }
            tmpMixYearList.add(
                "${it} (${tmpMyanmarYearList.distinct().joinToString(separator = "-")})"
            )

        }
        return tmpMixYearList
    }

    private fun getMonthListYear(year: Int): List<String> {
        val months = arrayListOf<String>()

        val monthList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        monthList.forEach { month ->

            val engDate = CalendarDay(year, month, 1)
            val maxDate = if (year % 400 == 0 && engDate.getMonth() == 2) {
                29
            }else if (year % 100 == 0 && engDate.getMonth() == 2){
                28
            }else if (year % 4 == 0 && engDate.getMonth() == 2) {
                29
            }else{
                if (engDate.getMonth() == 2) {
                    28
                }else {
                    engDate.getDate().month.maxLength()
                }

            }

            val endDate = CalendarDay.from(engDate.getYear(), engDate.getDate().monthValue, maxDate)
            val tmpBurmeseMonth = arrayListOf<String>()
            for (i in 1 until endDate.getDay()) {
                val mmDate = MyanmarDateConverter.convert(year, month, i)
                if (mmDate.monthName.isNotEmpty()) {
                    tmpBurmeseMonth.add(mmDate.monthName)
                }
            }

            months.add(
                "${engDate.getDate().month.name} ${tmpBurmeseMonth.distinct()
                    .joinToString(separator = "-")}"
            )


        }

        return months
    }


    fun convertDateFormat(date: String): String {
        if (date == "") return ""
        val dateFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        val date = sdf.parse(date)
        return SimpleDateFormat("yyyy, MMMM dd EEEE", Locale.getDefault()).format(date)
    }

    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                dismiss()
            }
        }
    }
}
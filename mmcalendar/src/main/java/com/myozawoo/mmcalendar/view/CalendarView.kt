package com.myozawoo.mmcalendar.view

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.myozawoo.mmcalendar.CalendarDay
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.WeekFields
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.myozawoo.mmcalendar.R
import com.myozawoo.mmcalendar.format.WeekDayFormatter
import com.myozawoo.mmcalendar.format.DateFormatDayFormatter


abstract class CalendarView(context: Context,
                            private val firstViewDay: CalendarDay,
                            private val firstDayOfWeek: DayOfWeek) : ViewGroup(context),
    View.OnClickListener, View.OnLongClickListener{

    companion object {
        val DEFAULT_DAYS_IN_WEEK = 7
        val DEFAULT_MAX_WEEK = 6
        val DAY_NAMES_ROW = 1
    }

    private val weekDayViews = arrayListOf<WeekDayView>()
    private val decoratorResults = arrayListOf<DecoratorResult>()
    private var minDate: CalendarDay? = null
    private var maxDate: CalendarDay? = null
    private val dayViews = arrayListOf<DayView>()

    init {
        buildWeekDays(resetAndGetWorkingCalendar())
        buildDayViews(dayViews, resetAndGetWorkingCalendar())
    }

    private fun buildWeekDays(calendar: LocalDate) {
        var local = calendar
        for (i in 0 until DEFAULT_DAYS_IN_WEEK) {
            val weekDayView = WeekDayView(context, local.dayOfWeek)
            weekDayView.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            weekDayView.setBackgroundColor(Color.parseColor("#154FCD"))
            weekDayViews.add(weekDayView)
            addView(weekDayView)
            local = local.plusDays(1)
        }
        //setMinimumDate(firstViewDay)
    }

    fun setDayViewDecorators(result: List<DecoratorResult>) {
        this.decoratorResults.clear()
        this.decoratorResults.addAll(result)
        invalidateDecorators()
    }

    fun addDayView(views: Collection<DayView>, temp: LocalDate) {
        val day = CalendarDay.from(temp)
        val dayView = DayView(context, day!!)
        dayView.setOnClickListener(this)
        dayView.setOnLongClickListener(this)
        dayViews.add(dayView)
        addView(dayView, LayoutParams())
    }

    fun setWeekDayTextAppearance(taId: Int) {
        weekDayViews.forEach {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.setTextAppearance(taId)
            }else {
                it.setTextAppearance(context, taId)
            }
        }
    }

    fun setDateTextAppearance(taId: Int) {
        dayViews.forEach {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //it.setTextAppearance(taId)
            }else {
                //it.setTextAppearance(context, taId)
            }
        }
    }

    fun setSelectionColor(color: Int) {
        dayViews.forEach {
            it.setSelectionColor(color)
        }
    }

    fun setSelectionEnable(selectionEnabled: Boolean) {
        dayViews.forEach {
            it.setOnClickListener(if (selectionEnabled) this else null)
            it.isClickable = selectionEnabled
        }
    }

    fun setWeekDayFormatter(formatter: WeekDayFormatter) {
        weekDayViews.forEach { it.setWeekDayFormatter(formatter) }
    }

    fun setDayFormatter(formatter: DateFormatDayFormatter) {
        dayViews.forEach { it.setDayFormatter(formatter) }
    }

    fun setMinimumDate(minDate: CalendarDay) {
        this.minDate = minDate
        updateUi()
    }

    fun setMaximumDate(maxDate: CalendarDay) {
        this.maxDate = maxDate
        updateUi()
    }

    private fun setSelectedDates(dates: List<CalendarDay>) {
        dayViews.forEach {
            //it.isChecked = dates.contains(it.getDate())
        }
        postInvalidate()
    }

    private fun updateUi() {
        dayViews.forEach {
            val day = it.getDate()
            showLog("isInRange: ${day.isInRange(minDate, maxDate)}")
            it.setupSelection(day.isInRange(minDate, maxDate), isDayEnabled(day))
        }
        postInvalidate()
    }

    abstract fun isDayEnabled(day: CalendarDay): Boolean

    private fun invalidateDecorators() {
        val facadeAccumulator = DayViewFacade()
        dayViews.forEach {
            facadeAccumulator.reset()
            decoratorResults.forEach { r ->
                if (r.decorator.shouldDecorate(it.getDate())) {
                    r.result.applyTo(facadeAccumulator)
                }
            }
            it.applyFacade(facadeAccumulator)
        }
    }

    private fun resetAndGetWorkingCalendar(): LocalDate {
        val firstDayOfWeek = WeekFields.of(firstDayOfWeek, 1).dayOfWeek()
        val temp = getFirstViewDay().getDate().with(firstDayOfWeek, 1)
        val dow = temp.dayOfWeek.value
        var delta = getFirstDayOfWeek().value - dow
        val removeRow = delta > 0
        if (removeRow) delta -= DEFAULT_DAYS_IN_WEEK
        return temp.plusDays(delta.toLong())
    }

    private fun getFirstDayOfWeek(): DayOfWeek = firstDayOfWeek

    fun getFirstViewDay(): CalendarDay = firstViewDay

    internal abstract fun buildDayViews(dayViews: Collection<DayView>, calendar: LocalDate)

    internal abstract fun getRows(): Int

    override fun onClick(v: View?) {
        if (v is DayView) {
            setSelectedDates(emptyList())
            setSelectedDates(listOf(v.getDate()))
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (v is DayView) {

            return true
        }
        return false
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidthSize  = MeasureSpec.getSize(widthMeasureSpec)
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val specHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (specHeightMode == MeasureSpec.UNSPECIFIED || specWidthMode == MeasureSpec.UNSPECIFIED) {
            throw IllegalStateException("Calendar pager view should never be left to decide it's size")
        }

        val measureTileWidth = specWidthSize / DEFAULT_DAYS_IN_WEEK
        val measureTileHeight = (specHeightSize/2) /getRows()
        setMeasuredDimension(specWidthSize, specHeightSize)
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measureTileWidth, MeasureSpec.EXACTLY)
            val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measureTileHeight, MeasureSpec.EXACTLY)
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val parentWidth = width
        val count = childCount
        val parentLeft = 0
        val parentRight = parentWidth

        var childTop = 0
        var childLeft = parentLeft
        var childRight = parentRight

        for (i in 0 until count) {
            val child = getChildAt(i)
            val width = child.measuredWidth
            val height = child.measuredHeight
            child.layout(childLeft, childTop, childLeft+width, childTop+height)
            childLeft += width
            if (i % DEFAULT_DAYS_IN_WEEK == (DEFAULT_DAYS_IN_WEEK - 1)) {
                childLeft = parentLeft;
                childRight = parentRight;
                childTop += height;
            }
        }

    }

    /**
     * Simple layout params class for MonthView, since every child is the same size
     */
    /**
     * {@inheritDoc}
     */
    protected class LayoutParams : ViewGroup.MarginLayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
        return LayoutParams()
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent?) {
        super.onInitializeAccessibilityEvent(event)
        event?.className == CalendarView::class.java.name

    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        super.onInitializeAccessibilityNodeInfo(info)
        info?.className = CalendarView::class.java.name
    }

    private fun showLog(message: String) {
        Log.d(javaClass.simpleName, message)
    }


}
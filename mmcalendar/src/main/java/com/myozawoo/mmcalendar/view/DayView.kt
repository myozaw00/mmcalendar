package com.myozawoo.mmcalendar.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.OvalShape
import androidx.appcompat.widget.AppCompatCheckedTextView
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.format.DayFormatter
import android.text.Spanned
import android.text.SpannableString
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.myozawoo.mmcalendar.R
import com.myozawoo.mmcalendar.format.DateFormatDayFormatter
import com.myozawoo.mmcalendar.format.DayInfo
import com.myozawoo.mmcalendar.utils.DayUtils
import kotlinx.android.synthetic.main.item_day_view.view.*
import org.threeten.bp.LocalDate


class DayView(context: Context,
              calendarDay: CalendarDay,
              isHoliday: Boolean) : RelativeLayout(context) {

    private var date: CalendarDay = calendarDay
    private var selectionColor = Color.GRAY

    private val fadeTime: Int
    private var customBackground: Drawable? = null
    private var selectionDrawable: Drawable? = null
    private var mCircleDrawable: Drawable? = null
    private lateinit var formatter: DateFormatDayFormatter
    private lateinit var contentDescriptionFormatter: DayFormatter
    private var isInRange = true
    private var isInMoth = true
    private var isDecoratedDisabled = false
    private val tempRect = Rect()
    private val circleDrawableRect = Rect()

    companion object {

        private fun generateCircleDrawable(color: Int): Drawable {
            val drawable = ShapeDrawable(OvalShape())
            drawable.paint.color = color
            return drawable
        }

        private fun generateBackground(color: Int, fadeTime: Int, bounds: Rect): Drawable {
            val drawable = StateListDrawable()
            drawable.setExitFadeDuration(fadeTime)
            drawable.addState(intArrayOf(android.R.attr.state_checked), generateCircleDrawable(color))
            drawable.addState(intArrayOf(android.R.attr.state_pressed), generateCircleDrawable(color))
            drawable.addState(intArrayOf(), generateCircleDrawable(Color.TRANSPARENT))
            return drawable
        }
    }

    init {
        inflate(context, R.layout.item_day_view, this)
        formatter =  DateFormatDayFormatter()
        contentDescriptionFormatter = formatter
        fadeTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        setSelectionColor(this.selectionColor)
        setCustomBackground(ContextCompat.getDrawable(context, R.drawable.rounded_grey_border))
        gravity = Gravity.CENTER
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setDay(date)
//        setText(getLabel())
        if (isHoliday) {
            tvMoonphase.setTextColor(Color.RED)
            tvBurmeseDay.setTextColor(Color.RED)
            tvWesternDay.setTextColor(Color.RED)
        }

        if (calendarDay.getDate() == LocalDate.now()) {
            tvWesternDay.setTextColor(Color.WHITE)
            tvWesternDay.background = generateCircleDrawable(Color.parseColor("#154FCD"))
        }



    }

    private fun setText(dayInfo: DayInfo) {

    }


    fun setDay(date: CalendarDay) {
        this.date = date
        val dayInfo = formatter.format(this.date)
        tvBurmeseDay.text = dayInfo.burmeseDay
        tvMoonphase.text = dayInfo.moonPhase
        tvWesternDay.text = dayInfo.westernDay

        if (DayUtils.getPublicHoliday(date.getYear(), date.getMonth(), date.getDay()) != "") {
            tvMoonphase.setTextColor(Color.RED)
            tvBurmeseDay.setTextColor(Color.RED)
            tvWesternDay.setTextColor(Color.RED)
            tvPublicHoliday.setTextColor(Color.RED)
            if (dayInfo.moonPhase == "") {
                tvMoonphase.text = DayUtils.getPublicHoliday(date.getYear(), date.getMonth(), date.getDay())
            }else {
                tvPublicHoliday.text = DayUtils.getPublicHoliday(date.getYear(), date.getMonth(), date.getDay())
            }

        }
    }

    fun setDayFormatter(formatter: DateFormatDayFormatter) {
        this.contentDescriptionFormatter = if (contentDescriptionFormatter === this.formatter)
            formatter
        else contentDescriptionFormatter
        this.formatter = formatter
//        val currentLabel = getLabel()
//        var spans: Array<Any>? = null
//        if (currentLabel is Spanned) {
//            spans = currentLabel.getSpans(0, currentLabel.length, Any::class.java)
//        }
//        val newLabel = SpannableString(getLabel())
//        spans?.forEach {
//            newLabel.setSpan(it, 0, newLabel.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        }
        //text = newLabel
    }

    fun getLabel(): DayInfo {
        return formatter.format(date)
    }

    fun getContentDescriptionLabel(): DayInfo {
        return contentDescriptionFormatter.format(date)
    }

    private fun regenerateBackground() {
        if (selectionDrawable != null) {
            background = selectionDrawable
        }else {
            mCircleDrawable = generateBackground(selectionColor, fadeTime, circleDrawableRect)
            background = mCircleDrawable

        }
    }


    fun setSelectionColor(color: Int) {
        this.selectionColor = color
        regenerateBackground()
    }

    fun setSelectionDrawable(drawable: Drawable?) {
        drawable?.let {
            this.selectionDrawable = drawable.constantState?.newDrawable(resources)
        }
        regenerateBackground()
    }

    fun setCustomBackground(drawable: Drawable?) {
        drawable?.let {
            this.customBackground = drawable.constantState?.newDrawable(resources)
        }
        invalidate()
    }

    fun getDate() = date

    private fun setEnabled() {
        val enabled = isInMoth && isInRange && !isDecoratedDisabled
        super.setEnabled(isInRange && !isDecoratedDisabled)
        var shouldBeVisible = enabled
        val showOutOfRange = true

        if (!isInRange && showOutOfRange) {
            shouldBeVisible = shouldBeVisible or isInMoth
        }
        if (!isInMoth) {
            tvMoonphase.setTextColor(Color.GRAY)
            tvBurmeseDay.setTextColor(Color.GRAY)
            tvWesternDay.setTextColor(Color.GRAY)
            tvPublicHoliday.setTextColor(Color.GRAY)
//            tvMoonphase.setTextColor(tvMoonphase.textColors.getColorForState(
//                intArrayOf(android.R.attr.state_enabled), Color.GRAY
//            ))
        }
        //visibility = if (shouldBeVisible) View.VISIBLE else View.INVISIBLE
    }

    internal fun setupSelection(inRange: Boolean,
                                 inMonth: Boolean) {

        this.isInRange = inRange
        this.isInMoth = inMonth
        setEnabled()
    }

    fun applyFacade(facade: DayViewFacade) {
        this.isDecoratedDisabled = facade.areDaysDisabled()
        setEnabled()
        setCustomBackground(facade.getBackgroundDrawable())
        setSelectionDrawable(facade.getSelectionDrawable())
        val spans = facade.getSpans()
        if (spans.isNotEmpty()) {
            val label = getLabel()
           // val formattedLabel = SpannableString(getLabel())
            spans.forEach {
         //       formattedLabel.setSpan(it, 0, label.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
           // text = formattedLabel
        }else {
            //text = getLabel()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        customBackground?.let {
            it.bounds = tempRect
            it.state = drawableState
            canvas?.let { c -> it.draw(c) }
        }
        mCircleDrawable?.bounds = circleDrawableRect
        super.onDraw(canvas)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
//        tempRect.set(left, top, right, bottom)
        calculateBounds(right-left, bottom-top)
        regenerateBackground()
    }

    private fun calculateBounds(width: Int, height: Int) {
        val radius = Math.min(height, width)
        val offset = Math.abs(height - width)/2
        val circleOffset = offset
        showLog("Width: $width Height: $height")
        if (width >= height) {
            tempRect.set(offset, 0, radius+offset, height)
            circleDrawableRect.set(circleOffset, 0, radius+circleOffset, height)
        }else {
            tempRect.set(0, 0, width, height)
            circleDrawableRect.set(0, circleOffset, width, radius + circleOffset)
        }
    }

    private fun showLog(message: String) {
        Log.d(javaClass.simpleName, message)
    }

}
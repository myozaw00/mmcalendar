package com.myozawoo.mmcalendar.view

import android.content.Context
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
import android.view.View


class DayView(context: Context,
              calendarDay: CalendarDay) : AppCompatCheckedTextView(context) {

    private lateinit var date: CalendarDay
    private var selectionColor = Color.GRAY

    private val fadeTime: Int
    private var customBackground: Drawable? = null
    private var selectionDrawable: Drawable? = null
    private var mCircleDrawable: Drawable? = null
    private var formatter = DayFormatter.DEFAULT
    private var contentDescriptionFormatter = formatter
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
        fadeTime = resources.getInteger(android.R.integer.config_shortAnimTime)

    }


    fun setDay(date: CalendarDay) {
        this.date = date
        text = getLabel()
    }

    fun setDayFormatter(formatter: DayFormatter) {
        this.contentDescriptionFormatter = if (contentDescriptionFormatter === this.formatter)
            formatter
        else contentDescriptionFormatter
        this.formatter = formatter
        val currentLabel = text
        var spans: Array<Any>? = null
        if (currentLabel is Spanned) {
            spans = currentLabel.getSpans(0, currentLabel.length, Any::class.java)
        }
        val newLabel = SpannableString(getLabel())
        spans?.forEach {
            newLabel.setSpan(it, 0, newLabel.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        text = newLabel
    }

    fun getLabel(): String {
        return formatter.format(date)
    }

    fun getContentDescriptionLabel(): String {
        return contentDescriptionFormatter.format(date)
    }

    private fun regenerateBackground() {
        if (selectionDrawable != null) {
            background = selectionDrawable
        }else {
            mCircleDrawable = generateBackground(selectionColor, fadeTime, circleDrawableRect)
        }
    }

    fun setSelectionColor(color: Int) {
        this.selectionColor = color

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
        val shouldBeVisible = enabled
        if (!isInMoth && shouldBeVisible) {
            setTextColor(textColors.getColorForState(
                intArrayOf(android.R.attr.state_enabled), Color.GRAY
            ))
        }
        visibility = if (shouldBeVisible) View.VISIBLE else View.INVISIBLE
    }

    internal fun setupSelection(inRange: Boolean,
                                 inMonth: Boolean) {

        this.isInRange = inRange
        this.isInMoth = inMonth
        setEnabled()
    }


}
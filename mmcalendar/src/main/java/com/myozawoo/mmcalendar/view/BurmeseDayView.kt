package com.myozawoo.mmcalendar.view

import android.R
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatCheckedTextView
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.format.DayFormatter
import mmcalendar.LanguageCatalog
import mmcalendar.MyanmarDateConverter
import java.util.*


/**
 * Display one day of a [MaterialCalendarView]
 */
@SuppressLint("ViewConstructor")
class BurmeseDayView : AppCompatCheckedTextView {
    var date: CalendarDay? = null
        private set
    private var selectionColor = Color.GRAY
    private var fadeTime = 0
    private var customBackground: Drawable? = null
    private var selectionDrawable: Drawable? = null
    private var mCircleDrawable: Drawable? = null
    private var formatter = DayFormatter.DEFAULT
    private var contentDescriptionFormatter: DayFormatter? = formatter
    private var isInRange = true
    private var isInMonth = true
    private var isDecoratedDisabled = false

    constructor(context: Context?, day: CalendarDay): super(context) {
        val c = Calendar.getInstance()
        c[day.getYear(), day.getMonth()] = day.getDay()
        val myanmarDate = MyanmarDateConverter.convert(c)
        myanmarDate.fortnightDay
        fadeTime = resources.getInteger(R.integer.config_shortAnimTime)
        setSelectionColor(selectionColor)
        gravity = Gravity.CENTER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        setDay(day)

        val date =
            getLabel() + "\n" + myanmarDate.getFortnightDay(LanguageCatalog.getInstance()) +
                    "\n"
        text = date
        setTextColor(Color.RED)
    }

    fun setDay(date: CalendarDay?) {
        this.date = date
        //setText(getLabel());
    }

    /**
     * Set the new label formatter and reformat the current label. This preserves current spans.
     *
     * @param formatter new label formatter
     */
    fun setDayFormatter(formatter: DayFormatter?) {
        contentDescriptionFormatter =
            if (contentDescriptionFormatter === this.formatter) formatter else contentDescriptionFormatter
        this.formatter = formatter ?: DayFormatter.DEFAULT
        val currentLabel = text
        var spans: Array<Any?>? = null
        if (currentLabel is Spanned) {
            spans = currentLabel.getSpans(
                0,
                currentLabel.length,
                Any::class.java
            )
        }
        val newLabel = SpannableString(getLabel())
        if (spans != null) {
            for (span in spans) {
                newLabel.setSpan(span, 0, newLabel.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        //setText(newLabel);
    }

    /**
     * Set the new content description formatter and reformat the current content description.
     *
     * @param formatter new content description formatter
     */
    fun setDayFormatterContentDescription(formatter: DayFormatter?) {
        contentDescriptionFormatter = formatter ?: this.formatter
        contentDescription = getContentDescriptionLabel()
    }

    fun getLabel(): String = formatter.format(date!!).westernDay

    fun getContentDescriptionLabel(): String {
        return if (contentDescriptionFormatter == null) formatter.format(date!!).toString()
        else contentDescriptionFormatter!!.format(date!!).toString()
    }

    fun setSelectionColor(color: Int) {
        selectionColor = color
        regenerateBackground()
    }

    /**
     * @param drawable custom selection drawable
     */
    fun setSelectionDrawable(drawable: Drawable?) {
        if (drawable == null) {
            selectionDrawable = null
        } else {
            selectionDrawable = drawable.constantState!!.newDrawable(resources)
        }
        regenerateBackground()
    }

    /**
     * @param drawable background to draw behind everything else
     */
    fun setCustomBackground(drawable: Drawable?) {
        if (drawable == null) {
            customBackground = null
        } else {
            customBackground = drawable.constantState!!.newDrawable(resources)
        }
        invalidate()
    }

    private fun setEnabled() {
        val enabled = isInMonth && isInRange && !isDecoratedDisabled
        super.setEnabled(isInRange && !isDecoratedDisabled)
//        val showOtherMonths: Boolean = showOtherMonths(showOtherDates)
        val showOtherMonths: Boolean = false
//        val showOutOfRange = showOutOfRange(showOtherDates) || showOtherMonths
        val showOutOfRange = false
//        val showDecoratedDisabled: Boolean = showDecoratedDisabled(showOtherDates)
        val showDecoratedDisabled: Boolean = true
        var shouldBeVisible = enabled
        if (!isInMonth && showOtherMonths) {
            shouldBeVisible = true
        }
        if (!isInRange && showOutOfRange) {
            shouldBeVisible = shouldBeVisible or isInMonth
        }
        if (isDecoratedDisabled && showDecoratedDisabled) {
            shouldBeVisible = shouldBeVisible or isInMonth && isInRange
        }
        if (!isInMonth && shouldBeVisible) {
            setTextColor(
                textColors.getColorForState(
                    intArrayOf(-R.attr.state_enabled),
                    Color.GRAY
                )
            )
        }
        visibility = if (shouldBeVisible) View.VISIBLE else View.INVISIBLE
    }

    fun setupSelection(showOtherDates: Int, inRange: Boolean, inMonth: Boolean) {
        isInMonth = inMonth
        isInRange = inRange
        setEnabled()
    }

    private val tempRect = Rect()
    private val circleDrawableRect = Rect()
    override fun onDraw(canvas: Canvas) {
        if (customBackground != null) {
            customBackground!!.bounds = tempRect
            customBackground!!.state = drawableState
            customBackground!!.draw(canvas)
        }
        mCircleDrawable!!.bounds = circleDrawableRect
        super.onDraw(canvas)
    }

    private fun regenerateBackground() {
        if (selectionDrawable != null) {
            setBackgroundDrawable(selectionDrawable)
        } else {
            mCircleDrawable = generateBackground(
                selectionColor,
                fadeTime,
                circleDrawableRect
            )
            setBackgroundDrawable(mCircleDrawable)
        }
    }

    /**
     * @param facade apply the facade to us
     */
    fun applyFacade(facade: DayViewFacade) {
        isDecoratedDisabled = facade.areDaysDisabled()
        setEnabled()
        setCustomBackground(facade.getBackgroundDrawable())
        setSelectionDrawable(facade.getSelectionDrawable())
        // Facade has spans
        val spans =
            facade.getSpans()
        if (!spans.isEmpty()) {
            val label = getLabel()
            val formattedLabel = SpannableString(label)
            for (span in spans) {
                formattedLabel.setSpan(span, 0, label.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            //setText(formattedLabel);
        } else { //  setText(getLabel());
        }
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        calculateBounds(right - left, bottom - top)
        regenerateBackground()
    }

    private fun calculateBounds(width: Int, height: Int) {
        val radius = Math.min(height, width)
        val offset = Math.abs(height - width) / 2
        // Lollipop platform bug. Circle drawable offset needs to be half of normal offset
        val circleOffset =
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) offset / 2 else offset
        if (width >= height) {
            tempRect[offset, 0, radius + offset] = height
            circleDrawableRect[circleOffset, 0, radius + circleOffset] = height
        } else {
            tempRect[0, offset, width] = radius + offset
            circleDrawableRect[0, circleOffset, width] = radius + circleOffset
        }
    }

    companion object {
        private fun generateBackground(
            color: Int,
            fadeTime: Int,
            bounds: Rect
        ): Drawable {
            val drawable =
                StateListDrawable()
            drawable.setExitFadeDuration(fadeTime)
            drawable.addState(
                intArrayOf(R.attr.state_checked),
                generateCircleDrawable(color)
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.addState(
                    intArrayOf(R.attr.state_pressed),
                    generateRippleDrawable(color, bounds)
                )
            } else {
                drawable.addState(
                    intArrayOf(R.attr.state_pressed),
                    generateCircleDrawable(color)
                )
            }
            drawable.addState(
                intArrayOf(),
                generateCircleDrawable(Color.TRANSPARENT)
            )
            return drawable
        }

        private fun generateCircleDrawable(color: Int): Drawable {
            val drawable = ShapeDrawable(OvalShape())
            drawable.paint.color = color
            return drawable
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun generateRippleDrawable(color: Int, bounds: Rect): Drawable {
            val list = ColorStateList.valueOf(color)
            val mask =
                generateCircleDrawable(Color.WHITE)
            val rippleDrawable = RippleDrawable(list, null, mask)
            //        API 21
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                rippleDrawable.bounds = bounds
            }
            //        API 22. Technically harmless to leave on for API 21 and 23, but not worth risking for 23+
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
                val center = (bounds.left + bounds.right) / 2
                rippleDrawable.setHotspotBounds(center, bounds.top, center, bounds.bottom)
            }
            return rippleDrawable
        }
    }
}

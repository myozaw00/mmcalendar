package com.myozawoo.mmcalendar.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.content.ContextCompat
import com.myozawoo.mmcalendar.CalendarDay
import com.myozawoo.mmcalendar.R
import com.myozawoo.mmcalendar.format.DayFormatter


class DayView(context: Context,
              calendarDay: CalendarDay) : AppCompatCheckedTextView(context) {

    private lateinit var date: CalendarDay
    private var selectionColor = Color.parseColor("#80ff0000")

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
            val drawable = ShapeDrawable(RectShape())
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
//        setTextColor(Color.DKGRAY)
        setDay(calendarDay)
        setSelectionColor(this.selectionColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textAlignment = View.TEXT_ALIGNMENT_CENTER;
        }
        gravity = Gravity.CENTER
        compoundDrawablePadding = -20
        setPadding(10,10,10,10)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.day_text_size))
    }


    fun setDay(date: CalendarDay) {
        /**
         * 0 - La San
         * 1 - La Pyae
         * 2 - La Sote
         * 3 - La Kwel
         */
        this.date = date

        text = getSpannableLabel()
        val moonPhaseResourceId = when(formatter.format(date).moonPhase) {
            0 -> R.drawable.ic_la_san
            1 -> R.drawable.ic_la_pyae
            2 -> R.drawable.ic_la_sote
            3 -> R.drawable.ic_la_kwel
            else -> 0
        }
        setCompoundDrawablesWithIntrinsicBounds(moonPhaseResourceId,
            0, 0, 0)
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
        return "${formatter.format(date).burmeseDay}\n${formatter.format(date).westernDay}"
    }

    fun getSpannableLabel(): SpannableString {
        val mmDay = formatter.format(date).burmeseDay
        val engDay = formatter.format(date).westernDay
        val spannableString = SpannableString("$mmDay\n$engDay")
        spannableString.setSpan(
            ForegroundColorSpan(Color.LTGRAY),
            0, mmDay.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            ForegroundColorSpan(Color.DKGRAY),
            mmDay.length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun getContentDescriptionLabel(): String {
        return contentDescriptionFormatter.format(date).westernDay
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
            val formattedLabel = SpannableString(getLabel())
            for (span in spans) {
                formattedLabel.setSpan(span, 0, label.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            setText(formattedLabel)
        } else {
            setText(getLabel())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (customBackground != null) {
            customBackground!!.setBounds(tempRect)
            customBackground!!.setState(getDrawableState())
            customBackground!!.draw(canvas!!)
        }

        mCircleDrawable?.setBounds(circleDrawableRect);
        super.onDraw(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
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



}
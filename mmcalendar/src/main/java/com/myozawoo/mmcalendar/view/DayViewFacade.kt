package com.myozawoo.mmcalendar.view

import android.graphics.drawable.Drawable
import java.util.*


class DayViewFacade {

    private var isDecorated = false
    private var backgroundDrawable: Drawable? = null
    private var selectionDrawable: Drawable? = null
    private val spans = LinkedList<Span>()
    private var daysDisabled = false


    fun setBackgroundDrawable(drawable: Drawable?) {
        requireNotNull(drawable) { "Cannot be null" }
        this.backgroundDrawable = drawable
        isDecorated = true
    }

    fun setSelectionDrawable(drawable: Drawable?) {
        requireNotNull(drawable) { "Cannot be null" }
        this.selectionDrawable = drawable
        isDecorated = true
    }

    /**
     * Add a span to the entire text of a day
     *
     * @param span text span instance
     */
    fun addSpan(span: Span?) {
        if(span != null) {
            this.spans.add(span)
            isDecorated = true
        }
    }

    /**
     * <p>Set days to be in a disabled state, or re-enabled.</p>
     * <p>Note, passing true here will <b>not</b> override minimum and maximum dates, if set.
     * This will only re-enable disabled dates.</p>
     *
     * @param daysDisabled true to disable days, false to re-enable days
     */
    fun setDaysDisabled(daysDisabled: Boolean) {
        this.daysDisabled = daysDisabled
        isDecorated = true
    }

    fun reset() {
        backgroundDrawable = null
        selectionDrawable = null
        spans.clear()
        isDecorated = false
        daysDisabled = false
    }

    /**
     * Apply things set this to other
     *
     * @param other facade to apply our data to
     */
    fun applyTo(other: DayViewFacade) {
        if (selectionDrawable != null) {
            other.setSelectionDrawable(selectionDrawable)
        }
        if (backgroundDrawable != null) {
            other.setBackgroundDrawable(backgroundDrawable)
        }
        other.spans.addAll(spans)
        other.isDecorated = isDecorated
        other.daysDisabled = daysDisabled
    }

    fun isDecorated(): Boolean = isDecorated

    fun getSelectionDrawable(): Drawable? = selectionDrawable

    fun getBackgroundDrawable(): Drawable? = backgroundDrawable

    fun getSpans(): List<Span> = Collections.unmodifiableList(spans)

    fun areDaysDisabled(): Boolean = daysDisabled

    class Span(span: Any)

}

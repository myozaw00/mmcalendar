package com.myozawoo.mmcalendar.view

import com.myozawoo.mmcalendar.CalendarDay

interface DayViewDecorator {

    /**
     * Determine if a specify day should be decorated
     *
     */
    fun shouldDecorate(day: CalendarDay): Boolean

    fun decorate(view: DayViewFacade)
}
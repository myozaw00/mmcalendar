package com.myozawoo.mmcalendar

import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.LocalDate


class CalendarDay : Parcelable {

    /**
     * Everything is based on this variable
     */
    private lateinit var date: LocalDate

    constructor(parcel: Parcel): this(parcel.readInt(), parcel.readInt(), parcel.readInt())

    companion object CREATOR : Parcelable.Creator<CalendarDay> {
        override fun createFromParcel(parcel: Parcel): CalendarDay {
            return CalendarDay(parcel)
        }

        override fun newArray(size: Int): Array<CalendarDay?> {
            return arrayOfNulls(size)
        }

        fun today(): CalendarDay {
            return from(LocalDate.now())!!
        }

        fun currentMonth(): CalendarDay {
            return from(LocalDate.now().year, LocalDate.now().monthValue, 1)
        }

        fun from(year: Int, month: Int, day: Int): CalendarDay {
            return CalendarDay(year, month, day)
        }

        fun from(localDate: LocalDate?): CalendarDay? {
            if (localDate == null) return null
            return CalendarDay(localDate)
        }

        private fun hashCode(year: Int, month: Int, day: Int): Int {
            return (year*10000) + (month*100) + day
        }
    }


    constructor(date: LocalDate)  {
        this.date = date
    }

    constructor(year: Int, month: Int, day: Int)  {
        date = LocalDate.of(year, month, day)
    }

    fun getYear(): Int = date.year

    fun getMonth(): Int = date.monthValue

    fun getDay(): Int = date.dayOfMonth

    fun getDate(): LocalDate = date


    /**
     * Determine if this day is within a specified range
     * @param minDate the earliest day, may be null
     * @param maxDate the latest day, may be null
     * @return true if the between (inclusive) the min and max
     */
    fun isInRange(minDate: CalendarDay?, maxDate: CalendarDay?): Boolean {
        return !(minDate != null && minDate.isAfter(this)) &&
                !(maxDate != null && maxDate.isBefore(this))
    }

    fun isBefore(other: CalendarDay): Boolean = date.isBefore(other.date)

    fun isAfter(other: CalendarDay): Boolean = date.isAfter(other.date)

    override fun hashCode(): Int {
        return CREATOR.hashCode(date.year, date.monthValue, date.dayOfMonth)
    }

    override fun toString(): String {
        return "CalendarDay{${date.year}-${date.monthValue}-${date.dayOfMonth}}"
    }


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(date.year)
        dest?.writeInt(date.monthValue)
        dest?.writeInt(date.dayOfMonth)
    }

    override fun describeContents(): Int = 0




}
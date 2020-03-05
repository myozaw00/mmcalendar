package com.myozawoo.mmcalendar.utils

object DayUtils {

    private val holidays = arrayListOf<PublicHoliday>()
    private val leapYearThinggyanHolidays = arrayListOf<PublicHoliday>()

    init {
        holidays.add(PublicHoliday(1, 1, "နိုင်ငံတာကာနှစ်သစ်ကူးနေ့"))
        holidays.add(PublicHoliday(1, 4, "လွတ်လပ်ရေးနေ့"))
        holidays.add(PublicHoliday(2, 12, "ပြည်ထောင်စုနေ့"))
        holidays.add(PublicHoliday(3, 2, "တောင်သူလယ်သမားနေ့"))
        holidays.add(PublicHoliday(3, 27, "တပ်မတော်နေ့"))

        holidays.add(PublicHoliday(4, 13, "သင်္ကြန်အကြိုနေ့"))
        holidays.add(PublicHoliday(4, 14, "သင်္ကြန်အကျနေ့"))
        holidays.add(PublicHoliday(4, 15, "သင်္ကြန်အကြတ်နေ့"))
        holidays.add(PublicHoliday(4, 16, "သင်္ကြန်အတက်"))
        holidays.add(PublicHoliday(4, 17, "နှစ်ဆန်းတစ်ရက်နေ့"))

        leapYearThinggyanHolidays.add(PublicHoliday(4, 12, "သင်္ကြန်အကြိုနေ့"))
        leapYearThinggyanHolidays.add(PublicHoliday(4, 13, "သင်္ကြန်အကျနေ့"))
        leapYearThinggyanHolidays.add(PublicHoliday(4, 14, "သင်္ကြန်အကြတ်နေ့"))
        leapYearThinggyanHolidays.add(PublicHoliday(4, 15, "သင်္ကြန်အကြတ်နေ့"))
        leapYearThinggyanHolidays.add(PublicHoliday(4, 16, "သင်္ကြန်အတက်"))
        leapYearThinggyanHolidays.add(PublicHoliday(4, 17, "နှစ်ဆန်းတစ်ရက်နေ့"))

        holidays.add(PublicHoliday(5, 1, "အလုပ်သမားနေ့"))
        holidays.add(PublicHoliday(7, 19, "အာဇာနည်နေ့"))
        holidays.add(PublicHoliday(12, 9, "အမျိုးသားနေ့"))
        holidays.add(PublicHoliday(12, 25, "ခရစ္စမတ်နေ့"))



    }

    fun getPublicHoliday(year: Int, month: Int, day: Int): String {
        var holiday = ""
        if (month == 4 && (year%4==0)) {
            leapYearThinggyanHolidays.forEach {
                if (it.month == month && it.day == day) {
                    holiday = it.holidayName
                    return@forEach
                }
            }
        }else {
            holidays.forEach {
                if (it.month == month && it.day == day) {
                    holiday = it.holidayName
                    return@forEach
                }
            }
        }
        return ""
    }

    data class PublicHoliday(val month: Int,
                             val day: Int,
                             val holidayName: String)

}
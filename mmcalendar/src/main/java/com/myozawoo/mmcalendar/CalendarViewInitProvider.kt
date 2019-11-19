package com.myozawoo.mmcalendar

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen

class CalendarViewInitProvider : ContentProvider() {

    private val CV_AUTHORITY = "com.myozawoo.mmcalendar.calendarviewinitprovider"

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun onCreate(): Boolean {
        Log.d("CalendarView", "Calendar view is initiated.")
        AndroidThreeTen.init(context)
        return true
    }

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        if (info == null) {
            throw NullPointerException("CalendarViewInitProvider ProviderInfo cannot be null.")
        }
        if (CV_AUTHORITY == info.authority) {
            throw IllegalStateException("Incorrect provider authority in manifest. Most likely due " +
                    "to a missing applicationId variable in application\'s build.gradle.")
        }
        super.attachInfo(context, info)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}
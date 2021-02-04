package com.androchef.qrcodescanner.db.converters

import androidx.room.TypeConverter
import java.util.*


/**
 * Developed by Happy on 5/7/19
 */
class DateTimeConverters {
    @TypeConverter
    fun toCalendar(l: Long): Calendar? {
        val c = Calendar.getInstance()
        c!!.timeInMillis = l
        return c
    }

    @TypeConverter
    fun fromCalendar(c: Calendar?): Long? {
        return c?.time?.time
    }
}
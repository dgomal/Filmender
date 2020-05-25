package com.bossdga.filmender.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Utility class to work with dates
 */
object DateUtils {
    /**
     * Method that transforms a date from a format to another format
     * @param date
     * @return
     */
    @JvmStatic
    fun formatDate(date: String): String? {
        val original = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val myFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        return myFormat.format(original.parse(date))
    }

    @JvmStatic
    fun fromMinutesToHHmm(minutes: Int): String {
        val hours = TimeUnit.MINUTES.toHours(minutes.toLong())
        val remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours)
        return String.format("%02d:%02d", hours, remainMinutes)
    }
}
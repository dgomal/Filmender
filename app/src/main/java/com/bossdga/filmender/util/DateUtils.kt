package com.bossdga.filmender.util

import java.text.SimpleDateFormat
import java.util.*

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
}
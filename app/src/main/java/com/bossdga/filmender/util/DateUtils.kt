package com.bossdga.filmender.util

import java.util.concurrent.TimeUnit

/**
 * Utility class to work with dates
 */
object DateUtils {
    @JvmStatic
    fun fromMinutesToHHmm(minutes: Int): String {
        val hours = TimeUnit.MINUTES.toHours(minutes.toLong())
        val remainMinutes = minutes.minus(TimeUnit.HOURS.toMinutes(hours))
        return String.format("%02d:%02d", hours, remainMinutes)
    }
}
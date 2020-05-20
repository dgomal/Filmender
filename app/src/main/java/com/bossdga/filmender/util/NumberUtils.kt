package com.bossdga.filmender.util

/**
 * This class is a number Utility class
 */
object NumberUtils {
    /**
     * Returns a formatted two decimals double
     * @param number
     * @return
     */
    fun getFormattedDouble(number: Double): String {
        return String.format("%.02f", number)
    }

    /**
     * Returns a random number from the provided range
     */
    fun getRandomNumberInRange(low: Int, high: Int): Int {
        return (low..high).shuffled().first()
    }
}
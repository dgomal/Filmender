package com.bossdga.filmender.util

import android.content.Context
import com.bossdga.filmender.source.network.RetrofitService

/**
 * This class is a number Utility class
 */
object PreferenceUtils {
    fun getGenres(context: Context): String {
        var result = ""
        val entries: Set<String>? = AppSettings(context, "SETTINGS_PREFERENCES").genres
        entries?.forEach { e -> result = result.plus(e).plus("|")}

        return result
    }

    fun getRating(context: Context): String? {
        return AppSettings(context, "SETTINGS_PREFERENCES").rating
    }

    fun getYearFrom(context: Context): String? {
        return AppSettings(context, "SETTINGS_PREFERENCES").yearFrom + "-01-01"
    }

    fun getYearTo(context: Context): String? {
        return AppSettings(context, "SETTINGS_PREFERENCES").yearTo + "-01-01"
    }

    fun getType(context: Context): String? {
        return AppSettings(context, "SETTINGS_PREFERENCES").type
    }

    fun getResults(context: Context): Int? {
        return AppSettings(context, "SETTINGS_PREFERENCES").results
    }
}
package com.bossdga.filmender.util

import android.content.Context
import com.bossdga.filmender.source.network.RetrofitService

/**
 * This class is a number Utility class
 */
object PreferenceUtils {
    private lateinit var context: Context

    fun initWith(context: Context){
        this.context = context
    }

    fun getGenres(): String {
        var result = ""
        val entries: Set<String>? = AppSettings(context, "SETTINGS_PREFERENCES").genres
        entries?.forEach { e -> result = result.plus(e).plus("|")}

        return result
    }

    fun getRating(): String? {
        return AppSettings(context, "SETTINGS_PREFERENCES").rating
    }

    fun getYearFrom(): String {
        return AppSettings(context, "SETTINGS_PREFERENCES").yearFrom + "-01-01"
    }

    fun getYearTo(): String {
        return AppSettings(context, "SETTINGS_PREFERENCES").yearTo + "-12-31"
    }

    fun getType(): String? {
        return AppSettings(context, "SETTINGS_PREFERENCES").type
    }

    fun getResults(): Int? {
        return AppSettings(context, "SETTINGS_PREFERENCES").results
    }

    fun getImageUrl(): String? {
        return AppSettings(context, "SETTINGS_PREFERENCES").imageUrl
    }

    fun setImageUrl(imageUrl: String) {
        AppSettings(context, "SETTINGS_PREFERENCES").imageUrl = imageUrl
    }

    fun getOriginalLanguage(): String? {
        return AppSettings(context, "SETTINGS_PREFERENCES").originalLanguage
    }

    fun getSort(): String? {
        return AppSettings(context, "SETTINGS_PREFERENCES").sort
    }
}
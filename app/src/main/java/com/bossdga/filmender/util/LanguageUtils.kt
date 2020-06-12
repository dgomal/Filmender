package com.bossdga.filmender.util

import android.content.res.Resources
import android.os.Build

object LanguageUtils {

    fun getSystemLanguage(): String {
        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0].language
        } else {
            Resources.getSystem().configuration.locale.language
        }

        return systemLocale
    }

    fun getSystemCountry(): String {
        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0].country
        } else {
            Resources.getSystem().configuration.locale.country
        }

        return systemLocale
    }

}
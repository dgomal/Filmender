package com.bossdga.filmender.util

import android.content.res.Resources
import android.os.Build

object LanguageUtils {

    fun getSystemLanguage(): String {
        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0]
        } else {
            Resources.getSystem().configuration.locale
        }

        return systemLocale.toString()
    }

}
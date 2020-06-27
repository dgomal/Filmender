package com.bossdga.filmender.util

import android.content.Context

class AppSettings(var context: Context, var name: String): Preferences(context, name) {
    var sort by stringPref()
    var yearFrom by stringPref()
    var yearTo by stringPref()
    var rating by stringPref()
    var results by intPref()
    var type by stringPref()
    var genres by stringSetPref()
    var imageUrl by stringPref()
    var originalLanguage by stringPref()
}
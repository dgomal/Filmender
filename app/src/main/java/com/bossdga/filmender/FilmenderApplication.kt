package com.bossdga.filmender

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.bossdga.filmender.source.network.RetrofitService
import com.bossdga.filmender.util.PreferenceUtils
import com.google.android.gms.ads.MobileAds

class FilmenderApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        RetrofitService.initWith(this)
        PreferenceUtils.initWith(this)

        PreferenceManager.setDefaultValues(this, "SETTINGS_PREFERENCES", Context.MODE_PRIVATE, R.xml.preferences, false)

        MobileAds.initialize(this)
    }
}
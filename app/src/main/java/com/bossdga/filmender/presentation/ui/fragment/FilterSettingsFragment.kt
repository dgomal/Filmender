package com.bossdga.filmender.presentation.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.bossdga.filmender.R

class FilterSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = "SETTINGS_PREFERENCES";
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
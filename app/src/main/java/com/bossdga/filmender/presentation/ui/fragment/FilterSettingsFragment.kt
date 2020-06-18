package com.bossdga.filmender.presentation.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.bossdga.filmender.R
import com.bossdga.filmender.util.AnalyticsUtils
import com.bossdga.filmender.util.PreferenceUtils
import com.google.firebase.analytics.FirebaseAnalytics

class FilterSettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var bundle = Bundle()
    protected lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = "SETTINGS_PREFERENCES";
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        lateinit var itemId: String
        lateinit var itemName: String
        lateinit var value: String
        if (key.equals("results")) {
            itemId = "results"
            itemName = "action_preference_results"
            value = PreferenceUtils.getResults().toString()
        }
        if (key.equals("type")) {
            itemId = "type"
            itemName = "action_preference_type"
            value = PreferenceUtils.getType().toString()
        }
        if (key.equals("yearFrom")) {
            itemId = "yearFrom"
            itemName = "action_preference_yearFrom"
            value = PreferenceUtils.getYearFrom()
        }
        if (key.equals("yearTo")) {
            itemId = "yearTo"
            itemName = "action_preference_yearTo"
            value = PreferenceUtils.getYearTo()
        }
        if (key.equals("rating")) {
            itemId = "rating"
            itemName = "action_preference_rating"
            value = PreferenceUtils.getRating().toString()
        }
        if (key.equals("genres")) {
            itemId = "genres"
            itemName = "action_preference_genres"
            value = PreferenceUtils.getGenres()
        }
        if (key.equals("originalLanguage")) {
            val language = if(PreferenceUtils.getOriginalLanguage().toString().isEmpty()) {
                "all"
            } else {
                PreferenceUtils.getOriginalLanguage().toString()
            }

            itemId = "originalLanguage"
            itemName = "action_preference_originalLanguage"
            value = language
        }
        bundle = AnalyticsUtils.selectContent(itemId, itemName, "preference", value)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    override fun onResume() {
        super.onResume()

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}
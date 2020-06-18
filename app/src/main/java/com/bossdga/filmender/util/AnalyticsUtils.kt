package com.bossdga.filmender.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Utility class to work with dates
 */
object AnalyticsUtils {
    @JvmStatic
    fun selectContent(itemId: String, itemName: String, contentType: String, value: String = ""): Bundle {
        val bundle = Bundle()

        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        bundle.putString(FirebaseAnalytics.Param.VALUE, value)

        return bundle
    }
}
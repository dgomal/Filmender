package com.bossdga.filmender.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * This is a Utility class to provide network functionality
 */
object NetworkUtils {
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}
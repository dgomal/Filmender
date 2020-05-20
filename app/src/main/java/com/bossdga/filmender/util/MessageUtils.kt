package com.bossdga.filmender.util

import android.content.Context
import android.widget.Toast

/**
 * Utility class to show messages
 */
object MessageUtils {
    /**
     * Displays a message from a String id
     * @param context
     * @param message
     * @param length
     */
    fun showMessage(context: Context?, message: Int, length: Int) {
        if (context != null) {
            val text = context.resources.getString(message)
            showMessage(context, text, length)
        }
    }

    /**
     * Displays a message from a String
     * @param context
     * @param message
     */
    private fun showMessage(context: Context?, message: String, length: Int) {
        if (context != null) {
            Toast.makeText(context, message, length).show()
        }
    }
}
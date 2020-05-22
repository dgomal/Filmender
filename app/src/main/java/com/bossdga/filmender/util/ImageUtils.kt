package com.bossdga.filmender.util

import android.widget.ImageView
import coil.api.load

/**
 * Utility class to deal with images
 */
object ImageUtils {
    private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/"
    /**
     * Sets an image to an ImageView from an url
     * @param context
     * @param image
     * @param url
     */
    fun setImage(image: ImageView, url: String?) {
        image.load(IMAGE_BASE_URL + url)
    }
}
package com.bossdga.filmender.util

import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Utility class to deal with images
 */
object ImageUtils {
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/"
    /**
     * Sets an image to an ImageView from an url
     * @param context
     * @param image
     * @param url
     */
    fun setImage(image: ImageView, url: String?) {
        Picasso.get().load(IMAGE_BASE_URL + url).into(image)
    }
}
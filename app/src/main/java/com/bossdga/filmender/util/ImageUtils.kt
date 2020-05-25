package com.bossdga.filmender.util

import android.content.Context
import android.widget.ImageView
import coil.api.load
import com.bossdga.filmender.model.content.ImageType

/**
 * Utility class to deal with images
 */
object ImageUtils {
    /**
     * Sets an image to an ImageView from an url
     * @param context
     * @param image
     * @param url
     */
    fun setImage(context: Context, image: ImageView, url: String?, imageType: ImageType) {
        val size: String?
        when (imageType) {
            ImageType.POSTER -> {
                size = "original"
            }
            ImageType.BACK_DROP -> {
                size = "w1280"
            }
        }
        image.load(PreferenceUtils.getImageUrl(context) + size + url)
    }
}
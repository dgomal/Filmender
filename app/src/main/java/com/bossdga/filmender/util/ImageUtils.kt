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
    fun setImage(image: ImageView, url: String?, imageType: ImageType) {
        val size: String
        when (imageType) {
            ImageType.POSTER -> {
                size = "w154"
            }
            ImageType.BACK_DROP -> {
                size = "w780"
            }
            ImageType.PROFILE -> {
                size = "w185"
            }
            ImageType.PROFILE_LARGE -> {
                size = "h632"
            }
        }
        image.load(PreferenceUtils.getImageUrl() + size + url) {
            crossfade(true)
            //placeholder(R.drawable.image_placeholder)
            //transformations(CircleCropTransformation())
        }
    }
}
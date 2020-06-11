package com.bossdga.filmender.model.content

/**
 * Enum declaring all the image types
 */
enum class ImageType(val typeId: Int) {
    POSTER(0), BACK_DROP(1), PROFILE(2), PROFILE_LARGE(3);

    companion object {
        fun contentByInt(id: Int): ImageType {
            for (contentType in values()) {
                if (contentType.typeId == id) {
                    return contentType
                }
            }
            throw IllegalArgumentException("Invalid Type id: $id")
        }
    }

}
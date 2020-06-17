package com.bossdga.filmender.model.content

/**
 * Enum declaring all the image types
 */
enum class AdType(val typeId: Int) {
    SMALL(0), MEDIUM(1), BIG(2);

    companion object {
        fun contentByInt(id: Int): AdType {
            for (contentType in values()) {
                if (contentType.typeId == id) {
                    return contentType
                }
            }
            throw IllegalArgumentException("Invalid Type id: $id")
        }
    }

}
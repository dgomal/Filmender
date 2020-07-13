package com.bossdga.filmender.model.content

/**
 * Enum declaring all the image types
 */
enum class LayoutType(val typeId: Int) {
    SIMPLE(0), NORMAL(1), COMPLEX(2);

    companion object {
        fun contentByInt(id: Int): LayoutType {
            for (contentType in values()) {
                if (contentType.typeId == id) {
                    return contentType
                }
            }
            throw IllegalArgumentException("Invalid Type id: $id")
        }
    }

}
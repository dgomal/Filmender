package com.bossdga.filmender.presentation.adapter

/**
 * Enum declaring all the ViewHolder types
 */
enum class ViewHolderType(val typeId: Int) {
    SIMPLE(0), COMPLEX(1);

    companion object {
        fun contentByInt(id: Int): ViewHolderType {
            for (contentType in values()) {
                if (contentType.typeId == id) {
                    return contentType
                }
            }
            throw IllegalArgumentException("Invalid Type id: $id")
        }
    }

}
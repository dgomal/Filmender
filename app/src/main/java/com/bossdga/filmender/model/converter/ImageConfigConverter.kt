package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.ImageConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImageConfigConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToImageConfig(data: String?): ImageConfig? {
        if (data == null){
            return ImageConfig("", "")
        }
        val type = object : TypeToken<ImageConfig>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun imageConfigToString(imageConfig: ImageConfig?): String? {
        return gson.toJson(imageConfig)
    }
}
package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.content.Image
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImageConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToImage(data: String?): Image? {
        val type = object : TypeToken<Image>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun imageToString(image: Image?): String? {
        return gson.toJson(image)
    }
}
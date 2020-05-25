package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.content.Image
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ImageListConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToimages(data: String?): List<Image>? {

        if (data == null){
            return Collections.emptyList()
        }
        val listType = object : TypeToken<ArrayList<Image>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun imagesToString(images: List<Image>?): String? {
        return gson.toJson(images)
    }
}
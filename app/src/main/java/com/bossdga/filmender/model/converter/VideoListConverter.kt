package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class VideoListConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<Video>? {

        if (data == null){
            return Collections.emptyList()
        }
        val listType = object : TypeToken<ArrayList<Video>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToString(genres: List<Video>?): String? {
        return gson.toJson(genres)
    }
}
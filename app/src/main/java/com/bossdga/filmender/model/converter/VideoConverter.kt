package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.content.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VideoConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToVideo(data: String?): Video? {
        val type = object : TypeToken<Video>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun videoToString(video: Video?): String? {
        return gson.toJson(video)
    }
}
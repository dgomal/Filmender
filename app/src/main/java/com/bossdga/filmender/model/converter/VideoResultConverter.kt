package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.content.VideoResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VideoResultConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToVideoResult(data: String?): VideoResult? {
        val type = object : TypeToken<VideoResult>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun videoResultToString(videoResult: VideoResult?): String? {
        return gson.toJson(videoResult)
    }
}
package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.content.Cast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class CastListConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToCast(data: String?): List<Cast>? {

        if (data == null){
            return Collections.emptyList()
        }
        val listType = object : TypeToken<ArrayList<Cast>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun castToString(genres: List<Cast>?): String? {
        return gson.toJson(genres)
    }
}
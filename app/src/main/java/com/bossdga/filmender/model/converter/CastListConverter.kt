package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.content.People
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class CastListConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToCast(data: String?): List<People>? {

        if (data == null){
            return Collections.emptyList()
        }
        val listType = object : TypeToken<ArrayList<People>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun castToString(genres: List<People>?): String? {
        return gson.toJson(genres)
    }
}
package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.content.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class GenreListConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToGenres(data: String?): List<Genre>? {

        if (data == null){
            return Collections.emptyList()
        }
        val listType = object : TypeToken<ArrayList<Genre>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun genresToString(genres: List<Genre>?): String? {
        return gson.toJson(genres)
    }
}
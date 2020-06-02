package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.content.Genre
import com.bossdga.filmender.model.content.Network
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class NetworkListConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToNetworks(data: String?): List<Network>? {

        if (data == null){
            return Collections.emptyList()
        }
        val listType = object : TypeToken<ArrayList<Network>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun networksToString(networks: List<Network>?): String? {
        return gson.toJson(networks)
    }
}
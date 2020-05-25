package com.bossdga.filmender.model.converter

import androidx.room.TypeConverter
import com.bossdga.filmender.model.content.Credit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreditConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToCredit(data: String?): Credit? {
        val type = object : TypeToken<Credit>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun creditToString(credit: Credit?): String? {
        return gson.toJson(credit)
    }
}
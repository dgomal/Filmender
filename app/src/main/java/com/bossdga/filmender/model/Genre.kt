package com.bossdga.filmender.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class Genre(@PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") var id: Int,
            @ColumnInfo(name = "name") @SerializedName("name") var name: String) {
}
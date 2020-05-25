package com.bossdga.filmender.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "configuration")
class ApiConfig(@PrimaryKey @ColumnInfo(name = "id") var id: Int,
                @SerializedName("images") var images: ImageConfig) {
}
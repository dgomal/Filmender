package com.bossdga.filmender.model.content

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class Cast(@PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") var id: Int,
           @ColumnInfo(name = "name") @SerializedName("name") var name: String) {
}
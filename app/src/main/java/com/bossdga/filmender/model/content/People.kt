package com.bossdga.filmender.model.content

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class People(@PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") override var id: Int,
             @ColumnInfo(name = "name") @SerializedName("name") var name: String,
             @ColumnInfo(name = "profile_path") @SerializedName("profile_path") var profilePath: String?): BaseContent {
}
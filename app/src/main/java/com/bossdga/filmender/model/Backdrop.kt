package com.bossdga.filmender.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class Backdrop(@PrimaryKey @ColumnInfo(name = "aspect_ratio") @SerializedName("aspect_ratio") var aspectRatio: Int,
               @ColumnInfo(name = "file_path") @SerializedName("file_path") var filePath: String,
               @ColumnInfo(name = "height") @SerializedName("height") var height: Int,
               @ColumnInfo(name = "width") @SerializedName("width") var width: Int,
               @ColumnInfo(name = "vote_average") @SerializedName("vote_average") var voteAverage: Int) {
}
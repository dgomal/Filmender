package com.bossdga.filmender.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class Image(@PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") var id: Int,
            @SerializedName("backdrops") var backdrops: List<Backdrop>,
            @SerializedName("posters") var posters: List<Poster>) {
}
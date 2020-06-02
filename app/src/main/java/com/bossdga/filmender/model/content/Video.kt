package com.bossdga.filmender.model.content

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class Video(@PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") var id: String,
            @ColumnInfo(name = "name") @SerializedName("name") var name: String,
            @ColumnInfo(name = "site") @SerializedName("site") var site: String,
            @ColumnInfo(name = "size") @SerializedName("size") var size: Int,
            @ColumnInfo(name = "type") @SerializedName("type") var type: String,
            @ColumnInfo(name = "key") @SerializedName("key") var key: String) {
}
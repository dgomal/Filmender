package com.bossdga.filmender.model.content

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class Network(@PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") var id: Int,
              @ColumnInfo(name = "name") @SerializedName("name") var name: String,
              @ColumnInfo(name = "logo_path") @SerializedName("logo_path") var logoPath: String) {
}
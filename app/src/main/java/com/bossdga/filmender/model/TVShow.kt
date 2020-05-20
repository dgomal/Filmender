package com.bossdga.filmender.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Class that represents a tv show
 */
@Entity(tableName = "tv_show")
data class TVShow(@PrimaryKey @ColumnInfo(name = "id") @Expose @SerializedName("id") override var id: Int,
                  @ColumnInfo(name = "poster_path") @Expose @SerializedName("poster_path") override var posterPath: String,
                  @ColumnInfo(name = "name") @Expose @SerializedName("name") var name: String,
                  @ColumnInfo(name = "overview") @Expose @SerializedName("overview") var overview: String,
                  @ColumnInfo(name = "first_air_date") @Expose @SerializedName("first_air_date") var firstAirDate: String) : BaseContent {

}
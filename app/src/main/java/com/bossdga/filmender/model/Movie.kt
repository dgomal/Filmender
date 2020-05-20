package com.bossdga.filmender.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Class that represents a movie
 */
@Entity(tableName = "movie")
data class Movie(@PrimaryKey @ColumnInfo(name = "id") @Expose @SerializedName("id") override var id: Int,
                 @ColumnInfo(name = "poster_path") @Expose @SerializedName("poster_path") override var posterPath: String,
                 @ColumnInfo(name = "title") @Expose @SerializedName("title") var title: String,
                 @ColumnInfo(name = "overview") @Expose @SerializedName("overview") var overview: String,
                 @ColumnInfo(name = "release_date") @Expose @SerializedName("release_date") var releaseDate: String) : BaseContent {

}
package com.bossdga.filmender.model.content

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Class that represents a tv show
 */
@Entity(tableName = "tv_show")
data class TVShow(@PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") override var id: Int,
                  @ColumnInfo(name = "poster_path") @SerializedName("poster_path") var posterPath: String?,
                  @ColumnInfo(name = "backdrop_path") @SerializedName("backdrop_path") var backdropPath: String,
                  @ColumnInfo(name = "name") @SerializedName("name") var title: String,
                  @ColumnInfo(name = "overview") @SerializedName("overview") var overview: String,
                  @ColumnInfo(name = "first_air_date") @SerializedName("first_air_date") var releaseDate: String,
                  @ColumnInfo(name = "vote_average") @SerializedName("vote_average") var voteAverage: String,
                  @SerializedName("number_of_seasons") var numberOfSeasons: Int,
                  @SerializedName("genres") var genres: List<Genre>,
                  @SerializedName("credits") var credits: Credit,
                  var images: Image,
                  var videos: Video): BaseContent {

}
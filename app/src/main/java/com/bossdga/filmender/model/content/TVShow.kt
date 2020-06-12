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
                  @ColumnInfo(name = "imdb_id") @SerializedName("imdb_id") var imdbId: String?,
                  @ColumnInfo(name = "poster_path") @SerializedName("poster_path") var posterPath: String?,
                  @ColumnInfo(name = "backdrop_path") @SerializedName("backdrop_path") var backdropPath: String?,
                  @ColumnInfo(name = "name") @SerializedName("name") var title: String,
                  @ColumnInfo(name = "overview") @SerializedName("overview") var overview: String,
                  @ColumnInfo(name = "first_air_date") @SerializedName("first_air_date") var releaseDate: String,
                  @ColumnInfo(name = "vote_average") @SerializedName("vote_average") var voteAverage: String,
                  @ColumnInfo(name = "vote_count") @SerializedName("vote_count") var voteCount: String,
                  @ColumnInfo(name = "number_of_seasons") @SerializedName("number_of_seasons") var numberOfSeasons: Int,
                  @ColumnInfo(name = "popularity") @SerializedName("popularity") var popularity: Double,
                  @ColumnInfo(name = "genres") @SerializedName("genres") var genres: List<Genre>,
                  @ColumnInfo(name = "credits") @SerializedName("credits") var credits: Credit,
                  @ColumnInfo(name = "images") @SerializedName("images") var images: Image,
                  @ColumnInfo(name = "videos") @SerializedName("videos") var videos: VideoResult,
                  @ColumnInfo(name = "networks") @SerializedName("networks") var networks: List<Network>): BaseContent {

}
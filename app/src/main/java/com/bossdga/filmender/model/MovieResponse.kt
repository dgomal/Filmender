package com.bossdga.filmender.model

import com.google.gson.annotations.SerializedName

/**
 * Class that represents a movie response
 */
data class MovieResponse(@SerializedName("total_results") var totalResults: Int,
                         @SerializedName("total_pages") var totalPages: Int,
                         @SerializedName("results") var results: List<Movie>) {

}
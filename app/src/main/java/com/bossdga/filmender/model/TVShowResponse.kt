package com.bossdga.filmender.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Class that represents a tv show response
 */
data class TVShowResponse(@Expose @SerializedName("total_results") var totalResults: Int,
                          @Expose @SerializedName("total_pages") var totalPages: Int,
                          @Expose @SerializedName("results") var results: List<TVShow>) {

}
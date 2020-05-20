package com.bossdga.filmender.model

import com.google.gson.annotations.SerializedName

/**
 * Class that represents a tv show response
 */
data class TVShowResponse(@SerializedName("total_results") var totalResults: Int,
                          @SerializedName("total_pages") var totalPages: Int,
                          @SerializedName("results") var results: List<TVShow>) {

}
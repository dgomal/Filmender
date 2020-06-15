package com.bossdga.filmender.source.network.api

import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.model.content.TVShowResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface that defines the API requests
 */
interface TVShowAPI {
    /**
     * Gets a list of tv shows
     * @return
     */
    @GET("/3/discover/tv")
    fun getTVShows(@Query("page") page: Int,
                   @Query("first_air_date.gte") airDateGte: String?,
                   @Query("first_air_date.lte") airDateLte: String?,
                   @Query("vote_average.gte") voteAverageGte: String?,
                   @Query("with_genres") withGenres: String?,
                   @Query("with_original_language") withOriginalLanguage: String?
    ): Observable<TVShowResponse>

    /**
     * Gets the tv show details
     * @param tvShowId
     * @return
     */
    @GET("/3/tv/{tv_id}")
    fun getTVShowDetails(@Path("tv_id") tvShowId: Int?,
                         @Query("append_to_response") appendToResponse: String?): Single<TVShow>
}
package com.bossdga.filmender.source.network.api

import com.bossdga.filmender.model.TVShow
import com.bossdga.filmender.model.TVShowResponse
import io.reactivex.Observable
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
                   @Query("air_date.gte") airDateGte: String?,
                   @Query("air_date.lte") airDateLte: String?,
                   @Query("vote_average.gte") voteAverageGte: String?,
                   @Query("with_genres") withGenres: String?
    ): Observable<TVShowResponse>

    /**
     * Gets the tv show details
     * @param tvShowId
     * @return
     */
    @GET("/3/tv/{tv_id}")
    fun getTVShowDetails(@Path("tv_id") tvShowId: Int): Observable<TVShow>
}
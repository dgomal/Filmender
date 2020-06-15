package com.bossdga.filmender.source.network.api

import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.model.content.MovieResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface that defines the API requests
 */
interface MovieAPI {
    /**
     * Gets a list of movies
     * @return
     */
    @GET("/3/discover/movie")
    fun getMovies(@Query("page") page: Int,
                  @Query("primary_release_date.gte") releaseDateGte: String?,
                  @Query("primary_release_date.lte") releaseDateLte: String?,
                  @Query("vote_average.gte") voteAverageGte: String?,
                  @Query("with_genres") withGenres: String?,
                  @Query("with_original_language") withOriginalLanguage: String?
    ): Observable<MovieResponse>

    /**
     * Gets the movie details
     * @param movieId
     * @return
     */
    @GET("/3/movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int?,
                        @Query("append_to_response") appendToResponse: String?): Single<Movie>
}
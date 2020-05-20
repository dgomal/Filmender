package com.bossdga.filmender.source.network.api

import com.bossdga.filmender.model.Movie
import com.bossdga.filmender.model.MovieResponse
import io.reactivex.Observable
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
                  @Query("release_date.gte") releaseDateGte: String?,
                  @Query("release_date.lte") releaseDateLte: String?
    ): Observable<MovieResponse>

    /**
     * Gets the movie details
     * @param movieId
     * @return
     */
    @GET("/3/movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int): Observable<Movie>
}
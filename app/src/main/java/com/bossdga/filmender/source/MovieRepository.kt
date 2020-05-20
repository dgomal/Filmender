package com.bossdga.filmender.source

import com.bossdga.filmender.model.Movie
import com.bossdga.filmender.model.MovieResponse
import com.bossdga.filmender.source.network.api.MovieAPI
import com.bossdga.filmender.source.persistence.MovieDao
import io.reactivex.Observable

/**
 * Repository to execute database and network operations
 */
class MovieRepository(private val dao: MovieDao, private val api: MovieAPI) {
    fun getMovies(page: Int, releaseDateGte: String?, releaseDateLte: String?): Observable<MovieResponse> {
        return api.getMovies(page, releaseDateGte, releaseDateLte)
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getMovies();
    }

    fun getMovieDetails(movieId: Int): Observable<Movie> {
        return api.getMovieDetails(movieId)
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getMovieDetails(movieId);
    }
}
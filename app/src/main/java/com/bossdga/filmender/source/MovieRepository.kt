package com.bossdga.filmender.source

import android.os.AsyncTask
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.model.content.MovieResponse
import com.bossdga.filmender.source.network.api.MovieAPI
import com.bossdga.filmender.source.persistence.MovieDao
import io.reactivex.Observable


/**
 * Repository to execute database and network operations
 */
class MovieRepository(private val dao: MovieDao, private val api: MovieAPI) {
    fun getMovies(page: Int, releaseDateGte: String?, releaseDateLte: String?, voteAverageGte: String?, withGenres: String?): Observable<MovieResponse> {
        return api.getMovies(page, releaseDateGte, releaseDateLte, voteAverageGte, withGenres)
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getMovies();
    }

    fun getMovieDetails(movieId: Int?): Observable<Movie> {
        return api.getMovieDetails(movieId, "videos,images,credits")
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getMovieDetails(movieId);
    }

    fun getMovies(): Observable<List<Movie>> {
        return dao.getMovies()
    }

    fun saveMovie(movie: Movie) {
        InsertMovieTask(dao).execute(movie)
    }

    /**
     * AsyncTask that will insert one row to the database
     */
    class InsertMovieTask internal constructor(private val dao: MovieDao): AsyncTask<Movie?, Void?, Void?>() {
        override fun doInBackground(vararg params: Movie?): Void? {
            dao.saveMovie(params[0])
            return null
        }
    }
}
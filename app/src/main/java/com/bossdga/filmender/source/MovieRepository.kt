package com.bossdga.filmender.source

import android.os.AsyncTask
import android.view.View
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.model.content.MovieResponse
import com.bossdga.filmender.source.network.api.MovieAPI
import com.bossdga.filmender.source.persistence.MovieDao
import com.bossdga.filmender.util.NumberUtils
import com.bossdga.filmender.util.PreferenceUtils
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


/**
 * Repository to execute database and network operations
 */
class MovieRepository(private val dao: MovieDao, private val api: MovieAPI) {
    fun getMovies(fromDB: Boolean): Observable<MovieResponse> {
        if(fromDB) {
            // Get observable from dao.getMovies(), get the list and create an observable with MovieResponse
            // return Observable.just(MovieResponse(movieList.size, 1, movieList))
        }
        return api.getMovies(1, PreferenceUtils.getYearFrom(),
            PreferenceUtils.getYearTo(),
            PreferenceUtils.getRating(),
            PreferenceUtils.getGenres())
            .flatMap{ movieResponse: MovieResponse -> api.getMovies(NumberUtils.getRandomNumberInRange(1, movieResponse.totalPages), PreferenceUtils.getYearFrom(),
                PreferenceUtils.getYearTo(),
                PreferenceUtils.getRating(),
                PreferenceUtils.getGenres()) }
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getMovies();
    }

    fun getMovieDetails(movieId: Int?, fromDB: Boolean): Single<Movie> {
        if(fromDB) {
            return dao.getMovieDetails(movieId)
        }
        return api.getMovieDetails(movieId, "videos,images,credits")
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getMovieDetails(movieId);
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
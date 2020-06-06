package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository
import io.reactivex.Observable
import io.reactivex.Single

/**
 * ViewModel that is used with the EventDetailActivity
 */
class MovieDetailViewModel(movieRepository: MovieRepository, tvShowRepository: TVShowRepository) : BaseViewModel(movieRepository, tvShowRepository)  {
    /**
     * Method that returns an Observable of a movie
     * @return
     */
    fun loadMovie(id: Int?, fromDB: Boolean): Single<Movie> {
        return movieRepository.getMovieDetails(id, fromDB)
    }

    fun saveMovie(movie: Movie) {
        movieRepository.saveMovie(movie)
    }
}
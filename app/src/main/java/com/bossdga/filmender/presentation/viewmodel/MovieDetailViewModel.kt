package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.model.Movie
import com.bossdga.filmender.source.MovieRepository
import io.reactivex.Observable

/**
 * ViewModel that is used with the EventDetailActivity
 */
class MovieDetailViewModel(private val movieRepository: MovieRepository) : BaseViewModel()  {
    /**
     * Method that returns an Observable of a movie
     * @return
     */
    fun loadMovie(id: Int?): Observable<Movie> {
        return movieRepository.getMovieDetails(id)
    }
}
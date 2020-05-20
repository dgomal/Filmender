package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.model.Movie
import com.bossdga.filmender.source.MovieRepository
import io.reactivex.Observable

/**
 * ViewModel that is used with the EventDetailActivity
 */
class DetailViewModel(private val movieRepository: MovieRepository) : BaseViewModel()  {
    /**
     * Method that returns an Observable of a Collection of movies
     * @return
     */
    fun loadContent(contentId: Int?): Observable<Movie> {
        return movieRepository.getMovieDetails(contentId)
    }
}
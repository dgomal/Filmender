package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository
import io.reactivex.Observable

/**
 * ViewModel that is used with the EventDetailActivity
 */
class MovieDetailViewModel(private val movieRepository: MovieRepository, tvShowRepository: TVShowRepository) : BaseViewModel(movieRepository, tvShowRepository)  {
    /**
     * Method that returns an Observable of a movie
     * @return
     */
    fun loadMovie(id: Int?, appendToResponse: String?): Observable<Movie> {
        return movieRepository.getMovieDetails(id, appendToResponse)
    }
}
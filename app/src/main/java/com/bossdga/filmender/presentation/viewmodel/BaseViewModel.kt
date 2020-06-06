package com.bossdga.filmender.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bossdga.filmender.model.content.MovieResponse
import com.bossdga.filmender.model.content.TVShowResponse
import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository
import io.reactivex.Observable

/**
 * Base ViewModel that holds common functionality
 */
abstract class BaseViewModel(protected val movieRepository: MovieRepository, protected val tvShowRepository: TVShowRepository) : ViewModel() {
    val loaded = MutableLiveData<String>()
    /**
     * Method that returns an Observable of a Collection of movies
     * @return
     */
    fun loadMovies(fromDB: Boolean): Observable<MovieResponse> {
        return movieRepository.getMovies(fromDB)
    }

    /**
     * Method that returns an Observable of a Collection of tv shows
     * @return
     */
    fun loadTVShows(fromDB: Boolean): Observable<TVShowResponse> {
        return tvShowRepository.getTVShows(fromDB)
    }
}
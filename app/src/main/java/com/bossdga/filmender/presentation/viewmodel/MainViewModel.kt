package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.model.ApiConfig
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.source.ApiConfigRepository
import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository
import io.reactivex.Observable

/**
 * ViewModel used with the MainActivity
 */
class MainViewModel(movieRepository: MovieRepository, tvShowRepository: TVShowRepository, private val apiConfigRepository: ApiConfigRepository) : BaseViewModel(movieRepository, tvShowRepository) {
    /**
     * Method that returns an Observable of a Configuration
     * @return
     */
    fun loadApiConfig(): Observable<ApiConfig> {
        return apiConfigRepository.getConfiguration()
    }

    /**
     * Method that returns an Observable of a Collection of movies
     * @return
     */
    fun loadMoviesFromDB(): Observable<List<Movie>> {
        return movieRepository.getMoviesFromDB()
    }

    /**
     * Method that returns an Observable of a Collection of tv shows
     * @return
     */
    fun loadTVShowsFromDB(): Observable<List<TVShow>> {
        return tvShowRepository.getTVShowsFromDB()
    }

}
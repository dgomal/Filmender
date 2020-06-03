package com.bossdga.filmender.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bossdga.filmender.model.ApiConfig
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.model.content.MovieResponse
import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.model.content.TVShowResponse
import com.bossdga.filmender.source.ApiConfigRepository
import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository
import com.bossdga.filmender.util.NumberUtils
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
    fun loadMovies(): Observable<List<Movie>> {
        return movieRepository.getMovies()
    }

    /**
     * Method that returns an Observable of a Collection of tv shows
     * @return
     */
    fun loadTVShows(): Observable<List<TVShow>> {
        return tvShowRepository.getTVShows()
    }

}
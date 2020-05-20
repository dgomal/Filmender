package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.model.MovieResponse
import com.bossdga.filmender.model.TVShowResponse
import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository
import com.bossdga.filmender.util.NumberUtils
import io.reactivex.Observable

/**
 * ViewModel used with the MainActivity
 */
class MainViewModel(private val movieRepository: MovieRepository, private val tvShowRepository: TVShowRepository) : BaseViewModel() {

    /**
     * Method that returns an Observable of a Collection of movies
     * @return
     */
    fun loadMovies(releaseDateGte: String?, releaseDateLte: String?): Observable<MovieResponse> {
        return movieRepository.getMovies(1, releaseDateGte, releaseDateLte)
            .flatMap{ movieResponse: MovieResponse -> movieRepository.getMovies(NumberUtils.getRandomNumberInRange(1, movieResponse.totalPages), releaseDateGte, releaseDateLte) }
    }

    /**
     * Method that returns an Observable of a Collection of tv shows
     * @return
     */
    fun loadTVShows(airDateGte: String?, airDateLte: String?): Observable<TVShowResponse> {
        return tvShowRepository.getTVShows(1, airDateGte, airDateLte)
            .flatMap{ tvShowResponse: TVShowResponse -> tvShowRepository.getTVShows(NumberUtils.getRandomNumberInRange(1, tvShowResponse.totalPages), airDateGte, airDateLte) }
    }

}
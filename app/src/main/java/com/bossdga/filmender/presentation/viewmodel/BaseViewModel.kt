package com.bossdga.filmender.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bossdga.filmender.model.content.MovieResponse
import com.bossdga.filmender.model.content.TVShowResponse
import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository
import com.bossdga.filmender.util.NumberUtils
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
    fun loadMovies(releaseDateGte: String?, releaseDateLte: String?, voteAverageGte: String?, withGenres: String?): Observable<MovieResponse> {
        return movieRepository.getMovies(1, releaseDateGte, releaseDateLte, voteAverageGte, withGenres)
            .flatMap{ movieResponse: MovieResponse -> movieRepository.getMovies(NumberUtils.getRandomNumberInRange(1, movieResponse.totalPages), releaseDateGte, releaseDateLte, voteAverageGte, withGenres) }
    }

    /**
     * Method that returns an Observable of a Collection of tv shows
     * @return
     */
    fun loadTVShows(airDateGte: String?, airDateLte: String?, voteAverageGte: String?, withGenres: String?): Observable<TVShowResponse> {
        return tvShowRepository.getTVShows(1, airDateGte, airDateLte, voteAverageGte, withGenres)
            .flatMap{ tvShowResponse: TVShowResponse -> tvShowRepository.getTVShows(NumberUtils.getRandomNumberInRange(1, tvShowResponse.totalPages), airDateGte, airDateLte, voteAverageGte, withGenres) }
    }
}
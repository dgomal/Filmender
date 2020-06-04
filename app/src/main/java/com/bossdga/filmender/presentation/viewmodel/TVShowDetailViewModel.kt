package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository
import io.reactivex.Observable

/**
 * ViewModel that is used with the EventDetailActivity
 */
class TVShowDetailViewModel(movieRepository: MovieRepository, tvShowRepository: TVShowRepository) : BaseViewModel(movieRepository, tvShowRepository)  {
    /**
     * Method that returns an Observable of a movie
     * @return
     */
    fun loadTVShow(id: Int?): Observable<TVShow> {
        return tvShowRepository.getTVShowDetails(id)
    }

    fun saveTVShow(tvShow: TVShow) {
        tvShowRepository.saveTVShow(tvShow)
    }
}
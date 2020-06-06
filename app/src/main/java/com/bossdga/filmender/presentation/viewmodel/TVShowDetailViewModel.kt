package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository
import io.reactivex.Observable
import io.reactivex.Single

/**
 * ViewModel that is used with the EventDetailActivity
 */
class TVShowDetailViewModel(movieRepository: MovieRepository, tvShowRepository: TVShowRepository) : BaseViewModel(movieRepository, tvShowRepository)  {
    /**
     * Method that returns an Observable of a movie
     * @return
     */
    fun loadTVShow(id: Int?, fromDB: Boolean): Single<TVShow> {
        return tvShowRepository.getTVShowDetails(id, fromDB)
    }

    fun saveTVShow(tvShow: TVShow) {
        tvShowRepository.saveTVShow(tvShow)
    }
}
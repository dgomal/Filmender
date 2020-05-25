package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.source.TVShowRepository
import io.reactivex.Observable

/**
 * ViewModel that is used with the EventDetailActivity
 */
class TVShowDetailViewModel(private val tvShowRepository: TVShowRepository) : BaseViewModel()  {
    /**
     * Method that returns an Observable of a movie
     * @return
     */
    fun loadTVShow(id: Int?, appendToResponse: String?): Observable<TVShow> {
        return tvShowRepository.getTVShowDetails(id, appendToResponse)
    }
}
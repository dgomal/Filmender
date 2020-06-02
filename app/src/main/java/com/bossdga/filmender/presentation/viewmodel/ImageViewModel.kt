package com.bossdga.filmender.presentation.viewmodel

import com.bossdga.filmender.source.MovieRepository
import com.bossdga.filmender.source.TVShowRepository

/**
 * ViewModel that is used with the EventDetailActivity
 */
class ImageViewModel(private val movieRepository: MovieRepository, tvShowRepository: TVShowRepository) : BaseViewModel(movieRepository, tvShowRepository)  {

}
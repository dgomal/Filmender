package com.bossdga.filmender.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.bossdga.filmender.source.ApiConfigRepository
import com.bossdga.filmender.source.Injector.provideApiConfigRepository
import com.bossdga.filmender.source.TVShowRepository
import com.bossdga.filmender.source.Injector.provideMovieRepository
import com.bossdga.filmender.source.Injector.provideTVShowRepository
import com.bossdga.filmender.source.MovieRepository

/**
 * Factory that returns ViewModel
 */
class ViewModelFactory private constructor(private val movieRepository: MovieRepository,
                                           private val tvShowRepository: TVShowRepository,
                                           private val configRepository: ApiConfigRepository) : NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = with(modelClass) {
        when {
            isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(movieRepository, tvShowRepository, configRepository)
            isAssignableFrom(MovieDetailViewModel::class.java) ->
                MovieDetailViewModel(movieRepository)
            isAssignableFrom(TVShowDetailViewModel::class.java) ->
                TVShowDetailViewModel(tvShowRepository)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: ViewModelFactory(provideMovieRepository(application),
                        provideTVShowRepository(application), provideApiConfigRepository(application)).also { INSTANCE = it }
                }
    }
}
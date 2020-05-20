package com.bossdga.filmender.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.bossdga.filmender.source.TVShowRepository
import com.bossdga.filmender.source.Injector.provideMovieRepository
import com.bossdga.filmender.source.Injector.provideTVShowRepository
import com.bossdga.filmender.source.MovieRepository

/**
 * Factory that returns ViewModel
 */
class ViewModelFactory private constructor(private val movieRepository: MovieRepository, private val tvShowRepository: TVShowRepository) : NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = with(modelClass) {
        when {
            isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(movieRepository, tvShowRepository)
            isAssignableFrom(DetailViewModel::class.java) ->
                DetailViewModel(movieRepository)
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
                        provideTVShowRepository(application)).also { INSTANCE = it }
                }
    }
}
package com.bossdga.filmender.source

import android.app.Application
import com.bossdga.filmender.source.network.RetrofitService
import com.bossdga.filmender.source.persistence.AppDatabase

/**
 * Class responsible of injecting repositories to a view model factory
 */
object Injector {
    /**
     * Provides a movie repository
     * @param application
     * @return
     */
    @JvmStatic
    fun provideMovieRepository(application: Application): MovieRepository {
        val db = AppDatabase.getInstance(application)
        return MovieRepository(db.movieDao(), RetrofitService.movieApi)
    }

    /**
     * Provides a tv show repository
     * @param application
     * @return
     */
    @JvmStatic
    fun provideTVShowRepository(application: Application): TVShowRepository {
        val db = AppDatabase.getInstance(application)
        return TVShowRepository(db.tvShowDao(), RetrofitService.tvShowApi)
    }
}
package com.bossdga.filmender.source

import android.os.AsyncTask
import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.model.content.TVShowResponse
import com.bossdga.filmender.source.network.api.TVShowAPI
import com.bossdga.filmender.source.persistence.TVShowDao
import com.bossdga.filmender.util.NumberUtils
import com.bossdga.filmender.util.PreferenceUtils
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Repository to execute database and network operations
 */
class TVShowRepository(private val dao: TVShowDao, private val api: TVShowAPI) {
    fun getTVShows(fromDB: Boolean): Observable<TVShowResponse> {
        if(fromDB) {
            // Get observable from dao.getTVShows(), get the list and create an observable with TVShowResponse
            // return Observable.just(TVShowResponse(showList.size, 1, showList))
        }
        return api.getTVShows(1, PreferenceUtils.getYearFrom(),
            PreferenceUtils.getYearTo(),
            PreferenceUtils.getRating(),
            PreferenceUtils.getGenres())
            .flatMap{ tvShowResponse: TVShowResponse -> api.getTVShows(NumberUtils.getRandomNumberInRange(1, tvShowResponse.totalPages), PreferenceUtils.getYearFrom(),
                PreferenceUtils.getYearTo(),
                PreferenceUtils.getRating(),
                PreferenceUtils.getGenres()) }
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getTVShows();
    }

    fun getTVShowDetails(tvShowId: Int?, fromDB: Boolean): Single<TVShow> {
        if(fromDB) {
            return dao.getTVShowDetails(tvShowId)
        }
        return api.getTVShowDetails(tvShowId, "videos,images,credits")
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getTVShowDetails(tvShowId);
    }

    fun saveTVShow(tvShow: TVShow) {
        InsertMovieTask(dao).execute(tvShow)
    }

    /**
     * AsyncTask that will insert one row to the database
     */
    class InsertMovieTask internal constructor(private val dao: TVShowDao): AsyncTask<TVShow?, Void?, Void?>() {
        override fun doInBackground(vararg params: TVShow?): Void? {
            dao.saveTVShow(params[0])
            return null
        }
    }
}
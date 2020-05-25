package com.bossdga.filmender.source

import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.model.content.TVShowResponse
import com.bossdga.filmender.source.network.api.TVShowAPI
import com.bossdga.filmender.source.persistence.TVShowDao
import io.reactivex.Observable

/**
 * Repository to execute database and network operations
 */
class TVShowRepository(private val dao: TVShowDao, private val api: TVShowAPI) {
    fun getTVShows(page: Int, airDateGte: String?, airDateLte: String?, voteAverageGte: String?, withGenres: String?): Observable<TVShowResponse> {
        return api.getTVShows(page, airDateGte, airDateLte, voteAverageGte, withGenres)
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getTVShows();
    }

    fun getTVShowDetails(tvShowId: Int?, appendToResponse: String?): Observable<TVShow> {
        return api.getTVShowDetails(tvShowId, appendToResponse)
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getTVShowDetails(tvShowId);
    }
}
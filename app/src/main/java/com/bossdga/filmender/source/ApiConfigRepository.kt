package com.bossdga.filmender.source

import com.bossdga.filmender.model.ApiConfig
import com.bossdga.filmender.source.network.api.ConfigurationAPI
import com.bossdga.filmender.source.persistence.ApiConfigDao
import io.reactivex.Observable

/**
 * Repository to execute database and network operations
 */
class ApiConfigRepository(private val dao: ApiConfigDao, private val api: ConfigurationAPI) {
    fun getConfiguration(): Observable<ApiConfig> {
        return api.getConfiguration()
        // TODO Add local database access in case there is not network connectivity or for caching purposes
        //return dao.getConfiguration();
    }
}
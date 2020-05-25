package com.bossdga.filmender.source.network.api

import com.bossdga.filmender.model.ApiConfig
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Interface that defines the API requests
 */
interface ConfigurationAPI {
    /**
     * Gets a list of movies
     * @return
     */
    @GET("/3/configuration")
    fun getConfiguration(): Observable<ApiConfig>
}
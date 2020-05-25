package com.bossdga.filmender.source.persistence

import androidx.room.Dao
import androidx.room.Query
import com.bossdga.filmender.model.ApiConfig
import io.reactivex.Observable

/**
 * Dao to interact with the database
 */
@Dao
interface ApiConfigDao {
    @Query("SELECT * FROM configuration")
    fun getConfiguration(): Observable<ApiConfig>
}
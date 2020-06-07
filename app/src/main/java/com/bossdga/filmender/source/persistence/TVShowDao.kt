package com.bossdga.filmender.source.persistence

import androidx.room.*
import com.bossdga.filmender.model.content.TVShow
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Dao to interact with the database
 */
@Dao
interface TVShowDao {
    @Query("SELECT * FROM tv_show")
    fun getTvShows(): Observable<List<TVShow>>

    @Query("SELECT * FROM tv_show WHERE id = :tvShowId")
    fun getTVShowDetails(tvShowId: Int?): Single<TVShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTVShow(tvShow: TVShow?)

    @Delete
    fun deleteTVShow(tvShow: TVShow?)
}
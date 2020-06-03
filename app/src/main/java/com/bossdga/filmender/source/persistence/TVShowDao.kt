package com.bossdga.filmender.source.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bossdga.filmender.model.content.TVShow
import io.reactivex.Observable

/**
 * Dao to interact with the database
 */
@Dao
interface TVShowDao {
    @Query("SELECT * FROM tv_show")
    fun getTvShows(): Observable<List<TVShow>>

    @Query("SELECT * FROM tv_show WHERE id = :tvShowId")
    fun getTVShowDetails(tvShowId: Int): Observable<TVShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTVShow(tvShow: TVShow?)
}
package com.bossdga.filmender.source.persistence

import androidx.room.Dao
import androidx.room.Query
import com.bossdga.filmender.model.content.Movie
import io.reactivex.Observable

/**
 * Dao to interact with the database
 */
@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getMovies(): Observable<List<Movie>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getMovieDetails(movieId: Int): Observable<Movie>
}
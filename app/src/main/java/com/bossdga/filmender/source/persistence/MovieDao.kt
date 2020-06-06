package com.bossdga.filmender.source.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bossdga.filmender.model.content.Movie
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Dao to interact with the database
 */
@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getMovies(): Observable<List<Movie>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getMovieDetails(movieId: Int?): Single<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovie(movie: Movie?)
}
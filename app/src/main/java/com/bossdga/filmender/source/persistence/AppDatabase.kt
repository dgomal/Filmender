package com.bossdga.filmender.source.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bossdga.filmender.model.ApiConfig
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.model.converter.*

/**
 * Class that represents a Room database
 */
@Database(entities = [Movie::class, TVShow::class, ApiConfig::class], version = 1)
@TypeConverters(GenreListConverter::class, VideoListConverter::class, ImageListConverter::class, ImageConfigConverter::class, ImageConverter::class, VideoConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TVShowDao
    abstract fun apiConfigDao(): ApiConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "filmender_db").build()
    }
}
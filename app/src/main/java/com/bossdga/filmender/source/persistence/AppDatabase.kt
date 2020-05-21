package com.bossdga.filmender.source.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bossdga.filmender.model.Movie
import com.bossdga.filmender.model.TVShow
import com.bossdga.filmender.model.converter.GenreListConverter
import com.bossdga.filmender.model.converter.ImageListConverter
import com.bossdga.filmender.model.converter.VideoListConverter

/**
 * Class that represents a Room database
 */
@Database(entities = [Movie::class, TVShow::class], version = 1)
@TypeConverters(GenreListConverter::class, VideoListConverter::class, ImageListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TVShowDao

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
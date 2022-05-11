package com.example.happyplaces.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Place::class], version = 2, exportSchema = false)
abstract class PlaceDataBase : RoomDatabase() {

    private val dataBase: PlaceDataBase? = null

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PlaceDataBase? = null

        fun getDatabase(context: Context): PlaceDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlaceDataBase::class.java,
                    "place_database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        const val DATABASE_NAME = "database"
    }

    abstract fun getTripDao(): PlaceDao
}
package com.example.happyplaces.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Place::class], version = 1)
abstract class PlaceDataBase : RoomDatabase() {

    val DATABASE_NAME = "database"

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
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}